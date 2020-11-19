package Model.Connection;

import Model.User;
import Server.Server;
import Model.Messages.Message;
import Model.Messages.MessageType;

import java.io.*;
import java.net.Socket;

public class Connection {


    private String currentUsername;
    private User currentUser;
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isClient;

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public Connection(User currentUser) {
        this.currentUser = currentUser;
        this.currentUsername = currentUser.getUsername();
        try {
            client = new Socket(Server.serverIP, Server.requestPort);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            Message request = new Message(MessageType.Connect, currentUsername, "", "");
            request.setUser(currentUser);
            sendRequest(request);
        } catch (IOException e) {
            throw new ServerConnectionException();
        }
    }

    public Connection(User currentUser, boolean isClient) {
        this.currentUser = currentUser;
        this.currentUsername = currentUser.getUsername();
        this.isClient = isClient;
        try {
            client = new Socket(Server.serverIP, Server.requestPort);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            Message request = new Message(isClient ? MessageType.Register : MessageType.Connect, currentUsername, "", "");
            request.setUser(currentUser);
            sendRequest(request);
        } catch (IOException e) {
            throw new ServerConnectionException();
        }
    }

    public void disconnect() {
        sendRequest(new Message(MessageType.Disconnect, currentUsername, "", ""));
    }

    public void initializeServices() {
        Thread listenerThread = new Thread(new ListenerService(this), "Listener Thread");
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendRequest(Message request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            throw new ServerConnectionException();
        }
    }

    public void sendText(String textMessage, String receiver) {
        sendRequest(new Message(MessageType.Text, currentUsername, receiver, textMessage));
    }

    public String getRespond() {
        try {
            return ClientMessageHandler.handle((Message) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new ServerConnectionException();
        }
    }
}

