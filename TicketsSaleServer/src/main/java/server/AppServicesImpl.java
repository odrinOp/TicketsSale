package server;

import interfaces.IGameRepo;
import interfaces.ISaleRepo;
import interfaces.IUserRepo;
import model.Game;
import model.Sale;
import model.User;
import services.AppException;
import services.IAppObserver;
import services.IAppServices;
import validators.ValidatorException;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppServicesImpl implements IAppServices {
    private IUserRepo userRepository;
    private IGameRepo gameRepo;
    private ISaleRepo saleRepo;
    private Map<String,IAppObserver> loggedClients;
    private final int defaultNoThreads = 5;

    public AppServicesImpl(IUserRepo userRepository, IGameRepo gameRepo, ISaleRepo saleRepo) {
        this.userRepository = userRepository;
        this.gameRepo = gameRepo;
        this.saleRepo = saleRepo;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, IAppObserver client) throws AppException {
        User userR = userRepository.findBy(user);
        if (userR != null){
            if (loggedClients.get(user.getUsername())!=null)
                throw new AppException("User already logged in!");
            loggedClients.put(user.getUsername(),client);

            return;
        }
        throw new AppException("Authentication Failed!");
    }


    @Override
    public synchronized void logout(User user, IAppObserver client) throws AppException {
        IAppObserver localClient = loggedClients.remove(user.getUsername());
        if (localClient == null)
            throw new AppException("User " + user.getUsername() + " is not logged in.");

    }

    @Override
    public synchronized void makeSale(Sale sale) throws AppException {
        Game gameR = gameRepo.findOne(sale.getGame_id());
        if (gameR == null)
            throw new AppException("The id of the game doesn't exists!");

        if (gameR.getAvailableSeats() < sale.getNo_of_seats())
            throw new AppException("There aren't enough tickets for this match!");

        gameR.setAvailableSeats(gameR.getAvailableSeats() - sale.getNo_of_seats());

        try {
            saleRepo.add(sale);
        } catch (ValidatorException e) {
            throw new AppException("Error validating sale data " + e.getMessage());
        }

        try {
            gameRepo.update(gameR);
        } catch (ValidatorException e) {
            throw new AppException("Error validating game data " + e.getMessage());
        }



        notifyAllLoggedClients();
    }

    @Override
    public Game[] getListOfGames() throws AppException {
        return getGamesArray();
    }


    private void notifyAllLoggedClients(){
        System.out.println("Sale done...\nNotifying about sale ...");

        ExecutorService executor = Executors.newFixedThreadPool(defaultNoThreads);

        for(IAppObserver client : loggedClients.values()){

            executor.execute(()->{
                System.out.println("Notifying client ...");
                try {
                    client.updateGameList(getGamesArray());
                } catch (AppException e) {
                    System.out.println("Error notifying client ...");
                }
            });
        }
        executor.shutdown();

    }

    private Game[] getGamesArray(){
        List<Game> games = gameRepo.findAllSorted();
        Game[] g =games.toArray(new Game[0]);

        return g;
    }

}
