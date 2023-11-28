package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class controls the application window for the ModifyProduct screen.
 */
public class ModifyProduct implements Initializable {
    public TextField modifyProductMinText;
    public TextField modifyProductNameText;
    public TextField modifyProductIDText;
    public TextField modifyProductInvText;
    public TextField modifyProductPriceText;
    public TextField modifyProductMaxText;
    public TextField modifyProductSearch;
    public TableColumn modifyProductAllPartIDColumn;
    public TableColumn modifyProductAllPartNameColumn;
    public TableColumn modifyProductAllPartInvColumn;
    public TableColumn modifyProductAllPartPriceColumn;
    public Button modifyProductAddButton;
    public Button modifyProductRemoveButton;
    public Button modifyProductSaveButton;
    public Button modifyProductCancelButton;
    public TableColumn modifyProductAssocPartIDColumn;
    public TableColumn modifyProductAssocPartNameColumn;
    public TableColumn modifyProductAssocPartInvColumn;
    public TableColumn modifyProductAssocPartPriceColumn;
    public TableView modifyProductAllPartsTable;
    public TableView modifyProductAssocPartsTable;

    private ObservableList<Part> allParts = Inventory.getAllParts();
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();
    private Product productToModify = null;

    /**
     *The initialize method sets up the ModifyProduct screen on launch.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productToModify = MainScreen.getProductToModify();
        assocParts = productToModify.getAllAssociatedParts();

        modifyProductAllPartsTable.setItems(allParts);
        modifyProductAssocPartsTable.setItems(assocParts);
        modifyProductAllPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductAllPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductAllPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductAllPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        modifyProductAssocPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductAssocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductAssocPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductAssocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProductIDText.setText(String.valueOf(productToModify.getId()));
        modifyProductNameText.setText(productToModify.getName());
        modifyProductInvText.setText(String.valueOf(productToModify.getStock()));
        modifyProductPriceText.setText(String.valueOf(productToModify.getPrice()));
        modifyProductMaxText.setText(String.valueOf(productToModify.getMax()));
        modifyProductMinText.setText(String.valueOf(productToModify.getMin()));
    }

    /**
     *The onModifyProductAddButton is called when the user clicks the add button on the ModifyProduct screen.
     * This method adds the selected part to the modifyProductAssocPartsTable.
     *
     * @param actionEvent the button click on the ModifyProduct screen
     */
    public void onModifyProductAddButton(ActionEvent actionEvent) {
        Part selectedPart = (Part)modifyProductAllPartsTable.getSelectionModel().getSelectedItem();


        if(selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory Management System");
            alert.setHeaderText("No Part Selected!");
            alert.setContentText("Please select part to add.");
            alert.showAndWait();
        }

        assocParts.add(selectedPart);

        modifyProductAssocPartsTable.setItems(assocParts);
    }

    /**
     *The onModifyProductRemoveButton is called when the user clicks the remove associated parts button on the ModifyProduct screen.
     * This method removes the selected part from the modifyProductAssocPartsTable.
     *
     * @param actionEvent the button click on the ModifyProduct screen
     */
    public void onModifyProductRemoveButton(ActionEvent actionEvent) {
        Part selectedPart = (Part) modifyProductAssocPartsTable.getSelectionModel().getSelectedItem();


        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory Management System");
            alert.setHeaderText("No Part Selected!");
            alert.setContentText("Please select part to remove.");
            alert.showAndWait();
        }else {
            Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION, "This will remove the selected Part from this Product!\n\nContinue?\n\n");
            deleteWarning.setTitle("Warning");
            deleteWarning.setHeaderText("Delete Part?");

            Optional<ButtonType> result = deleteWarning.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
            }
        }

        modifyProductAssocPartsTable.setItems(assocParts);
    }

    /**
     *The onModifyProductSaveButton method is called when the user clicks the Save button on the ModifyProduct screen.
     * This method gets the user text inputs and the associated parts list and creates an instance of the Product class. It replaces that instance with the
     * modified instance in the allProducts list in the Inventory class. Alerts are in place to catch errors.
     *
     * @param actionEvent the button click on the ModifyProduct screen
     */
    public void onModifyProductSaveButton(ActionEvent actionEvent) throws IOException {
        try {
            int id = productToModify.getId();
            String name = modifyProductNameText.getText();
            int stock = Integer.parseInt(modifyProductInvText.getText());
            double price = Double.parseDouble(modifyProductPriceText.getText());
            int min = Integer.parseInt(modifyProductMinText.getText());
            int max = Integer.parseInt(modifyProductMaxText.getText());
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

            if (!threwError) {
                Product product = new Product(id, name, price, stock, min, max);

                for (Part part : assocParts) {
                    product.addAssociatedPart(part);
                }

                Inventory.updateProduct(findProductIndex(productToModify), product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Product updated successfully.");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Product Information");
            alert.setContentText("All fields are required.\n\nPlease enter valid product information for each field.\n\n");
            alert.showAndWait();
        }
    }

    /**
     *The onModifyProductCancelButton method is called when the user clicks the Cancel button on the ModifyProduct screen.
     * This method ensures the user wants to cancel, then launches the MainScreen screen of the application.
     *
     * @param actionEvent the button click on the ModifyProduct screen
     */
    public void onModifyProductCancelButton(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inventory Management System");
        alert.setHeaderText("Product has not been saved.");
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
     *The onModifyProductSearch is called when the user presses enter while the cursor is in the search window on the ModifyProduct screen.
     * This method calls the lookupPart method from Inventory and searches for the part based on the user input. Then it places the parts in the tableview.
     *
     * @param actionEvent the enter press on the user keyboard
     */
    public void onModifyProductSearch(ActionEvent actionEvent) {

        String textInput = modifyProductSearch.getText();

        ObservableList<Part> parts = Inventory.lookupPart(textInput);

        if(parts.size() == 0){
            try {
                int id = Integer.parseInt(textInput);
                Part part = Inventory.lookupPart(id);
                if (part != null) {
                    parts.add(part);
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Part ID not found.");
                    alert.setTitle("Inventory Management System");
                    alert.showAndWait();
                }
            }
            catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No parts found with that name.\n\nPart names are case sensitive.");
                alert.setTitle("Inventory Management System");
                alert.showAndWait();
            }
        }
        if (parts.size() != 0) {
            modifyProductAllPartsTable.setItems(parts);
        }
        modifyProductSearch.setText("");
    }

    /**
     *The findProductIndex method finds the index of the product to be modified in the allProducts list in Inventory.
     * This method checks the allProducts list from Inventory and returns the index for the product.
     *
     * @param product the product to search for
     * @return the index for the part in the allParts list in Inventory
     */
    private int findProductIndex(Product product){
        ObservableList<Product> products= Inventory.getAllProducts();

        return products.indexOf(product);
    }
}
