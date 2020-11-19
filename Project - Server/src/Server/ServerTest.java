package Server;

import Model.Connection.Connection;
import Model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerTest {
    private User hamid, ali, reza;
    private Connection hamidC, aliC, rezaC;

    @Before
    public void init() {
        hamid = new User("a", "saffari", "a", "1");
        ali = new User("b", "saffari", "b", "2");
        reza = new User("s", "saffari", "s", "3");
    }

    @Test
    public void test() throws InterruptedException {
        Server.start();
        Thread.sleep(100);
        hamidC = new Connection(hamid,true);
        aliC = new Connection(ali,true);
        rezaC = new Connection(reza,true);
        assertEquals(3,ServerHandler.getAllUsers().size());
        assertEquals(ServerHandler.getAllUsers().get(0),hamid);
        assertEquals(ServerHandler.getAllUsers().get(1),ali);
        assertEquals(ServerHandler.getAllUsers().get(2),reza);
        Connection fake = new Connection(hamid);
        assertEquals(3,ServerHandler.getAllUsers().size());
        assertEquals(ServerHandler.getAllUsers().get(0).getFirstName(),hamid.getFirstName());
        assertEquals(ServerHandler.getAllUsers().get(1).getFirstName(),ali.getFirstName());
        assertEquals(ServerHandler.getAllUsers().get(2).getFirstName(),reza.getFirstName());
    }

}