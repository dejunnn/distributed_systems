package templatesgiven.field;

/*
 * Updated on Feb 2025
 */
import centralserver.ICentralServer;
import common.MessageInfo;
import field.IFieldUnit;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/* You can add/change/delete class attributes if you wish.
 *
 * You can also add other methods and change the implementation of the methods
 * provided, as long as you DO NOT CHANGE the interface.
 */

public class FieldUnit implements IFieldUnit {
  private ICentralServer central_server;

  /* Note: Could you discuss in one line of comment you think can be
   * an appropriate size for buffsize? (used to init DatagramPacket?)
   */

  private static final int buffsize = 2048;
  private int timeout = 50000;

  public FieldUnit() {
    /* TODO: Initialise data structures */
  }

  @Override
  public void addMessage(MessageInfo msg) {
    /* TODO: Save received message in receivedMessages */

  }

  @Override
  public void sMovingAverage(int k) {
    /* TODO: Compute SMA and store values in a class attribute */

  }

  @Override
  public void receiveMeasures(int port, int timeout) throws SocketException {

    this.timeout = timeout;

    /* TODO: Create UDP socket and bind to local port 'port' */

    boolean listen = true;

    System.out.println("[Field Unit] Listening on port: " + port);

    while (listen) {

      /* TODO: Receive until all messages in the transmission (msgTot) have been received or
      until there is nothing more to be received */

      /* TODO: If this is the first message, initialise the receive data structure before storing it. */

      /* TODO: Store the message */

      /* TODO: Keep listening UNTIL done with receiving  */

    }

    /* TODO: Close socket  */

  }

  public static void main(String[] args) throws SocketException {
    if (args.length < 2) {
      System.out.println("Usage: ./fieldunit.sh <UDP rcv port> <RMI server HostName/IPAddress>");
      return;
    }

    /* TODO: Parse arguments */

    /* TODO: Construct Field Unit Object */

    /* TODO: Call initRMI on the Field Unit Object */

    /* TODO: Wait for incoming transmission */

    /* TODO: Compute Averages - call sMovingAverage()
    on Field Unit object */

    /* TODO: Compute and print stats */

    /* TODO: Send data to the Central Serve via RMI and
     *        wait for incoming transmission again
     */

  }

  @Override
  public void initRMI(String address) {

    /* TODO: Initialise Security Manager (If JAVA version earlier than version 17) */

    /* TODO: Bind to RMIServer */

  }

  @Override
  public void sendAverages() {
    /* TODO: Attempt to send messages the specified number of times */

  }

  @Override
  public void printStats() {
    /* TODO: Find out how many messages were missing */

    /* TODO: Print stats (i.e. how many message missing?
     * do we know their sequence number? etc.) */

    /* TODO: Now re-initialise data structures for next time */

  }
}
