package utils;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer{
    public AbstractConcurrentServer(int port) {
        super(port);
        System.out.println("Abstract Concurrent Server");
    }

    @Override
    protected void processRequest(Socket client) {
        Thread th = createWorker(client);
        th.start();
    }

    protected abstract Thread createWorker(Socket client);
}
