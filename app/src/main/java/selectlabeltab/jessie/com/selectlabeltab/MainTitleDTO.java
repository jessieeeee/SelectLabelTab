package selectlabeltab.jessie.com.selectlabeltab;

import java.io.Serializable;

//标题DTO
public class MainTitleDTO implements Serializable{
    private int id;
    private String Title;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
