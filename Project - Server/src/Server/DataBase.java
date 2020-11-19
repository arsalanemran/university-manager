package Server;

import Model.User;

import java.io.*;
import java.util.ArrayList;

public class DataBase {
    private static ArrayList<User> usersArrayList = new ArrayList<>();

    public final static String DB_PATH = "Database/users.txt";

    private static FileOutputStream fos;
    private static FileInputStream fis;

    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    static {
        try {
//            usersArrayList = reReadDB_users();
            readDB();
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void newList(ArrayList<User> users) {
        usersArrayList = users;
    }

    public static void reWriteDB_users() throws IOException {
        fos = new FileOutputStream(DB_PATH);
        oos = new ObjectOutputStream(fos);
        oos.writeObject(usersArrayList);
        fos.close();
        oos.close();
    }

    public static void readDB() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(fis);
        fis = new FileInputStream(DB_PATH);
        usersArrayList = (ArrayList<User>) ois.readObject();
        fis.close();
        ois.close();
    }

    public static ArrayList<User> reReadDB_users() throws IOException, ClassNotFoundException {
        return (ArrayList<User>) ois.readObject();
    }
}
