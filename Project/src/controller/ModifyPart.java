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
 *This class controls the application window for the ModifyPart screen.
 */
public class ModifyPart implements Initializable {
    public Button modifyPartSaveButton;
    public Button modifyPartCancelButton;
    public RadioButton modifyPartInHouseRadial;
    public ToggleGroup partLocation;
    public RadioButton modifyPartOutsourcedRadial;
    public Label modifyPartMachineCompanyLabel;
    public TextField modifyPartIdText;
    public TextField modifyPartNameText;
    public TextField modifyPartInvText;
    public TextField modifyPartPriceText;
    public TextField modifyPartMaxText;
    public TextField modifyPartMinText;
    public TextField modifyPartMachineCompanyText;

    private Part partToModify = null;

    /**
     *The initialize method sets up the ModifyPart screen on launch.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partToModify = MainScreen.getPartToModify();

        modifyPartIdText.setText(String.valueOf(partToModify.getId()));
        modifyPartNameText.setText(partToModify.getName());
        modifyPartInvText.setText(String.valueOf(partToModify.getStock()));
        modifyPartMaxText.setText(String.valueOf(partToModify.getMax()));
        modifyPartMinText.setText(String.valueOf(partToModify.getMin()));
        modifyPartPriceText.setText(String.valueOf(partToModify.getPrice()));

        if(partToModify instanceof InHouse){
            modifyPartMachineCompanyText.setText(String.valueOf(((InHouse) partToModify).getMachineId()));
            modifyPartInHouseRadial.setSelected(true);
        }else{
            modifyPartMachineCompanyText.setText(((OutSourced) partToModify).getCompanyName());
            modifyPartOutsourcedRadial.setSelected(true);
            modifyPartMachineCompanyLabel.setText("Company Name");
        }
    }

    /**
     *The onSaveButton method is called when the user clicks the Save button on the ModifyPart screen.
     * This method gets the user text inputs and creates an instance of the Part class. It replaces that instance with the
     * modified instance in the allParts list in the Inventory class. Alerts are in place to catch errors.
     *
     * @param actionEvent the button click on the ModifyPart screen
     */
    public void onSaveButton(ActionEvent actionEvent) {
        try {
            int id = partToModify.getId();
            String name = modifyPartNameText.getText();
            int stock = Integer.parseInt(modifyPartInvText.getText());
            double price = Double.parseDouble(modifyPartPriceText.getText());
            int min = Integer.parseInt(modifyPartMinText.getText());
            int max = Integer.parseInt(modifyPartMaxText.getText());
            boolean threwError = false;

            if (min > max){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Min cannot be higher than Max.");
                alert.showAndWait();
                threwError = true;
            }else if(stock > max || stock < min){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Stock is outside allowed min/max range.");
                alert.showAndWait();
                threwError = true;
            }
            if(!threwError) {
                if (modifyPartInHouseRadial.isSelected()) {
                    int machineId = Integer.parseInt(modifyPartMachineCompanyText.getText());
                    InHouse part = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.updatePart(findPartIndex(partToModify), part);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Part updated successfully.");
                    alert.showAndWait();
                    Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("Inventory Management System");
                    stage.setScene(scene);
                    stage.show();
                } else if (modifyPartOutsourcedRadial.isSelected()) {
                    String companyName = modifyPartMachineCompanyText.getText();
                    OutSourced part = new OutSourced(id, name, price, stock, min, max, companyName);
                    Inventory.updatePart(findPartIndex(partToModify), part);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Part updated successfully.");
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
        }catch (RuntimeException | IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Part Information");
                alert.setContentText("All fields are required.\n\nPlease enter valid part information for each field.\n\n");
                alert.showAndWait();
            }
    }

    /**
     *The onCancelButton method is called when the user clicks the Cancel button on the ModifyPart screen.
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
     *The onInHouseRadial method is called when the user selects the In-House radial on the ModifyPart screen.
     * This method changes the text on the modifyPartMachineCompanyLabel.
     *
     * @param actionEvent the selection click on the ModifyPart screen
     */
    public void onInHouseRadial(ActionEvent actionEvent) {
        modifyPartMachineCompanyLabel.setText("Machine ID");
    }

    /**
     *The onOutsourcedRadial method is called when the user selects the Outsourced radial on the ModifyPart screen.
     * This method changes the text on the modifyPartMachineCompanyLabel.
     *
     * @param actionEvent the selection click on the ModifyPart screen
     */
    public void onOutsourcedRadial(ActionEvent actionEvent) {
        modifyPartMachineCompanyLabel.setText("Company Name");
    }

    /**
     *The findPartIndex method finds the index of the part to be modified in the allParts list in Inventory.
     * This method checks the allParts list from Inventory and returns the index for the part.
     *
     * @param part the part to search for
     * @return the index for the part in the allParts list in Inventory
     */
    private int findPartIndex(Part part){
        ObservableList<Part> parts= Inventory.getAllParts();

        return parts.indexOf(part);
    }
}
