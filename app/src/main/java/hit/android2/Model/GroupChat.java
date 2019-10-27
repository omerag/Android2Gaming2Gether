package hit.android2.Model;

public class GroupChat {

    private String sender;
    private String senderName;
    private String message;

    public GroupChat(String sender, String senderName, String message) {
        this.sender = sender;
        this.senderName = senderName;
        this.message = message;
    }

    public GroupChat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
