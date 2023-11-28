package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Inventory;

/**
 *The Main class loads the application and sets the initial stage and scene. It provides the start and main methods to load MainScreen.fxml and launch the application.
 *
 * @author Allison McGillen ID:001403090
 *
 * <p><b>FUTURE ENHANCEMENT:</b> A login system that would prevent unauthorized users from changing the inventory. This would replace the main screen with a login page that
 * would verify a user's username and password. The main screen could then be accessed by the verified user. Potentially a second login screen could further
 * restrict access to the add and modify screens for both parts and products. This would allow for low access users to search through parts and products
 * on the main screen without giving acces to modify those parts and products or add new parts and products.</p>
 *
 * <p><b>RUNTIME ERROR:</b> The runtime error occured in the onPartAddButton method in the MainScreen class. See that method for details.</p>
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root, 1000, 450));
        stage.show();
    }

    //TODO: All comments

    /**
     *This method launches the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args){

        launch(args);

    }
}
