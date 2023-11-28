package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *This class controls the application window for the MainScreen screen.
 */
public class MainScreen implements Initializable{

    public TableView partTable;
    public TableColumn partIDColumn;
    public TableColumn partNameColumn;
    public TableColumn partLevelColumn;
    public TableColumn partPriceColumn;
    public Button partAddButton;
    public Button partModifyButton;
    public Button partDeleteButton;
    public TableView productTable;
    public TableColumn productIDColumn;
    public TableColumn productNameColumn;
    public TableColumn productLevelColumn;
    public TableColumn productPriceColumn;
    public Button productAddButton;
    public Button productModifyButton;
    public Button productDeleteButton;
    public TextField searchParts;
    public TextField searchProducts;
    public Button exitButton;

    private ObservableList<Part> allParts = Inventory.getAllParts();
    private ObservableList<Product> allProducts = Inventory.getAllProducts();

    private static Part partToModify = null;
    public static Part getPartToModify(){return partToModify;}
    private static Product productToModify = null;
    public static Product getProductToModify(){return productToModify;}

    /**
     *The initialize method sets up the MainScreen screen on launch.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partTable.setItems(allParts);
        productTable.setItems(allProducts);
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     *The onPartAddButton launches the AddPart screen.
     * This method sets the stage and scene for the AddPart screen and loads the application window.
     *
     * @param actionEvent the button click on the MainScreen screen
     *
     * <b>RUNTIME ERROR:</b> The add button threw a runtime error load exception, Null pointer exception when clicked.
     * The initialize method in the AddPart class contained code for changing a label that did not exist. This label was created
     * during the early process of creating the program and was later deleted. The error was corrected by removing the invalid code for
     * changing the label.
     */
    public void onPartAddButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *The onPartModifyButton method launches the ModifyPart screen.
     * This method sets the stage and scene for the ModifyPart screen and loads the application window. It also sets the partToModify for the ModifyPart screen.
     *
     * @param actionEvent the button click on the MainScreen screen
     */
    public void onPartModifyButton(ActionEvent actionEvent) throws IOException {
        partToModify = (Part)partTable.getSelectionModel().getSelectedItem();

        if(partToModify != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory Management System");
            alert.setHeaderText("No Part Selected!");
            alert.setContentText("Please select part to modify.");
            alert.showAndWait();
        }
    }

    /**
     *The onPartDeleteButton is called when the user clicks the delete button on the MainScreen screen.
     * This method confirms the user wants to delete the selected part and then removes is from the allParts list in Inventory.
     *
     * @param actionEvent the button click on the MainScreen screen
     */
    public void onPartDeleteButton(ActionEvent actionEvent) {
        Part selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Part Selected!");
            alert.setContentText("Please select part to delete.");
            alert.showAndWait();
        }else {

            Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION, "This will remove the selected Part from Inventory!\n\nContinue?\n\n");
            deleteWarning.setTitle("Warning");
            deleteWarning.setHeaderText("Delete Part?");

            Optional<ButtonType> result = deleteWarning.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
                partTable.setItems(allParts);
            }
        }
    }

    /**
     *The onProductAddButton method launches the AddProduct screen.
     * This method sets the stage and scene for the AddProduct screen and loads the application window.
     *
     * @param actionEvent the button click on the MainScreen screen
     */
    public void onProductAddButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *The onPartModifyButton method launches the ModifyProduct screen.
     * This method sets the stage and scene for the ModifyProduct screen and loads the application window. It also sets the productToModify for the ModifyProduct screen.
     *
     * @param actionEvent the button click on the MainScreen screen
     */
    public void onProductModifyButton(ActionEvent actionEvent) throws IOException {
        productToModify = (Product) productTable.getSelectionModel().getSelectedItem();

        if (productToModify != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory Management System");
            alert.setHeaderText("No Product Selected!");
            alert.setContentText("Please select product to modify.");
            alert.showAndWait();
        }
    }

    /**
     *The onProductDeleteButton is called when the user clicks the delete button on the MainScreen screen.
     * This method confirms the user wants to delete the selected product and then removes is from the allProducts list in Inventory.
     *
     * @param actionEvent the button click on the MainScreen screen
     */
    public void onProductDeleteButton(ActionEvent actionEvent) {
        Product selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();

        if(selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory Management System");
            alert.setHeaderText("No Product Selected!");
            alert.setContentText("Please select product to delete.");
            alert.showAndWait();
        }else if (selectedProduct.getAllAssociatedParts().size() != 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Cannot delete product!");
            alert.setContentText("This product has associated parts. Associated parts must be removed before deleting product.");
            alert.showAndWait();
        }else {
            Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION, "This will remove the selected Product from Inventory!\n\nContinue?\n\n");
            deleteWarning.setTitle("Warning");
            deleteWarning.setHeaderText("Delete Product?");

            Optional<ButtonType> result = deleteWarning.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
                productTable.setItems(allProducts);
            }
        }
    }

    /**
     *The onExitButton is called when the user clicks the exit button on the MainScreen screen.
     * This method confirms the user would like to quit and then closes the application.
     *
     * @param actionEvent the button click on the MainScreen screen
     */
    public void onExitButton(ActionEvent actionEvent) {
        Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
        deleteWarning.setTitle("Inventory Management System");
        deleteWarning.setHeaderText("Close Inventory Management System?");

        Optional<ButtonType> result = deleteWarning.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    /**
     *The onSearchParts is called when the user presses enter while the cursor is in the search window on the MainScreen screen.
     * This method calls the lookupPart method from Inventory and searches for the part based on the user input. Then it places the parts in the tableview.
     *
     * @param actionEvent the enter press on the user keyboard
     */
    public void onSearchParts(ActionEvent actionEvent) {

        String textInput = searchParts.getText();

        ObservableList<Part> parts = Inventory.lookupPart(textInput);

        try {
            if(parts.size() == 0) {
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
        }catch (RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No parts found with that name.\n\nPart names are case sensitive.");
            alert.setTitle("Inventory Management System");
            alert.showAndWait();
        }
        if (parts.size() != 0) {
            partTable.setItems(parts);
        }
        searchParts.setText("");
    }

    /**
     *The onSearchProducts is called when the user presses enter while the cursor is in the search window on the MainScreen screen.
     * This method calls the lookupProduct method from Inventory and searches for the product based on the user input. Then it places the products in the tableview.
     *
     * @param actionEvent the enter press on the user keyboard
     */
    public void onSearchProducts(ActionEvent actionEvent) {

        String textInput = searchProducts.getText();

        ObservableList<Product> products = Inventory.lookupProduct(textInput);

        try {
            if (products.size() == 0) {

                int id = Integer.parseInt(textInput);
                Product product = Inventory.lookupProduct(id);
                if (product != null) {
                    products.add(product);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product ID not found.");
                    alert.setTitle("Inventory Management System");
                    alert.showAndWait();
                }
            }
        }catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No products found with that name.\n\nProduct names are case sensitive.");
                alert.setTitle("Inventory Management System");
                alert.showAndWait();
        }
        if (products.size() != 0) {
            productTable.setItems(products);
        }
        searchProducts.setText("");
    }
}

