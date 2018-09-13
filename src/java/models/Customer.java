package models;

import java.util.ArrayList;

public class Customer {
    private String birthday;
    private ArrayList <CustomerAddress> customerAddress;
    private ShoppingBasket shoppingBasket;
    private TotalItemInBasket totalItemInBasket;

    public Customer(){}

    public Customer(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ArrayList<CustomerAddress> getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(ArrayList<CustomerAddress> customerAddress) {
        this.customerAddress = customerAddress;
    }

    public ShoppingBasket getShoppingBasket() {
        return shoppingBasket;
    }

    public void setShoppingBasket(ShoppingBasket shoppingBasket) {
        this.shoppingBasket = shoppingBasket;
    }

    public TotalItemInBasket getTotalItemInBasket() {
        return totalItemInBasket;
    }

    public void setTotalItemInBasket(TotalItemInBasket totalItemInBasket) {
        this.totalItemInBasket = totalItemInBasket;
    }
    
    public void addAddress(CustomerAddress address){
        customerAddress.add(address);
    }
}
