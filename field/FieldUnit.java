package field;

/*
 * Updated on Feb 2025
 */
import centralserver.ICentralServer;
import common.MessageInfo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* You can add/change/delete class attributes if you wish.
 *
 * You can also add other methods and change the implementation of the methods
 * provided, as long as you DO NOT CHANGE the interface.
 */

public class FieldUnit implements IFieldUnit {
  private ICentralServer central_server;

  /* Note: Could you discuss in one line of comment you think can be
   * an appropriate size for buffsize? (used to init DatagramPacket?)
   * buffsize = 2048 bytes: a MessageInfo string (total;num;value\n) is at
   * most ~40 chars, so 2048 gives generous headroom for any payload.
   */
  private static final int buffsize = 2048;
  private int timeout = 50000;

  private List<MessageInfo> receivedMessages;
  private float[] movingAverages;
  private int totalExpected;

  public FieldUnit() {
    // Initialise data structures
    this.receivedMessages = new ArrayList<>();
    this.movingAverages = null;
    this.totalExpected = 0;
  }

  @Override
  public void addMessage(MessageInfo msg) {
    // Save received message in receivedMessages
    this.receivedMessages.add(msg);
  }

  @Override
  public void sMovingAverage(int k) {
    // Sort by message number to handle out-of-order delivery
    this.receivedMessages.sort(Comparator.comparingInt(MessageInfo::getMessageNum));

    // Compute SMA and store values in a class attribute
    int n = this.receivedMessages.size();
    this.movingAverages = new float[n];

    for (int i = 0; i < n; i++) {
      if (i < k - 1) {
        // If index i < k-1 points, use raw values
        this.movingAverages[i] = this.receivedMessages.get(i).getMessage();
      } else {
        // If index i >= k-1 points, compute average of i-k+1 to i values
        float sum = 0;
        for (int j = i - k + 1; j <= i; j++) {
          sum += this.receivedMessages.get(j).getMessage();
        }
        this.movingAverages[i] = sum / k;
      }
    }
  }

  @Override
  public void receiveMeasures(int port, int timeout) throws SocketException {
    this.timeout = timeout;

    // Create UDP socket and bind to local port 'port'
    DatagramSocket socket = new DatagramSocket(port);
    socket.setSoTimeout(this.timeout);

    boolean listen = true;
    int msgTot = -1;

    System.out.println("[Field Unit] Listening on port: " + port);

    while (listen) {

      // Receive until all messages in the transmission (msgTot) have been received or until there
      // is nothing more to be received
      try {
        byte[] buffer = new byte[buffsize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        String received = new String(packet.getData(), 0, packet.getLength()).trim();
        MessageInfo msg = new MessageInfo(received);

        // If this is the first message, initialise the receiving data structure before storing it.
        if (msgTot == -1) {
          msgTot = msg.getTotalMessages();
          this.totalExpected = msgTot;
          this.receivedMessages = new ArrayList<>();
        }

        System.out.println(
            "[Field Unit] Message "
                + msg.getMessageNum()
                + " out of "
                + msg.getTotalMessages()
                + " received. Value = "
                + msg.getMessage());

        // Store the message
        addMessage(msg);

        // Keep listening UNTIL done with receiving
        if (receivedMessages.size() >= msgTot) {
          listen = false;
        }

      } catch (SocketTimeoutException e) {
        System.out.println("[Field Unit] Socket timed out waiting for messages.");
        listen = false;
      } catch (Exception e) {
        System.err.println("[Field Unit] Error receiving message: " + e.getMessage());
      }
    }

    // Close socket
    socket.close();
  }

  public static void main(String[] args) throws SocketException {
    if (args.length < 2) {
      System.out.println("Usage: ./fieldunit.sh <UDP rcv port> <RMI server HostName/IPAddress>");
      return;
    }

    // Parse arguments
    int port = Integer.parseInt(args[0]);
    String rmiAddress = args[1];

    // Construct Field Unit Object
    FieldUnit fieldUnit = new FieldUnit();

    // Call initRMI on the Field Unit Object
    fieldUnit.initRMI(rmiAddress);

    while (true) {
      // Wait for incoming transmission
      try {
        fieldUnit.receiveMeasures(port, 5000);
      } catch (SocketException e) {
        System.err.println("[Field Unit] Socket exception: " + e.getMessage());
        break;
      }

      if (fieldUnit.receivedMessages.isEmpty()) {
        System.out.println("[Field Unit] No messages received, waiting again...");
        System.out.println("[Field Unit] Listening on port: " + port);
        continue;
      }

      // Compute Averages - call sMovingAverage() on Field Unit object
      System.out.println("===============================");
      System.out.println("[Field Unit] Computing SMAs");
      fieldUnit.sMovingAverage(7);

      // Compute and print stats
      fieldUnit.printStats();

      // Send data to the Central Serve via RMI and wait for incoming transmission again
      System.out.println("[Field Unit] Sending SMAs to RMI");
      fieldUnit.sendAverages();

      System.out.println("[Field Unit] Listening on port: " + port);
    }
  }

  @Override
  public void initRMI(String address) {

    // Initialise Security Manager (If JAVA version earlier than version 17)
    // Not required

    try {
      // Bind to RMIServer
      Registry registry = LocateRegistry.getRegistry(address);
      central_server = (ICentralServer) registry.lookup("CentralServer");
      System.out.println("[Field Unit] Connected to CentralServer via RMI at " + address);
    } catch (RemoteException | NotBoundException e) {
      System.err.println("[Field Unit] RMI init error: " + e.getMessage());
    }
  }

  @Override
  public void sendAverages() {
    if (movingAverages == null || movingAverages.length == 0) {
      System.err.println("[Field Unit] No averages to send.");
      return;
    }
    int total = movingAverages.length;

    // Attempt to send messages the specified number of times
    for (int i = 0; i < total; i++) {
      MessageInfo msg = new MessageInfo(total, i + 1, movingAverages[i]);
      try {
        central_server.receiveMsg(msg);
      } catch (RemoteException e) {
        System.err.println(
            "[Field Unit] RMI send error for message " + (i + 1) + ": " + e.getMessage());
      }
    }
  }

  @Override
  public void printStats() {

    // Find out how many messages were missing
    int received = receivedMessages.size();
    int missing = totalExpected - received;

    // Print stats (i.e. how many message missing? do we know their sequence number? etc.)
    System.out.println("Total Missing Messages = " + missing + " out of " + totalExpected);

    if (missing > 0) {
      // Determine which sequence numbers were not received
      List<Integer> receivedNums = new ArrayList<>();
      for (MessageInfo m : receivedMessages) {
        receivedNums.add(m.getMessageNum());
      }
      System.out.print("Missing sequence numbers: ");
      for (int i = 1; i <= totalExpected; i++) {
        if (!receivedNums.contains(i)) {
          System.out.print(i + " ");
        }
      }
      System.out.println();
    }

    // Now re-initialise data structures for next time
    receivedMessages = new ArrayList<>();
    movingAverages = null;
    totalExpected = 0;
  }
}
