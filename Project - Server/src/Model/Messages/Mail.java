package Model.Messages;

import Model.User;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Mail implements Serializable {


    private boolean spam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(text, mail.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    public boolean isSpam() {
        return spam;
    }

    public void setSpam(boolean spam) {
        this.spam = spam;
    }

    public boolean isBookMarked() {
        return bookMarked;
    }

    public void setBookMarked(boolean bookMarked) {
        this.bookMarked = bookMarked;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isInTrash() {
        return inTrash;
    }

    public void setInTrash(boolean inTrash) {
        this.inTrash = inTrash;
    }

    private boolean bookMarked;
    private boolean read;
    private boolean inTrash;
    private boolean fileIsDownloaded;
    private boolean fromGoogleDelivery;

    public boolean isFromGoogleDelivery() {
        return fromGoogleDelivery;
    }

    public void setFromGoogleDelivery(boolean fromGoogleDelivery) {
        this.fromGoogleDelivery = fromGoogleDelivery;
    }

    public boolean isFileIsDownloaded() {
        return fileIsDownloaded;
    }

    public void setFileIsDownloaded(boolean fileIsDownloaded) {
        this.fileIsDownloaded = fileIsDownloaded;
    }

    private String sender;
    private String receiver;
    private String subject;
    private User receiverUser;
    private User senderUser;

    private File attachedFile;
    private byte[] attached;

    public byte[] getAttached() {
        return attached;
    }

    public void setAttached(byte[] attached) {
        this.attached = attached;
    }

    public File getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(File attachedFile) {
        this.attachedFile = attachedFile;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    private String text;
    // date and time
    private LocalDateTime time;


    public Mail(LocalDateTime time, String sender, String receiver, String subject, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getTimeString() {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm a");
        return dtf.format(time);
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }
}
