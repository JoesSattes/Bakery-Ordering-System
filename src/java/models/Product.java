/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author Sattaya Singkul
 */
public class Product {

    private String productName;
    private double productPrice;
    private String productDescription;
    private int productStock;
    private int productId;
    private int minimum;
    private double deliveryCost;
    private ArrayList<String> tag;
    private ArrayList photoPath;
    private ArrayList<TermOfUse> termOfUses;

    public Product(){
        this.tag = new ArrayList();
        this.photoPath = new ArrayList();
    }
    public Product(int id, String name, double price, String descript, int stock, int minimum, double delivery){
        this.productId = id;
        this.productName = name;
        this.productPrice = price;
        this.productDescription = descript;
        this.productStock = stock;
        this.minimum = minimum;
        this.deliveryCost = delivery;
        this.tag = new ArrayList();
        this.photoPath = new ArrayList();
    }
    
    public Product(int id, String name, double price, String descript, int stock, int minimum, double delivery, ArrayList tag){
        this.productId = id;
        this.productName = name;
        this.productPrice = price;
        this.productDescription = descript;
        this.productStock = stock;
        this.minimum = minimum;
        this.deliveryCost = delivery;
        this.tag = tag;
    }
    
    public Product(int id, String name, double price, String descript, int stock, int minimum, double delivery, ArrayList tag, ArrayList photoPath){
        this.productId = id;
        this.productName = name;
        this.productPrice = price;
        this.productDescription = descript;
        this.productStock = stock;
        this.minimum = minimum;
        this.deliveryCost = delivery;
        this.tag = tag;
        this.photoPath = photoPath;
    }
    
    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productPrice
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * @param productPrice the productPrice to set
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * @return the productDescription
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * @param productDescription the productDescription to set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * @return the productStock
     */
    public int getProductStock() {
        return productStock;
    }

    /**
     * @param productStock the productStock to set
     */
    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    /**
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return the minimum
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * @param minimum the minimum to set
     */
    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    /**
     * @return the deliveryCost
     */
    public double getDeliveryCost() {
        return deliveryCost;
    }

    /**
     * @param deliveryCost the deliveryCost to set
     */
    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    /**
     * @return the tag
     */
    public ArrayList<String> getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }
    
    public void addTag(String tagName){
        tag.add(tagName);
    }
    
    public void removeTag(int index){
        tag.remove(index);
    }
    
    public void clearTag(){
        tag.clear();
    }
    
    /**
     * @return the photoPath
     */
    public ArrayList getPhotoPath() {
        return photoPath;
    }

    /**
     * @param photoPath the photoPath to set
     */
    public void setPhotoPath(ArrayList<String> photoPath) {
        this.photoPath = photoPath;
    }
    
    public void addPhotoPath(String path){
        photoPath.add(path);
    }
    
    public void removePhotoPath(int index){
        photoPath.remove(index);
    }
    
    public void clearPhotoPath(){
        photoPath.clear();
    }

    /**
     * @return the termOfUses
     */
    public ArrayList<TermOfUse> getTermOfUses() {
        return termOfUses;
    }

    /**
     * @param termOfUses the termOfUses to set
     */
    public void setTermOfUses(ArrayList<TermOfUse> termOfUses) {
        this.termOfUses = termOfUses;
    }
    
    public void addTerm(TermOfUse term){
        termOfUses.add(term);
    }
    
    public void removeTerm(int index){
        termOfUses.remove(index);
    }
    
    public void clearTermOfUse(){
        termOfUses.clear();
    }
}