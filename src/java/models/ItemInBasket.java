
package models;

public class ItemInBasket {
    private int shoppingBasketId;
    private int itemNo;
    private int itemAmount;
    private double itemPrice;
    private int inventoryId;
    private String itemName;
    private double deliveryCost;
    private int minimum;
    private String status = "pending";
    private Review review;

    public ItemInBasket() {
    }

    public ItemInBasket(int shoppingBasketId, int itemNo, int itemAmount, double itemPrice, int inventoryId, String itemName, double deliveryCost, int minimum) {
        this.shoppingBasketId = shoppingBasketId;
        this.itemNo = itemNo;
        this.itemAmount = itemAmount;
        this.itemPrice = itemPrice;
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.deliveryCost = deliveryCost;
        this.minimum = minimum;
    }    

    public int getShoppingBasketId() {
        return shoppingBasketId;
    }

    public void setShoppingBasketId(int shoppingBasketId) {
        this.shoppingBasketId = shoppingBasketId;
    }

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
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

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
