package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *This class controls the application window for the AddPart screen.
 */
public class AddPart implements Initializable {
    public Button addPartSaveButton;
    public Button addPartCancelButton;
    public RadioButton addPartInHouseRadial;
    public RadioButton addPartOutsourcedRadial;
    public Label addPartMachineCompanyLabel;
    public TextField addPartIdText;
    public TextField addPartNameText;
    public TextField addPartInvText;
    public TextField addPartPriceText;
    public TextField addPartMaxText;
    public TextField addPartMinText;
    public TextField addPartMachineCompanyText;
    public ToggleGroup partLocation;


    /**
     *The initialize method sets up the AddPart screen on launch.
     */
 @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     *The onSaveButton method is called when the user clicks the Save button on the AddPart screen.
     * This method gets the user text inputs and creates an instance of the Part class. It adds that instance to the allParts list in the Inventory class.
     *
     * @param actionEvent the button click on the AddPart screen
     */
    public void onSaveButton(ActionEvent actionEvent) {
        try {

            int id = getPartID();
            String name = addPartNameText.getText();
            int stock = Integer.parseInt(addPartInvText.getText());
            double price = Double.parseDouble(addPartPriceText.getText());
            int min = Integer.parseInt(addPartMinText.getText());
            int max = Integer.parseInt(addPartMaxText.getText());
            boolean threwError = false;

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Min cannot be higher than Max.");
                alert.showAndWait();
                threwError = true;
            }else if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Stock is outside allowed min/max range.");
                alert.showAndWait();
                threwError = true;
            }
            if (!threwError) {
                if (addPartInHouseRadial.isSelected()) {
                    int machineId = Integer.parseInt(addPartMachineCompanyText.getText());
                    InHouse part = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.addPart(part);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Part added successfully.");
                    alert.showAndWait();
                    Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("Inventory Management System");
                    stage.setScene(scene);
                    stage.show();
                } else if (addPartOutsourcedRadial.isSelected()) {
                    String companyName = addPartMachineCompanyText.getText();
                    OutSourced part = new OutSourced(id, name, price, stock, min, max, companyName);
                    Inventory.addPart(part);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Part added successfully.");
                    alert.showAndWait();
                    Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("Inventory Management System");
                    stage.setScene(scene);
                    stage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select if part is OUTSOURCED or IN-HOUSE.");
                    alert.setHeaderText("No vendor type selected!");
                    alert.showAndWait();
                }
            }
        }
        catch(RuntimeException | IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Part Information");
                alert.setContentText("All fields are required.\n\nPlease enter valid part information for each field.\n\n");
                alert.showAndWait();
            }
    }

    /**
     *The onCancelButton method is called when the user clicks the Cancel button on the AddPart screen.
     * This method ensures the user wants to cancel, then launches the MainScreen screen of the application.
     *
     * @param actionEvent the button click on the AddPart screen
     */
    public void onCancelButton(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inventory Management System");
        alert.setHeaderText("Part has not been saved.");
        alert.setContentText("Are you sure you want to cancel?\n\n");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     *The onInHouseRadial method is called when the user selects the In-House radial on the AddPart screen.
     * This method changes the text on the addPartMachineCompanyLabel.
     *
     * @param actionEvent the selection click on the AddPart screen
     */
    public void onInHouseRadial(ActionEvent actionEvent) {
        addPartMachineCompanyLabel.setText("Machine ID");
    }

    /**
     *The onOutsourcedRadial method is called when the user selects the Outsourced radial on the AddPart screen.
     * This method changes the text on the addPartMachineCompanyLabel.
     *
     * @param actionEvent the selection click on the AddPart screen
     */
    public void onOutsourcedRadial(ActionEvent actionEvent) {
        addPartMachineCompanyLabel.setText("Company Name");
    }

    /**
     *The getPartID method sets a unique ID for the new part.
     * This method checks the allParts list from Inventory and sets a unique ID to be used for the new part.
     *
     * @return partID the unique part ID
     */
    private int getPartID() {
        int partID;
        ObservableList<Part> allParts = Inventory.getAllParts();

        if (allParts.size() != 0) {
            Part part = allParts.get(allParts.size() - 1);
            partID = part.getId() + 1;
        } else partID = 1;

        return partID;
    }
}
