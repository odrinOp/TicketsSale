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
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppServicesProxy implements IAppServices {
    private String host;
    private int port;

    private IAppObserver client;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Socket connection;

    private BlockingQueue<Response> qResponses;
    private volatile boolean finished;

    public AppServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;

        qResponses = new LinkedBlockingQueue<>();
    }


    @Override
    public void login(User user, IAppObserver client) throws AppException {
        System.out.println("Client == null : " + (client == null));
        initConnection();
        System.out.println("Proxy: sending login request ...");
        sendRequest(new LoginRequest(user));
        System.out.println("PROXY: getting response ...");
        Response response = readResponse();
        System.out.println(response);
        if (response != null){

            if(response instanceof OkResponse){

                this.client = client;

                System.out.println("PROXY: Client saved...");
                return;
            }

            if(response instanceof ErrorResponse){
                ErrorResponse err = (ErrorResponse) response;
                closeConnection();
                throw new AppException(err.getMessage());
            }
        }

    }

    @Override
    public void logout(User user, IAppObserver client) throws AppException {
            sendRequest(new LogoutRequest(user));
            Response response = readResponse();
            closeConnection();
            if(response != null){

                if(response instanceof OkResponse){

                    return;
                }
                if(response instanceof  ErrorResponse){
                    ErrorResponse err = (ErrorResponse) response;
                    throw new AppException(err.getMessage());
                }
            }
    }

    @Override
    public void makeSale(Sale sale) throws AppException {
        sendRequest(new SaleRequest(sale));
        Response response = readResponse();
        if(response != null){
            if(response instanceof ErrorResponse){
                ErrorResponse err = (ErrorResponse) response;
                throw new AppException(err.getMessage());
            }
        }

    }

    @Override
    public Game[] getListOfGames() throws AppException {
        sendRequest(new GetGamesRequest());
        Response response = readResponse();

        if(response instanceof ErrorResponse) {
                ErrorResponse err = (ErrorResponse) response;
                throw new AppException(err.getMessage());
            }
        GetGamesResponse gameRes = (GetGamesResponse) response;
        return gameRes.getGames();

        }




    private void initConnection() throws AppException {
        try{

            connection = new Socket(host,port);


            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush();

            inputStream = new ObjectInputStream(this.connection.getInputStream());

            finished = false;

            startReader();


        } catch (UnknownHostException e) {
            throw new AppException("The server can't be reached right now!\nSorry and please try later!");
        } catch (IOException e) {
            throw new AppException("The server can't be reached right now!\nSorry and please try later!");
        }
    }

    private void startReader(){
        Thread th = new Thread(new ReaderThread());
        th.start();
    }

    private void closeConnection(){
        finished = true;
        try{
            inputStream.close();
            outputStream.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws AppException {
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            throw new AppException("Error sending response " + e.getMessage() + "...");
        }

    }

    private Response readResponse(){
        Response response = null;
        try {
            response = qResponses.take();
            System.out.println("PROXY -Response taken: " + response + "...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void handleUpdate(UpdateResponse response){
        if (response instanceof UpdateGamesResponse){
            UpdateGamesResponse gamesResponse = (UpdateGamesResponse) response;
            try {
                client.updateGameList(gamesResponse.getGames());

            } catch (AppException e) {
                e.printStackTrace();
            }
        }

    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished){
                try {
                    System.out.println("PROXY- READERTHREAD: Waiting response ...");
                    Object response = inputStream.readObject();
                    System.out.println("PROXY- READERTHREAD : response received: " + response + " ...");
                    if(response instanceof UpdateResponse){
                        handleUpdate((UpdateResponse) response);
                    }else{
                        try {
                            qResponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
