package centralserver;

import common.*;

/*
 * Updated on Feb 2025
 */
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/* You can add/change/delete class attributes if you think it would be
 * appropriate.
 *
 * You can also add helper methods and change the implementation of those
 * provided if you think it would be appropriate, as long as you DO NOT
 * CHANGE the provided interface.
 */

// extend appropriate classes and implement the appropriate interfaces
public class CentralServer extends UnicastRemoteObject implements ICentralServer {

  private List<MessageInfo> receivedMessages;
  private int totalExpected;

  protected CentralServer() throws RemoteException {
    super();
    // Initialise Array receivedMessages
    receivedMessages = new ArrayList<>();
    totalExpected = 0;
  }

  public static void main(String[] args) throws RemoteException {
    CentralServer cs = new CentralServer();

    // Configure Security Manager (If JAVA version earlier than version 17)
    // Not required

    // Create (or Locate) Registry
    Registry registry;
    try {
      registry = LocateRegistry.createRegistry(1099);
    } catch (RemoteException e) {
      registry = LocateRegistry.getRegistry(1099);
    }

    // Bind to Registry
    registry.rebind("CentralServer", cs);

    System.out.println("Central Server ready");
  }

  @Override
  public void receiveMsg(MessageInfo msg) {
    // If this is the first message, reset counter and initialise data structure.
    if (msg.getMessageNum() == 1) {
      receivedMessages = new ArrayList<>();
      totalExpected = msg.getTotalMessages();
    }

    System.out.println(
        "[Central Server] Received message "
            + msg.getMessageNum()
            + " out of "
            + msg.getTotalMessages()
            + ". Measure = "
            + msg.getMessage());

    // Save current message
    receivedMessages.add(msg);

    // If done with receiveing prints stats.
    if (receivedMessages.size() >= totalExpected) {
      printStats();
    }
  }

  public void printStats() {
    // Find out how many messages were missing */
    int received = receivedMessages.size();
    int missing = totalExpected - received;

    System.out.println("Total Missing Messages = " + missing + " out of " + totalExpected);

    // Print stats (i.e. how many message missing? do we know their sequence number? etc.)
    if (missing > 0) {
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
    totalExpected = 0;
  }
}
