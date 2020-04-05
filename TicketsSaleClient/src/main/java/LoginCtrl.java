import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Game;
import model.User;
import services.AppException;
import services.IAppServices;



public class LoginCtrl {
    private IAppServices server;
    private AppCtrl appCtrl;
    private User user;
    private Parent mainAppParent;

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;


    public LoginCtrl() {
    }


    public void setServer(IAppServices server){
        this.server = server;
    }

    public void setParent(Parent p){
        mainAppParent = p;
    }

    public void setController(AppCtrl controller){
        appCtrl = controller;
    }

    @FXML
    public void loginAction(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();

        user = new User(username,password);

        try{
            server.login(user,appCtrl);
            Game[] games =server.getListOfGames();
            appCtrl.updateGameList(games);
            Stage stage = new Stage();
            stage.setTitle("Tickets Sale Manager : " + user.getUsername());
            stage.setScene(new Scene(mainAppParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    try {
                        server.logout(user,appCtrl);
                        System.exit(0);
                    } catch (AppException e) {
                        Utils.getAlert(Alert.AlertType.INFORMATION,"Error",e.getMessage()).showAndWait();
                    }
                }
            });
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (AppException e) {
            Utils.getAlert(Alert.AlertType.INFORMATION,"Error",e.getMessage()).showAndWait();
        }
    }

}
