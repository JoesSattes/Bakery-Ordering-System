package models;

public class OrderedItem {
    private int shopping_basket_id;
    private int itemAmount;
    private double itemPrice;
    private int inventoryId;
    private String itemName;
    private double deliveryCost;
    private int minimum;
    private String status;

    public OrderedItem() {
    }

    public OrderedItem(int shopping_basket_id, int itemAmount, double itemPrice, int inventoryId, String itemName, double deliveryCost, int minimum, String status) {
        this.shopping_basket_id = shopping_basket_id;
        this.itemAmount = itemAmount;
        this.itemPrice = itemPrice;
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.deliveryCost = deliveryCost;
        this.minimum = minimum;
        this.status = status;
    }

    public int getShopping_basket_id() {
        return shopping_basket_id;
    }

    public void setShopping_basket_id(int shopping_basket_id) {
        this.shopping_basket_id = shopping_basket_id;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
