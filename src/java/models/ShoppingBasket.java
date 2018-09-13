package models;

import java.util.*;

public class ShoppingBasket {
    private ArrayList <ItemInBasket> basket;
    private Payment payment;
    private String basketId;
    private String basketDescription;
    private String order_completion_time;
    private String status;
    private String address;
    private String deliveryType;

    public ShoppingBasket(){}

    public ShoppingBasket(ArrayList<ItemInBasket> basket) {
        this.basket = basket;
    }

    public ArrayList<ItemInBasket> getBasket() {
        return basket;
    }

    public void setBasket(ArrayList<ItemInBasket> basket) {
        this.basket = basket;
    }
    
    public void addItem(ItemInBasket item){
        basket.add(item);
    }
    
    public void addItem(ItemInBasket item, int index){
        basket.add(index, item);
    }
    
    public void removeItem(int index){
        basket.remove(index);
    }
    
    public void removeItem(ItemInBasket item){
        basket.remove(item);
    }
    
    public void clearAllItem(){
        basket.clear();
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public String getBasketDescription() {
        return basketDescription;
    }

    public void setBasketDescription(String basketDescription) {
        this.basketDescription = basketDescription;
    }

    public String getOrder_completion_time() {
        return order_completion_time;
    }

    public void setOrder_completion_time(String order_completion_time) {
        this.order_completion_time = order_completion_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
    
    
        
}
