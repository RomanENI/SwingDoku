import javax.swing.*;

public class Message{



    String msgText;
    JButton msgButton;
    int msgLength;

    Message(String text, int msgLength, JButton button){
        this.msgText = text;
        this.msgButton = button;
        this.msgLength = msgLength;



    }


    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public JButton getMsgButton() {
        return msgButton;
    }

    public void setMsgButton(JButton msgButton) {
        this.msgButton = msgButton;
    }







}
