package com.example.ShoppingCart.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class User {
    private int id;
    private String name;
    private int totalPrice;
    private List<BuyList> buyList;

    public User(int id,String name, List<BuyList> buyList){
        this.id = id;
        this.name = name;
        this.buyList = buyList;
    }

    public int getTotalPrice(){
        int totalPrcie = 0;
        for(BuyList list:this.getBuyList()){
            totalPrcie += list.getPrice();
        }
        return totalPrcie;
    }
}
