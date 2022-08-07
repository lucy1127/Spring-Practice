package com.example.ShoppingCart.service;


import com.example.ShoppingCart.model.BuyList;
import com.example.ShoppingCart.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private List<User> userList;

    public UserService(){

        List<BuyList> buyList1 = new ArrayList<>();
        buyList1.add(new BuyList("Shirt",250));
        buyList1.add(new BuyList("dress",100));

        List<BuyList> buyList2 = new ArrayList<>();
        buyList2.add(new BuyList("Jacket",500));
        buyList2.add(new BuyList("Belt",290));

        this.userList = new ArrayList<>();
        this.userList.add(new User(1,"Lucy",buyList1));
        this.userList.add(new User(2,"Louis",buyList2));
    }

    public List<User> getAllUsers(){ //所有使用者的資料
        return this.userList;
    }

    public User getUserById(int id){ //獲取單一使用者的資料
        for(User userId:this.userList){
            if(id == userId.getId()){
                return userId;
            }
        }
        return null;
    }
    public User createUser(User user){
        this.userList.add(user);
        return user;
    }

    public User updateUser(int id,User user){
        for(User updatedUser:this.userList){
            if(id == updatedUser.getId()){
                updatedUser.setName(user.getName());
                updatedUser.setBuyList(user.getBuyList());
                updatedUser.setTotalPrice(user.getTotalPrice());
                return updatedUser;
            }
        }
        return null;
    }

    public User deleteUser(int id){
        for(User user:this.userList){
            if(id == user.getId()){
                this.userList.remove(user);
                return user;
            }
        }
        return null;
    }
}
