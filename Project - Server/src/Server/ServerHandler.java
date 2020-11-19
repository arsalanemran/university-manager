package Server;

import Model.Messages.Conversation;
import Model.Messages.Mail;
import Model.Messages.Message;
import Model.Messages.MessageType;
import Model.User;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ServerHandler {
    private static ArrayList<User> ALL_USERS = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private LocalDateTime time;

    static {
        try {
            openUsers();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ServerHandler(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }


    void handle(Message message) throws IOException, ClassNotFoundException {
//        DataBase.readDB();
//        ALL_USERS = DataBase.reReadDB_users();
        switch (message.getMessageType()) {
            case CheckSecurityQuestion:
                User SQU = getUser(message.getSender());
                if (SQU != null && SQU.getSECURITY_QUESTION().equals(message.getReceiver()) && SQU.getSECURITY_QUESTION_ANSWER().equals(message.getMessageText())) {
                    outputStream.writeBoolean(true);
                    outputStream.flush();
                } else
                    outputStream.writeBoolean(false);
                break;
            case ForgotPass:
                User ForgotPassUser = getUser(message.getMessageText());
                ForgotPassUser.setPassword(message.getSender());
                break;
            case ChangePass:
                User editedPassUser = getUser(message.getUser().getUsername());
                editedPassUser.setPassword(message.getSender());
                message.setUser(editedPassUser);
                outputStream.writeObject(message);
                break;
            case ChangePro:
                User editedProUser = getUser(message.getUser().getUsername());
                editedProUser.editProfile(message.getSender(), message.getReceiver(), message.getMessageText());
                message.setUser(editedProUser);
                outputStream.writeObject(message);
                break;
            case AvailableUsername:
                outputStream.writeBoolean(!ALL_USERS.contains(new User(null, null, message.getSender(), null)));
                outputStream.flush();
                break;
            case Forward:
            case Text:
                User receiver = getUser(message.getMail().getReceiver());
                User sender = getUser(message.getMail().getSender());
                if (receiver != null) {
                    receiver.addConversation(message.getMail());
//                    receiver.getOutputStream().writeObject(message);
                    sender.addSent(message.getMail());
                    Message SENT = new Message(MessageType.Sent, "", "", "");
                    SENT.setUser(sender);
                    outputStream.writeObject(SENT);
                    String absolutePath;
                    absolutePath = getAddressString(message);
                    if (message.getMessageType().equals(MessageType.Text)) {
                        System.out.println(receiver.getUsername() + " receive");
                        System.out.println("message: " + sender.getUsername() + absolutePath);
                        System.out.println("time: " + TimeString());
                        System.out.println();
                        System.out.println(sender.getUsername() + " send");
                        System.out.println("message: " + message.getMail().getSubject() + absolutePath + " to " + receiver.getUsername());
                        System.out.println("time: " + TimeString());
                        System.out.println();
                    } else if (message.getMessageType().equals(MessageType.Forward)) {
                        System.out.println(sender.getUsername() + " forward");
                        System.out.println("message: " + message.getMail().getSubject() + absolutePath + " from " + sender.getUsername() + " to " + receiver.getUsername());
                        System.out.println("time: " + TimeString());
                    }

                }
                // else
                // we should send an email with Error messageType and ....
                else {
                    Message notFound = new Message(MessageType.Error, "", "", "");
                    Mail doNotExists = new Mail(LocalDateTime.now(), "mailerdaemon@googlemail.com", sender.getUsername(), null, "The receiver user does not exist!");
                    doNotExists.setSenderUser(new User("Mail Delivery Subsystem", " ", "mailerdaemon@googlemail.com", ""));
                    doNotExists.setFromGoogleDelivery(true);
                    sender.addConversation(doNotExists);
//                    outputStream.writeObject();
                }
                break;
            case Login:
                User user = getUser(message.getSender());
                Message msg = new Message(MessageType.Login, null, null, null);
                msg.setUser(user);
                if (user == null) {
                    msg.setOkLogedin(false);
                    outputStream.writeObject(msg);
                } else if (user.getPassword().equals(message.getReceiver())) {
                    msg.setOkLogedin(true);
                    outputStream.writeObject(msg);
                    System.out.println(user.getUsername() + " Sign in");
                    System.out.println("time: " + TimeString());

                } else {
                    msg.setOkLogedin(false);
                    outputStream.writeObject(msg);
                }
                outputStream.flush();
                break;
            case Connect:
                System.out.println(message.getUser().getUsername() + " connect");
                System.out.println("time: " + TimeString());
                break;
            case Refresh:
                message.setUser(ALL_USERS.get(ALL_USERS.indexOf(message.getUser())));
                outputStream.writeObject(message);
                break;
            case Reply:
                User receiverReply = getUser(message.getMail().getReceiver());
                User senderReply = getUser(message.getMail().getSender());
                Conversation REPLY = receiverReply.getConversations().get(receiverReply.getConversations().indexOf(new Conversation(message.getMail().getSubject())));
                REPLY.addMail(message.getMail());
                senderReply.addSent(message.getMail());
                System.out.println(senderReply.getUsername() + " receive");
                System.out.println("message: " + message.getMail().getSubject() + " to " + receiverReply.getUsername());
                System.out.println("time: " + TimeString());
                break;
            case MailBookmark:
                User MBU = getUser(message.getUser().getUsername());
                Conversation MBMC = MBU.getConversations().get(MBU.getConversations().indexOf(new Conversation(message.getReceiver())));
                Mail mail = MBMC.getMails().get(MBMC.getMails().indexOf(message.getMail()));
                mail.setBookMarked(!mail.isBookMarked());
                System.out.println(MBU.getUsername() + (mail.isBookMarked() ? " important" : " unimportant"));
                System.out.println("message: " + message.getMail().getSubject() + MBU.getUsername() + " as " + (mail.isBookMarked() ? "important" : "unimportant"));
                System.out.println("time: " + TimeString());
                break;
            case MailDelete:
                User MDU = getUser(message.getUser().getUsername());
                Conversation MDC = MDU.getConversations().get(MDU.getConversations().indexOf(new Conversation(message.getReceiver())));
                MDC.getMails().get(MDC.getMails().indexOf(message.getMail())).setInTrash(true);
                MDC.getMails().remove(MDC.getMails().get(MDC.getMails().indexOf(message.getMail())));
                if (MDC.getMails().size() == 0)
                    MDU.getConversations().remove(MDC);
                else
                    MDC.setLastMail(MDC.getMails().get(MDC.getMails().size() - 1));
                System.out.println(MDU.getUsername() + " removemsg");
                System.out.println("message: " + message.getMail().getSubject() + " " + MDU.getUsername());
                System.out.println("time: " + TimeString());
                break;
            case MailRead:
                User MRU = getUser(message.getUser().getUsername());
                Conversation MRC = MRU.getConversations().get(MRU.getConversations().indexOf(new Conversation(message.getReceiver())));
                Mail mail1 = MRC.getMails().get(MRC.getMails().indexOf(message.getMail()));
                mail1.setRead(!mail1.isRead());
                System.out.println(MRU.getUsername() + (mail1.isRead() ? " read" : " unread"));
                System.out.println("message: " + message.getMail().getSubject() + MRU.getUsername() + " as " + (mail1.isRead() ? "read" : "unread"));
                System.out.println("time: " + TimeString());
                break;
            case ConversationBookmark:
                User CBU = getUser(message.getUser().getUsername());
                Conversation CBMC = CBU.getConversations().get(CBU.getConversations().indexOf(new Conversation(message.getSender())));
                CBMC.setBookMarked(!CBMC.isBookMarked());
                break;
            case ConversationDelete:
                User CDU = getUser(message.getUser().getUsername());
                Conversation CDC = CDU.getConversations().get(CDU.getConversations().indexOf(new Conversation(message.getSender())));
                String username;
                if (CDC.getReceiver() != null)
                    username = CDC.getReceiver().getUsername();
                else
                    username = "receiver";
                System.out.println(CDU.getUsername() + " removemsg " + username);
                System.out.println("time: " + TimeString());
                CDC.setInTrash(true);
                CDU.getConversations().remove(CDC);
                break;
            case ConversationRead:
                User CRU = getUser(message.getUser().getUsername());
                Conversation CRC = CRU.getConversations().get(CRU.getConversations().indexOf(new Conversation(message.getSender())));
                CRC.setRead(!CRC.isRead());
                break;
            case Register:
                String path;
                if (message.getUser().getProfileImage() != null)
                    path = message.getUser().getProfileImage().getAbsolutePath();
                else
                    path = " ";
                System.out.println(message.getUser().getUsername() + " register " + path);
                System.out.println("time: " + TimeString());
                message.getUser().setStreams(outputStream, inputStream);
                ALL_USERS.add(message.getUser());
                break;
            case Downloaded:
                User FDU = getUser(message.getUser().getUsername());
                Conversation FDC = FDU.getConversations().get(FDU.getConversations().indexOf(new Conversation(message.getReceiver())));
                FDC.getMails().get(FDC.getMails().indexOf(message.getMail())).setFileIsDownloaded(true);
                break;
            case Spam:

                User senderBlock = getUser(message.getSender());
                User receiverBlock = getUser(message.getReceiver());
                senderBlock.block(receiverBlock);
                System.out.println(senderBlock.getUsername() + " block " + receiverBlock.getUsername());
                System.out.println("time: " + TimeString());
                break;
            case Unspam:
                User senderUnBlock = getUser(message.getSender());
                User receiverUnBlock = getUser(message.getReceiver());
                senderUnBlock.getBlockedUsers().remove(receiverUnBlock);
                System.out.println(senderUnBlock.getUsername() + " unblock " + receiverUnBlock.getUsername());
                System.out.println("time: " + TimeString());
                break;
            case SaveUsers:
                saveUsers();
                break;
        }
//        DataBase.newList(ALL_USERS);
//        DataBase.reWriteDB_users();
    }

    private String getAddressString(Message message) {
        String absolutePath;
        if (message.getMail().getAttachedFile() != null)
            absolutePath = message.getMail().getAttachedFile().getAbsolutePath();
        else
            absolutePath = " ";
        return absolutePath;
    }

    private User getUser(String username) {
        User o = new User("", "", username, "");
        if (ALL_USERS.contains(o)) {
            return ALL_USERS.get(ALL_USERS.indexOf(o));
        }
        return null;
    }

    private String TimeString() {
        time = LocalDateTime.now();
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm a");
        return dtf.format(time);
    }

    public static void saveUsers() throws IOException {
        File file = new File("Database/users.txt");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(ALL_USERS);
        oos.close();
        fos.close();
    }

    public static void openUsers() throws IOException, ClassNotFoundException {
        File file = new File("Database/users.txt");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ALL_USERS = (ArrayList<User>) ois.readObject();
        fis.close();
        ois.close();
    }

    public static ArrayList<User> getAllUsers() {
        return ALL_USERS;
    }
}
