package farma;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NespravneVyplnanieController {

    @FXML
    private Button okButton;

    @FXML
    void initialize() {
        okButton.setOnAction(eh -> {
            okButton.getScene().getWindow().hide();
        });
    }

}
