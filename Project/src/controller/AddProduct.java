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
 *This class controls the application window for the AddProduct screen.
 */
public class AddProduct implements Initializable {
    public TextField addProductMinText;
    public TextField addProductNameText;
    public TextField addProductIDText;
    public TextField addProductInvText;
    public TextField addProductPriceText;
    public TextField addProductMaxText;
    public TextField addProductSearchText;
    public TableColumn addProductAllPartsPartIDColumn;
    public TableColumn addProductAllPartsNameColumn;
    public TableColumn addProductAllPartsInvColumn;
    public TableColumn addProductAllPartsPriceColumn;
    public Button addProductAddButton;
    public Button addProductRemoveButton;
    public Button addProductSaveButton;
    public Button addProductCancelButton;
    public TableColumn addProductAssocPartsPartIDColumn;
    public TableColumn addProductAssocPartsNameColumn;
    public TableColumn addProductAssocPartsInvColumn;
    public TableColumn addProductAssocPartsPriceColumn;
    public TableView addProductAllPartsTable;
    public TableView addProductAssocPartsTable;

    private ObservableList<Part> allParts = Inventory.getAllParts();
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    /**
     *The initialize method sets up the AddProduct screen on launch.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addProductAllPartsTable.setItems(Inventory.getAllParts());
        addProductAllPartsPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAllPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAllPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAllPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductAssocPartsPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAssocPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAssocPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAssocPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     *The onAddProductAddButton is called when the user clicks the add button on the AddProduct screen.
     * This method adds the selected part to the addProductAssocPartsTable.
     *
     * @param actionEvent the button click on the AddProduct screen
     */
    public void onAddProductAddButton(ActionEvent actionEvent) {
        Part selectedPart = (Part)addProductAllPartsTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory Management System");
            alert.setHeaderText("No Part Selected!");
            alert.setContentText("Please select part to add.");
            alert.showAndWait();
        }

        assocParts.add(selectedPart);

        addProductAssocPartsTable.setItems(assocParts);
    }

    /**
     *The onAddProductRemoveButton is called when the user clicks the remove associated parts button on the AddProduct screen.
     * This method removes the selected part from the addProductAssocPartsTable.
     *
     * @param actionEvent the button click on the AddProduct screen
     */
    public void onAddProductRemoveButton(ActionEvent actionEvent) {
        Part selectedPart = (Part) addProductAssocPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null){
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

        addProductAssocPartsTable.setItems(assocParts);
    }

    /**
     *The onAddProductSaveButton method is called when the user clicks the Save button on the AddProduct screen.
     * This method gets the user text inputs and the associated parts list and creates an instance of the Product class. It adds that instance to the
     * allProducts list in the Inventory class. Alerts are in place to catch errors.
     *
     * @param actionEvent the button click on the AddPart screen
     */
    public void onAddProductSaveButton(ActionEvent actionEvent) {
        try {
            int id = getProductID();
            String name = addProductNameText.getText();
            int stock = Integer.parseInt(addProductInvText.getText());
            double price = Double.parseDouble(addProductPriceText.getText());
            int min = Integer.parseInt(addProductMinText.getText());
            int max = Integer.parseInt(addProductMaxText.getText());
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
                Product product = new Product(id, name, price, stock, min, max);

                for (Part part : assocParts) {
                    product.addAssociatedPart(part);
                }

                Inventory.addProduct(product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Product added successfully.");
                alert.showAndWait();
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();
            }
        }catch (RuntimeException | IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Product Information");
            alert.setContentText("All fields are required.\n\nPlease enter valid product information for each field.\n\n");
            alert.showAndWait();
        }
    }

    /**
     *The onAddProductCancelButton method is called when the user clicks the Cancel button on the AddProduct screen.
     * This method ensures the user wants to cancel, then launches the MainScreen screen of the application.
     *
     * @param actionEvent the button click on the AddProduct screen
     */
    public void onAddProductCancelButton(ActionEvent actionEvent) throws IOException {
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
     *The onAddProductSearchText is called when the user presses enter while the cursor is in the search window on the AddProduct screen.
     * This method calls the lookupPart method from Inventory and searches for the part based on the user input. Then it places the parts in the tableview.
     *
     * @param actionEvent the enter press on the user keyboard
     */
    public void onAddProductSearchText(ActionEvent actionEvent) {
        String textInput = addProductSearchText.getText();

        ObservableList<Part> parts = Inventory.lookupPart(textInput);
        try {
            if (parts.size() == 0) {

                int id = Integer.parseInt(textInput);
                Part part = Inventory.lookupPart(id);
                if (part != null) {
                    parts.add(part);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Part ID not found.");
                    alert.setTitle("Inventory Management System");
                    alert.showAndWait();
                }
            }
        }catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No parts found with that name.\n\nPart names are case sensitive.");
            alert.setTitle("Inventory Management System");
            alert.showAndWait();
        }

        if (parts.size() != 0) {
            addProductAllPartsTable.setItems(parts);
        }
        addProductSearchText.setText("");
    }

    /**
     *The getProductID method sets a unique ID for the new product.
     * This method checks the allProducts list from Inventory and sets a unique ID to be used for the new product.
     *
     * @return the unique product ID
     */
    private int getProductID() {
        int productID;
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        if (allProducts.size() != 0) {
            Product product = allProducts.get(allProducts.size() - 1);
            productID = product.getId() + 1;
        } else productID = 1000;

        return productID;
    }

}
