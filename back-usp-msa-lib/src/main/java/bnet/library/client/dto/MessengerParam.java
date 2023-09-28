package bnet.library.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MessengerParam implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3684716301701548255L;
    private String systemId;
    private MessengerUser sender = new MessengerUser();
    private List<MessengerUser> recipients = new ArrayList<MessengerUser>();
    private String title;
    private String content;

    public String getSystemId() {
        return systemId;
    }
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
    public MessengerUser getSender() {
        return sender;
    }
    public void setSender(MessengerUser sender) {
        this.sender = sender;
    }
    public void setSender(String messengerId, String userNm) {
        if (this.getSender() == null) {
            this.setSender(new MessengerUser());
        }
        this.getSender().setMessengerId(messengerId);
        this.getSender().setUserNm(userNm);
    }
    public void clearRecipients() {
        if (this.getRecipients() == null) {
            return;
        }
        this.getRecipients().clear();
    }
    public void addRecipient(String messengerId, String userNm) {
        if (this.getRecipients() == null) {
            this.setRecipients(new ArrayList<MessengerUser>());
        }
        this.getRecipients().add(new MessengerUser(messengerId, userNm));
    }
    public List<MessengerUser> getRecipients() {
        List<MessengerUser> recipients = new ArrayList<>();
        if(this.recipients != null) {
        	recipients.addAll(this.recipients);
        }
		return recipients;
    }
    public void setRecipients(List<MessengerUser> recipients) {
        this.recipients = new ArrayList<>();
        if(recipients != null) {
        	this.recipients.addAll(recipients);
        }
    }
    public String getRecipientsString() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (MessengerUser vo: getRecipients()) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(vo.getMessengerId());
            i++;
        }
        return builder.toString();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String messageTitle) {
        this.title = messageTitle;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String messageContent) {
        this.content = messageContent;
    }

}
