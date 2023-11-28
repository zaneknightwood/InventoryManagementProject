package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *The Inventory class defines an instance of Inventory.
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *The addPart method adds parts to the static allParts list in Inventory.
     *
     * @param part the part to be added
     */
    public static void addPart(Part part){
        allParts.add(part);
    }

    /**
     *The addProduct method adds products to the static allProducts list in Inventory.
     *
     * @param product the product to be added
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /**
     *The lookupPart method searches the allParts list for a part.
     *
     * @param partId ID of the the part to be found
     * @return the found part or null if no part found
     */
    public static Part lookupPart(int partId){

        for(Part part : allParts){
            if (part.getId() == partId) return part;
        }

        return null;
    }

    /**
     *The lookupProduct method searches the allProducts list for a product.
     *
     * @param productId the ID of the product to be found
     * @return the found product or null if no product found
     */
    public static Product lookupProduct(int productId){

        for(Product product : allProducts){
            if (product.getId() == productId) return product;
        }

        return null;
    }

    /**
     *The lookupPart method searches the allParts list for a part.
     *
     * @param partName the partial name of the part to be found
     * @return the found parts as a list or null if no part found
     */
    public static ObservableList<Part> lookupPart(String partName){
      ObservableList<Part> namedParts = FXCollections.observableArrayList();

      for(Part part : allParts){
          if (part.getName().contains(partName)) namedParts.add(part);
      }

      return namedParts;
    }

    /**
     *The lookupProduct method searches the allProducts list for a product.
     *
     * @param productName the partial name of the product to be found
     * @return the found products as a list or null if no product found
     */
    public static ObservableList<Product> lookupProduct(String productName){
       ObservableList<Product> namedProducts = FXCollections.observableArrayList();

       for(Product product : allProducts){
           if (product.getName().contains(productName)) namedProducts.add(product);
       }

       return namedProducts;
    }

    /**
     *The updatePart method searches the AllParts list and replaces the found part with a new part.
     *
     * @param index the index of the part to be replaced
     * @param selectedPart the new part that will replace the current part
     */
    public static void updatePart(int index, Part selectedPart){
        for(Part part : allParts){
            if (index == allParts.indexOf(part)){
                allParts.set(index, selectedPart);
            }
        }
    }

    /**
     *The updateProduct method searches the AllParts list and replaces the found product with a new product.
     *
     * @param index the index of the product to be replaced
     * @param selectedProduct the new product that will replace the current product
     */
    public static void updateProduct(int index, Product selectedProduct){
        for(Product product : allProducts){
            if (index == allProducts.indexOf(product)){
                allProducts.set(index, selectedProduct);
            }
        }
    }

    /**
     *The addPart method removes parts from the static allParts list in Inventory.
     *
     * @param selectedPart the part to be removed
     * @return true
     */
    public static boolean deletePart(Part selectedPart){
        allParts.remove(selectedPart);
        return true;
    }

    /**
     *The deleteProduct method removes products from the static allProducts list in Inventory.
     *
     * @param selectedProduct the products to be removed
     * @return true
     */
    public static boolean deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * @return the list of all parts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * @return the list of all products
     */
    public static ObservableList<Product> getAllProducts(){
        return  allProducts;
    }
}
