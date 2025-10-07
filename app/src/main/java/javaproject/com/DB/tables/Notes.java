package javaproject.com.DB.tables;

public class Notes {
    private int userid;
    private String noteText;
    private String creationDate;

    
    public int getUserId(){
        return userid;
    }

    public void setUserID(int userId){
        this.userid = userId;
    }

    public String getNoteText(){
        return noteText;
    }

    public void setNoteText(String text){
        this.noteText = text;
    }

    public String creationDate() {
        return creationDate;
    }

    public void setCreationDate(String date){
        this.creationDate = date;
    }

    @Override
    public String toString() {
        return (
            "notes{" +
            "userid='" +
            userid +
            '\'' +
            ", note='" +
            noteText +
            '\'' +
            ", Date='" +
            creationDate +
            '}'
            );
    }
}
