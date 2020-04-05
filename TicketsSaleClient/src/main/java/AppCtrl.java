import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Game;
import model.Sale;
import model.User;
import services.AppException;
import services.IAppObserver;
import services.IAppServices;

public class AppCtrl implements IAppObserver {

    @FXML
    private TableView<Game> gameList;

    @FXML
    private TextField name;

    @FXML
    private TextField tickets;

    private IAppServices server;

    private Game selectedGame;





    public AppCtrl() {
    }

    public void setServer(IAppServices server){
        this.server = server;
    }

    @FXML
    public void clearAction(){
        name.setText("");
        tickets.setText("");
    }
    @FXML
    public void makeSaleAction(){
        String client_name = name.getText();
        int tickets_numb = -1;
        try{
            tickets_numb = Integer.parseInt(tickets.getText());
        }catch (NumberFormatException e){

                Utils.getAlert(Alert.AlertType.ERROR, "Error", "You need to introduce a number in 'Number of tickets' field!").showAndWait();
                return;
        }

        Game g = gameList.getSelectionModel().getSelectedItem();
        if(g == null){
            Utils.getAlert(Alert.AlertType.INFORMATION,"Error","Please select a game first!").showAndWait();
            return;
        }
        int game_id = g.getId();

        Sale s = new Sale(game_id,client_name,tickets_numb);

        try {
            server.makeSale(s);
        } catch (AppException e) {
            Utils.getAlert(Alert.AlertType.INFORMATION,"Eroare",e.getMessage());
        }

    }

    @FXML
    public void updateSelectedGame(){
        selectedGame = gameList.getSelectionModel().getSelectedItem();
    }



    @Override
    public void updateGameList(Game[] games) throws AppException {
        gameList.getItems().clear();
        for(int i = 0; i< games.length; i++)
            gameList.getItems().add(games[i]);
    }


}
