package objectprotocol;

import model.Game;
import model.Sale;
import model.User;
import services.AppException;
import services.IAppObserver;
import services.IAppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AppClientWorker implements Runnable, IAppObserver {

    private IAppServices server;
    private Socket connection;


    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private volatile boolean connected;

    public AppClientWorker(IAppServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected){
            try{
                Object request = inputStream.readObject();
                Object response = handleRequest((Request) request);
                if (response != null)
                    sendResponse((Response) response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request){

        if(request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq = (LoginRequest) request;
            User user = logReq.getUser();
            try{
                server.login(user,this);
                return new OkResponse();
            } catch (AppException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof  LogoutRequest){
            System.out.println("Logout request ...");
            LogoutRequest logReq = (LogoutRequest) request;
            User user = logReq.getUser();
            try{
                server.logout(user,this);
                connected = false;
                return new OkResponse();
            } catch (AppException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof SaleRequest){
            System.out.println("Make sale request ...");
            SaleRequest saleReq = (SaleRequest) request;
            Sale sale = saleReq.getSale();

            try {
                server.makeSale(sale);
                return new OkResponse();
            } catch (AppException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetGamesRequest){
            System.out.println("Getting the list of games request ...");

            try {
                Game[] games = server.getListOfGames();
                return new GetGamesResponse(games);
            } catch (AppException e) {
                return new ErrorResponse(e.getMessage());
            }

        }
        return null;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("send response: " + response + " ...");
        outputStream.writeObject(response);
        outputStream.flush();;
    }


    @Override
    public void updateGameList(Game[] games) throws AppException {
        System.out.println("Updating game list ...");
        UpdateGamesResponse response = new UpdateGamesResponse(games);
        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new AppException(e.getMessage());
        }
    }
}
