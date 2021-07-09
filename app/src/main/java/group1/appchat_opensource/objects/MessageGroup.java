package group1.appchat_opensource.objects;

public class MessageGroup {
    String content,senderId,time,isSeen;

    public MessageGroup(String content, String senderId, String time, String isSeen) {
        this.content = content;
        this.senderId = senderId;
        this.time = time;
        this.isSeen = isSeen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }
}
