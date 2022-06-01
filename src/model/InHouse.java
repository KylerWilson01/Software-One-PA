package model;

/**
 * This class extends the part class to show what machine the part was made at.
 * @author Kyler Wilson
 */
public class InHouse extends Part {
    private int machineId;

    /**
     * This makes a new outsourced part that we will keep track of in the inventory class.
     * @param id this will be the part id.
     * @param name this is the name of the part.
     * @param price this is how much the part cost.
     * @param stock this is how much of the part is in stock.
     * @param min this is the minimum value of stock the user allows.
     * @param max this is the maximum value of stock the user allows.
     * @param machineId this is the machine where the part was made.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);

        this.machineId = machineId;
    }

    /**
     * This will set the machine id to where the part was made at.
     * @param machineId the machine id to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * This will grab the machine id to where the part was made at.
     * @return this machine's id
     */
    public int getMachineId() {
        return this.machineId;
    }
}
