package utils;

import objectprotocol.AppClientWorker;
import services.IAppServices;

import java.net.Socket;

public class AppObjectServer extends AbstractConcurrentServer {

    private IAppServices appServer;
    public AppObjectServer(int port,IAppServices appServer) {
        super(port);
        this.appServer = appServer;
        System.out.println("App -- Object Concurrent Server");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AppClientWorker worker = new AppClientWorker(appServer,client);
        Thread th = new Thread(worker);
        return th;
    }
}
