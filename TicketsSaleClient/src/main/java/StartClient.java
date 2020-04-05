import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import objectprotocol.AppServicesProxy;
import services.IAppServices;

import java.util.Properties;

public class StartClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
        FXMLLoader appLoader = new FXMLLoader(getClass().getResource("/views/appView.fxml"));

        Parent loginP = loginLoader.load();
        Parent appP = appLoader.load();

        LoginCtrl loginCtrl = loginLoader.getController();
        AppCtrl appCtrl = appLoader.getController();


        Properties clientProp = new Properties();

        clientProp.load(StartClient.class.getResourceAsStream("/client.properties"));
        System.out.println("Client properties set ...");
        clientProp.list(System.out);

        String serverIP = clientProp.getProperty("client.server.host");
        int serverPort = Integer.parseInt(clientProp.getProperty("client.server.port"));

        System.out.println("IP: " + serverIP + " ; PORT: " + serverPort);

        IAppServices server = new AppServicesProxy(serverIP,serverPort);


        appCtrl.setServer(server);
        loginCtrl.setServer(server);
        loginCtrl.setController(appCtrl);
        loginCtrl.setParent(appP);


        stage.setScene(new Scene(loginP));
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
