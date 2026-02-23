package centralserver;

import centralserver.ICentralServer;
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

/* TODO extend appropriate classes and implement the appropriate interfaces */
public class CentralServer implements ICentralServer {

  protected CentralServer() throws RemoteException {
    super();

    /* TODO: Initialise Array receivedMessages */
  }

  public static void main(String[] args) throws RemoteException {
    CentralServer cs = new CentralServer();

    /* TODO: Configure Security Manager (If JAVA version earlier than version 17) */

    /* TODO: Create (or Locate) Registry */

    /* TODO: Bind to Registry */

    System.out.println("Central Server is running...");
  }

  @Override
  public void receiveMsg(MessageInfo msg) {
    System.out.println(
        "[Central Server] Received message "
            + (msg.getMessageNum())
            + " out of "
            + msg.getTotalMessages()
            + ". Measure = "
            + msg.getMessage());

    /* TODO: If this is the first message, reset counter and initialise data structure. */

    /* TODO: Save current message */

    /* TODO: If done with receiveing prints stats. */

  }

  public void printStats() {
    /* TODO: Find out how many messages were missing */

    /* TODO: Print stats (i.e. how many message missing?
     * do we know their sequence number? etc.) */

    /* TODO: Now re-initialise data structures for next time */

  }
}
