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
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> userList = this.userService.getUserList();
        return userList;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable int id) {
        User user = this.userService.getUserById(id);
        return user;
    }

    @PostMapping("/users")
     void addUser(@RequestBody CreateUserRequest request) {
        String userCreate = this.userService.createUser(request);

    }

    @PutMapping("/users/{id}")
    void updateUser(@PathVariable int  id,@RequestBody UpdateUserRequest request) {
        String userUpdate = this.userService.updateUser(id,request);

       // return new StatusResponse(userUpdate);
    }

    @DeleteMapping("/users/{id}")
    void deleteUserById(@PathVariable int id) {

        String userDelete = this.userService.deleteUserById(id);
        //return new StatusResponse(userDelete);
    }
} // Class end
