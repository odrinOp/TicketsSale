import interfaces.IGameRepo;
import interfaces.ISaleRepo;
import interfaces.IUserRepo;
import repositories.GameRepoJDBC;
import repositories.SaleRepoJDBC;
import repositories.UserRepoJDBC;
import server.AppServicesImpl;
import services.IAppServices;
import utils.AbstractServer;
import utils.AppObjectServer;
import utils.ServerException;
import validators.GameValidator;
import validators.SaleValidator;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    public static void main(String[] args) {
        int port=-1;
        Properties serverProps = new Properties();

        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set...");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
        }

        GameValidator gameValidator = new GameValidator();
        SaleValidator saleValidator = new SaleValidator();

        IUserRepo userRepo = new UserRepoJDBC(serverProps);
        IGameRepo gameRepo = new GameRepoJDBC(serverProps,gameValidator);
        ISaleRepo saleRepo = new SaleRepoJDBC(serverProps,saleValidator);

        IAppServices servicesImpl = new AppServicesImpl(userRepo,gameRepo,saleRepo);
        try {
            port = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException e){
            System.err.println("Wrong port number..." + e );
            return;
        }

        AbstractServer server = new AppObjectServer(port,servicesImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting server ..." + e.getMessage());
        }

    }
}
