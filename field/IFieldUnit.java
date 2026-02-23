package field;

/*
 * Updated on Feb 2025
 */
import common.MessageInfo;

import java.net.SocketException;

public interface IFieldUnit {
  /* Save message into local data structure */
  public void addMessage(MessageInfo m);

  /* Compute the k-points moving averages of all messages */
  public void sMovingAverage(int k);

  /* Listen on UDP port UNTIL there is no more to receive */
  public void receiveMeasures(int port, int timeout) throws SocketException;

  /* Set up RMI client */
  public void initRMI(String address);

  /* In this function we call CentralServer.receiveMsg() to send the message to Central Server via RMI */
  public void sendAverages();

  /* Print Stats */
  public void printStats();
}
