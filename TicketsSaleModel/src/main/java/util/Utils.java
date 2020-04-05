package util;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import sun.nio.ch.Util;


public class Utils {

    public static Logger logger = (Logger) LogManager.getLogger(Util.class.getName());
    public static Alert createMessageAlert(Stage owner, Alert.AlertType type, String header,String text){
        Alert alert = new Alert(type);
        alert.initOwner(owner);
        alert.setTitle(header);
        alert.setContentText(text);
        return alert;
    }
}
