package bnet.library.client.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MessengerUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6782742595065276273L;
    private String messengerId;
    private String userNm;

    public MessengerUser() {

    }
    public MessengerUser(String messengerId, String userNm) {
        this.messengerId = messengerId;
        this.userNm = userNm;
    }
    public String getMessengerId() {
        return messengerId;
    }
    public void setMessengerId(String messengerId) {
        this.messengerId = messengerId;
    }
    public String getUserNm() {
        return userNm;
    }
    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
}
