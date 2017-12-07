
package farma;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;


public class StrojeEditSceneController {
    private StrojFxModel aktualnyStroj;
    private StrojDao strojDao = DaoFactory.INSTANCE.getStrojDao();;
    
    public StrojeEditSceneController(){
        aktualnyStroj = new StrojFxModel();
        aktualnyStroj.setDatum(LocalDateTime.now());
    }
    
    @FXML
    private TextField vyrobcaTextField;

    @FXML
    private TextField typTextField;

    @FXML
    private TextField kategoriaTextField;

    @FXML
    private TextField datumTextField;

    @FXML
    private TextField cenaTextField;

    @FXML
    private Button vlozitButton;

    @FXML
    void initialize() {
         
         vyrobcaTextField.textProperty().bindBidirectional(aktualnyStroj.vyrobcaProperty());
         typTextField.textProperty().bindBidirectional(aktualnyStroj.typProperty());
         kategoriaTextField.textProperty().bindBidirectional(aktualnyStroj.kategoriaProperty());
         
         StringConverter<Number> converter = new NumberStringConverter();
         cenaTextField.textProperty().bindBidirectional(aktualnyStroj.cenaProperty(), converter);
         
         datumTextField.textProperty().bindBidirectional(aktualnyStroj.datumProperty(),
                new StringConverter<LocalDateTime>() {

            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy H:m");

            @Override
            public String toString(LocalDateTime t) {
                return formatter.format(t);
            }

            @Override
            public LocalDateTime fromString(String string) {
                try {
                    datumTextField.setStyle("-fx-background-color: white;");
                    return LocalDateTime.parse(string, formatter);
                } catch (DateTimeParseException e) {
                    datumTextField.setStyle("-fx-background-color: lightcoral;");
                    return null;
                }
            }
        });
        
         vlozitButton.setOnAction(eh ->{
             try{
            strojDao.add(aktualnyStroj.getStroj());
            vyrobcaTextField.clear();
            kategoriaTextField.clear();
            typTextField.clear();
            cenaTextField.clear();
            datumTextField.clear();
           } catch (Exception e) {
                    System.err.println(e);

                    ZvieraNespravneVyplnanieController controller = new ZvieraNespravneVyplnanieController();
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("NespravneVyplnenie.fxml"));
                        loader.setController(controller);
                        Parent parentPane = loader.load();
                        Scene scene = new Scene(parentPane);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("Nesprávne vyplnenie údajov");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                        // toto sa vykona az po zatvoreni okna
                    } catch (IOException iOException) {
                        iOException.printStackTrace();
                    }

                }
            
         });
    }
}