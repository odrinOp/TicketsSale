import javafx.scene.control.Alert;

public class Utils {

    public static Alert getAlert(Alert.AlertType type,String title,String text){
        Alert a  = new Alert(type);
        a.setTitle(title);
        a.setContentText(text);
        return a;
    }
}
