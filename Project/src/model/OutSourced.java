package model;

/**
 *Extended from Part class. This class defines an instance of the class Outsourced.
 */
public class OutSourced extends Part{

    private String companyName;

    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName(){
        return companyName;
    }
}
