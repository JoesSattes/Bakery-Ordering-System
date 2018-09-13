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
public class Inventory {
    
    private ArrayList<Product> listProductDetail;
    
    public Inventory(){
        this.listProductDetail = new ArrayList<Product>();
    }
    
    public Inventory(ArrayList<Product> listProductDetail){
        this.listProductDetail = listProductDetail;
    }

    /**
     * @return the listProductDetail
     */
    public ArrayList<Product> getListProductDetail() {
        return listProductDetail;
    }

    /**
     * @param listProductDetail the listProductDetail to set
     */
    public void setListProductDetail(ArrayList<Product> listProductDetail) {
        this.listProductDetail = listProductDetail;
    }
    
    public void clearListProductDetail() {
        this.listProductDetail.clear();
    }
    
    public void addProductInList(Product productDetail){
        listProductDetail.add(productDetail);
    }
    
    public void addProductInList(Product productDetail, int index){
        listProductDetail.add(index, productDetail);
    }
    
    public Product getProductInList(int index){
        return listProductDetail.get(index);
    }

    public void removeProductInList(int index){
        listProductDetail.remove(index);
    }
    
}