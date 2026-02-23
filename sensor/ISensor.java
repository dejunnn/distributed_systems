package templatesgiven.sensor;

/*
 * Updated on Feb 2025
 */
import common.MessageInfo;

public interface ISensor {
  /* sends N measurements to the Field Unit*/
  public void run(int N) throws InterruptedException;

  /* Send message 'msg' to 'address' on port 'port' */
  public void sendMessage(String address, int port, MessageInfo msg);

  /* Simulate one measurement */
  public float getMeasurement();
}
