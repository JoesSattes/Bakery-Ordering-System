package models;

import java.util.ArrayList;

public class TotalItemInBasket {
    private ArrayList <ItemInBasket> totalBasket;

    public TotalItemInBasket() {
    }

    public TotalItemInBasket(ArrayList<ItemInBasket> totalBasket) {
        this.totalBasket = totalBasket;
    }

    public ArrayList<ItemInBasket> getTotalBasket() {
        return totalBasket;
    }

    public void setTotalBasket(ArrayList<ItemInBasket> totalBasket) {
        this.totalBasket = totalBasket;
    }
}
