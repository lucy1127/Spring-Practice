package com.example.springwebservice.controller;

import com.example.springwebservice.model.entity.User;
import com.example.springwebservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/part4/user")
public class Part4Controller {

    @Autowired
    UserService userService;

    @GetMapping()
    public List<String> getUserList(){
        List<String> userList = this.userService.getNoRepeatUser();
        return userList;
    }

    @GetMapping("/getOne")
    public Map<Integer,String> getOneMap(){
        Map<Integer,String> userList = this.userService.getMap();
        return userList;
    }
    @GetMapping("/getFirst")
    public Optional<User> getFirst(){
        Optional<User> userList = this.userService.getFirst();
        return userList;
    }

    @GetMapping("/getOrderBy")
    public List<User> getOrderBy(){
        List<User> userList = this.userService.getOrderBy();
        return userList;
    }

    @GetMapping("/getAllBy")
    public String getAllByString(){
        String userList = this.userService.getAllByString();
        return userList;
    }
}
