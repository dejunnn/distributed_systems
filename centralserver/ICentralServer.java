package centralserver;

/*
 * Updated on Feb 2025
 */
import common.MessageInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICentralServer extends Remote {
  /* Receive Message. Called by the client send a value to a Central Server object via RMI */
  public void receiveMsg(MessageInfo m) throws RemoteException;
}
