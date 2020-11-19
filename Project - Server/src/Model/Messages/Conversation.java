package Model.Messages;

import Model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Conversation implements Serializable {
    private List<Mail> mails = new ArrayList<>();
    private Mail lastMail;
    private User sender;
    private User receiver;

    private boolean bookMarked;
    private boolean read;
    private boolean inTrash;


    public String getSubject() {
        return subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject);
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String subject;

    public void setBookMarked(boolean bookMarked) {
        this.bookMarked = bookMarked;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isBookMarked() {
        return bookMarked;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isInTrash() {
        return inTrash;
    }

    public void setInTrash(boolean inTrash) {
        this.inTrash = inTrash;
    }

    public Conversation(Mail mail, User sender, User receiver) {
        mails.add(mail);
        lastMail = mail;
        this.sender = sender;
        this.receiver = receiver;
        subject = mail.getSubject();
    }

    public Conversation(Mail mail) {
        mails.add(mail);
        lastMail = mail;
        this.sender = mail.getSenderUser();
        this.receiver = mail.getReceiverUser();
        subject = mail.getSubject();
    }

    public void setLastMail(Mail lastMail) {
        this.lastMail = lastMail;
    }

    public Conversation(String subject) {
        this.subject = subject;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public Mail getLastMail() {
        return lastMail;
    }

    public void addMail(Mail mail) {
        mails.add(mail);
        lastMail = mail;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
// date and time
}
