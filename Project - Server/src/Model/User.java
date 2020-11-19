package Model;

import Model.Connection.Connection;
import Model.Messages.Conversation;
import Model.Messages.Mail;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;
    private Gender gender;
    private byte[] proImage;

    public byte[] getProImage() {
        return proImage;
    }

    public void setProImage(byte[] proImage) {
        this.proImage = proImage;
    }

    private File profileImage;
    private String SECURITY_QUESTION;


    public String getSECURITY_QUESTION() {
        return SECURITY_QUESTION;
    }

    public void setSECURITY_QUESTION(String SECURITY_QUESTION) {
        this.SECURITY_QUESTION = SECURITY_QUESTION;
    }

    public String getSECURITY_QUESTION_ANSWER() {
        return SECURITY_QUESTION_ANSWER;
    }

    public void setSECURITY_QUESTION_ANSWER(String SECURITY_QUESTION_ANSWER) {
        this.SECURITY_QUESTION_ANSWER = SECURITY_QUESTION_ANSWER;
    }

    private String SECURITY_QUESTION_ANSWER;


    private String birthDate;


    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void editProfile(String fname, String lname, String phonenumb) {
        this.firstName = fname;
        this.lastName = lname;
        this.phoneNumber = phonenumb;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    private String gmailAddress;

    private transient ObjectInputStream inputStream;
    private transient ObjectOutputStream outputStream;


    public List<Mail> getSent() {
        return sent;
    }

    private List<Conversation> conversations = new ArrayList<>();
    private List<Conversation> inbox = new ArrayList<>();
    private List<Mail> sent = new ArrayList<>();
    private List<User> blockedUsers = new ArrayList<>();

    public List<User> getBlockedUsers() {
        return blockedUsers;
    }

    public void block(User user) {
        blockedUsers.add(user);
    }

    public void addSent(Mail mail) {
        sent.add(mail);
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void addConversation(Mail mail) {
        conversations.add(new Conversation(mail));
    }


    public File getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(File profileImage) {
        this.profileImage = profileImage;
    }

    public String getGmailAddress() {
        return gmailAddress;
    }

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        gmailAddress = username + "@gmail.com";
    }

    public void complete(File avatar, String phoneNumber, Gender gender) {
        this.profileImage = avatar;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public void setStreams(ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}

