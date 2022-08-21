package com.example.springwebservice.controller;


import com.example.springwebservice.controller.dto.request.CreateUserRequest;
import com.example.springwebservice.controller.dto.request.UpdateUserRequest;
import com.example.springwebservice.controller.dto.response.StatusResponse;
import com.example.springwebservice.model.entity.User;
import com.example.springwebservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/practice/user")
public class PracticeController {


    @Autowired
    UserService userService;

    @GetMapping()
    public List<User> getUserList(@RequestParam String name,@RequestParam int age){
        List<User> userList = this.userService.findUserByQuery(name,age);
        return userList;
    }

    @PostMapping()
    public StatusResponse createUserBySql(@RequestBody CreateUserRequest request){
        String response = this.userService.createUserBySql(request);
        return new StatusResponse(response);
    }
    @PutMapping("/{id}")
    public StatusResponse updateUserBySql(@PathVariable int id,@RequestBody UpdateUserRequest request){
        String response = this.userService.updateUserBySql(id,request);
        return new StatusResponse(response);
    }

    @DeleteMapping()
    public StatusResponse deleteUserBySql(@RequestParam String name,@RequestParam int age){
        String response = this.userService.deleteUserBySql(name,age);
        return new StatusResponse(response);
    }

}
