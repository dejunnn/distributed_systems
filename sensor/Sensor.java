package sensor;

/*
 * Updated on Feb 2025
 */
import common.MessageInfo;

import java.io.IOException;
import java.net.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

  private DatagramSocket datagramSocket;
  private byte[] buffer;

  private String destAddress;
  private int destPort;
  private int totalMessages;

  /* Note: Could you discuss in one line of comment what you think can be
   * an appropriate size for buffsize? (Which is used to init DatagramPacket?)
   * buffsize = 2048 bytes is appropriate: MessageInfo serialised as a string
   * (totalMessages;msgNum;value\n) is well under 100 bytes, so 2048 gives
   * ample headroom for any reasonable payload without wasting memory.
   */
  private static final int buffsize = 2048;

  public Sensor(String address, int port, int totMsg) {
    // Build Sensor Object
    this.destAddress = address;
    this.destPort = port;
    this.totalMessages = totMsg;
    this.buffer = new byte[buffsize];
    try {
      this.datagramSocket = new DatagramSocket();
    } catch (SocketException e) {
      System.err.println("[Sensor] Could not create socket: " + e.getMessage());
    }
  }

  @Override
  public void run(int N) throws InterruptedException {
    DateTimeFormatter fmt =
        DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneId.systemDefault());
    Instant firstSent = null;
    Instant lastSent = null;

    // Send N measurements to the destination address and port
    for (int i = 1; i <= N; i++) {
      float measurement = this.getMeasurement();
      MessageInfo msg = new MessageInfo(N, i, measurement);

      // Call sendMessage() to send the msg to destination
      sendMessage(destAddress, destPort, msg);

      Instant now = Instant.now();
      if (i == 1) firstSent = now;
      if (i == N) lastSent = now;

      System.out.println(
          "[Sensor] Sending message "
              + i
              + " out of "
              + N
              + ". Measure = "
              + measurement
              + " | time="
              + fmt.format(now));
    }

    if (firstSent != null && lastSent != null) {
      long durationMs = java.time.Duration.between(firstSent, lastSent).toMillis();
      System.out.println("[Sensor] First sent : " + fmt.format(firstSent));
      System.out.println("[Sensor] Last sent  : " + fmt.format(lastSent));
      System.out.println("[Sensor] Duration   : " + durationMs + " ms");
    }

    // Close datagram socket after sending all messages
    datagramSocket.close();
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

    // Call constructor of sensor to build Sensor object
    Sensor sensor = new Sensor(address, port, totMsg);

    // Use Run to send the messages and catch any InterruptedException
    try {
      sensor.run(totMsg);
    } catch (InterruptedException e) {
      System.err.println("[Sensor] Run Interrupted: " + e.getMessage());
    }
  }

  @Override
  public void sendMessage(String address, int port, MessageInfo msg) {
    try {
      // Build destination address object
      InetAddress dest = InetAddress.getByName(address);

      // Build datagram packet to send
      byte[] data = msg.toString().getBytes();
      if (data.length > buffer.length) {
        throw new IOException(
            "Message size " + data.length + " exceeds buffer size " + buffer.length);
      }
      System.arraycopy(data, 0, buffer, 0, data.length);
      DatagramPacket packet = new DatagramPacket(buffer, data.length, dest, port);

      // Send packet
      datagramSocket.send(packet);
    } catch (IOException e) {
      System.err.println("[Sensor] Error sending message: " + e.getMessage());
    }
  }

  @Override
  public float getMeasurement() {
    Random r = new Random();
    measurement = r.nextFloat() * (max_measure - min_measure) + min_measure;
    return measurement;
  }
}
