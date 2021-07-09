package group1.appchat_opensource.objects;

public class Message {
    String senderId,receiverId,content,isSeen,time;

    public Message(String senderId, String receiverId, String content, String isSeen, String time) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isSeen = isSeen;
        this.time = time;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
