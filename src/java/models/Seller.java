/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Sattaya Singkul
 */
public class Seller {
    
    private String storeName;
    private String storeDescript;
    private String storeAddress;
    private String storeTerm;
    private int storeType;
    private Inventory inventory;
    private OrderedItem orderedItem;
    
    public Seller(){}
    public Seller(String storeName, String storeDescript, String storeAddress, String storeTerm){
        this.storeName = storeName;
        this.storeDescript = storeDescript;
        this.storeAddress = storeAddress;
        this.storeTerm = storeTerm;
    }

    /**
     * @return the storeName
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * @param storeName the storeName to set
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * @return the storeDescript
     */
    public String getStoreDescript() {
        return storeDescript;
    }

    /**
     * @param storeDescript the storeDescript to set
     */
    public void setStoreDescript(String storeDescript) {
        this.storeDescript = storeDescript;
    }

    /**
     * @return the storeAddress
     */
    public String getStoreAddress() {
        return storeAddress;
    }

    /**
     * @param storeAddress the storeAddress to set
     */
    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    /**
     * @return the storeTerm
     */
    public String getStoreTerm() {
        return storeTerm;
    }

    /**
     * @param storeTerm the storeTerm to set
     */
    public void setStoreTerm(String storeTerm) {
        this.storeTerm = storeTerm;
    }
    
    /**
     * @return the storeType
     */
    public int getStoreType() {
        return storeType;
    }

    /**
     * @param storeType the storeType to set
     */
    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    /**
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * @return the orderedItem
     */
    public OrderedItem getOrderedItem() {
        return orderedItem;
    }

    /**
     * @param orderedItem the orderedItem to set
     */
    public void setOrderedItem(OrderedItem orderedItem) {
        this.orderedItem = orderedItem;
    }
    
    
}