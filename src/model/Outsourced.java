package model;

/**
 * This class extends the part class to show what company the part was made from.
 * @author Kyler Wilson
 */
public class Outsourced extends Part {
    private String companyName;

    /**
     * This makes a new outsourced part that we will keep track of in the inventory class.
     * @param id this will be the part id.
     * @param name this is the name of the part.
     * @param price this is how much the part cost.
     * @param stock this is how much of the part is in stock.
     * @param min this is the minimum value of stock the user allows.
     * @param max this is the maximum value of stock the user allows.
     * @param companyName this is the company that makes the part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        this.companyName = companyName;
    }

    /**
     * This will update the company name.
     * @param companyName the company that manufactured the part
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * This will grab the company name.
     * @return the company that manufactured the part
     */
    public String getCompanyName() {
        return companyName;
    }
}
