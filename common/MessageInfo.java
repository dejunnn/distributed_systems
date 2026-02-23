/*
 *
 * Updated on Feb 2025
 */
package templatesgiven.common;

import java.io.Serializable;

/**
 * Utility class to encapsulate the message information to be passed from client to server.
 * Information can be extracted or constructed as a String for transmitting.
 */
public class MessageInfo implements Serializable {

  private int totalMessages;
  private int messageNum;
  private float message;

  public MessageInfo(int total, int msgNum, float value) {
    totalMessages = total;
    messageNum = msgNum;
    message = value;
  }

  public MessageInfo(String msg) throws Exception {
    String[] fields = msg.split(";");

    if (fields.length != 3)
      throw new Exception("MessageInfo: Invalid string for message construction: " + msg);

    totalMessages = Integer.parseInt(fields[0]);
    messageNum = Integer.parseInt(fields[1]);
    message = Float.parseFloat(fields[2]);
  }

  @Override
  public String toString() {
    return new String(totalMessages + ";" + messageNum + ";" + message + "\n");
  }

  public float getMessage() {
    return message;
  }

  public int getMessageNum() {
    return messageNum;
  }

  public int getTotalMessages() {
    return totalMessages;
  }

  public void setMessage(float message) {
    this.message = message;
  }

  public void setMessageNum(int messageNum) {
    this.messageNum = messageNum;
  }

  public void setTotalMessages(int totalMessages) {
    this.totalMessages = totalMessages;
  }
}
