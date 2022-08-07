package com.example.ShoppingCart.cotroller;


import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> getAllUsers(){ //取得所有使用者
        List<User> userList = this.userService.getAllUsers();
        return userList;
    }

    @GetMapping("/{id}") //根據ID取得單一使用者訂單
    public User getUserById(@PathVariable int id){
        User user = this.userService.getUserById(id);
        return user;
    }

    @PostMapping() //新增使用者
    public User createUser(@RequestBody User user){
        User createUser = this.userService.createUser(user);
        return user;
    }

    @PutMapping("/{id}") //修改使用者資料
    public User updateUser(@PathVariable int id,@RequestBody User user){
        User updateUser = this.userService.updateUser(id,user);
        return updateUser;
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable int id){
        User deleteUser = this.userService.deleteUser(id);
        return deleteUser;
    }


}
