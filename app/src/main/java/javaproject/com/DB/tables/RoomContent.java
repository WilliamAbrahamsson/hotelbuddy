package javaproject.com.DB.tables;

public class RoomContent {

    private int id;
    private String content;
    private String type;

    // ID.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Content.

    public String getcontent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Type.

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // to string
    @Override
    public String toString() {
        return ("Content_id [id=" +
                id +
                ", content=" +
                content +
                ", type=" +
                type +
                "]");
    }

}
