package templatesgiven.sensor;

/*
 * Updated on Feb 2025
 */
import common.MessageInfo;
import sensor.ISensor;

import java.io.IOException;
import java.net.*;
import java.util.Random;

/* You can add/change/delete class attributes if you think it appropriate.
 *
 * You can also add helper methods and change the implementation of those
 * provided, as long as you DO NOT CHANGE the interface.
 */

public class Sensor implements ISensor {
  private float measurement;

  private static final int max_measure = 50;
  private static final int min_measure = 10;

  private DatagramSocket s;
  private byte[] buffer;

  /* Note: Could you discuss in one line of comment what you think can be
   * an appropriate size for buffsize?
   * (Which is used to init DatagramPacket?)
   */
  private static final int buffsize = 2048;

  public Sensor(String address, int port, int totMsg) {
    /* TODO: Build Sensor Object */
  }

  @Override
  public void run(int N) throws InterruptedException {
    /* TODO: Send N measurements */

    /* Hint: You can get ONE measurement by calling
     *
     * float measurement = this.getMeasurement();
     */

    /* TODO: Call sendMessage() to send the msg to destination */

  }

  public static void main(String[] args) {
    if (args.length < 3) {
      System.out.println("Usage: ./sensor.sh field_unit_address port number_of_measures");
      return;
    }

    /* Parse input arguments */
    String address = args[0];
    int port = Integer.parseInt(args[1]);
    int totMsg = Integer.parseInt(args[2]);

    /* TODO: Call constructor of sensor to build Sensor object*/

    /* TODO: Use Run to send the messages */

  }

  @Override
  public void sendMessage(String address, int port, MessageInfo msg) {
    String toSend = msg.toString();

    /* TODO: Build destination address object */

    /* TODO: Build datagram packet to send */

    /* TODO: Send packet */

  }

  @Override
  public float getMeasurement() {
    Random r = new Random();
    measurement = r.nextFloat() * (max_measure - min_measure) + min_measure;

    return measurement;
  }
}
