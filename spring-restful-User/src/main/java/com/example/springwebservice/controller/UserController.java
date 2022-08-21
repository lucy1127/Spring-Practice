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
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        List<User> userList = this.userService.getUserList();
        return userList;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        User user = this.userService.getUserById(id);
        return user;
    }

    @PostMapping()
    public StatusResponse createUser(@RequestBody CreateUserRequest request) {
        String userCreate = this.userService.createUser(request);

        return new StatusResponse(userCreate);
    }

    @PutMapping("/{id}")
    public StatusResponse updateUser(@PathVariable int  id,@RequestBody UpdateUserRequest request) {
        String userUpdate = this.userService.updateUser(id,request);

        return new StatusResponse(userUpdate);
    }

    @DeleteMapping("/{id}")
    public StatusResponse deleteUserById(@PathVariable int id) {

        String userDelete = this.userService.deleteUserById(id);
        return new StatusResponse(userDelete);
    }

    @GetMapping("/getAge")
    public List<User> getAge(@RequestParam int age) {
        List<User> userList = this.userService.getAgeGreaterThanEquals(age);
        return userList;
    }
    @GetMapping("/ageSort")
    public List<User> getALlAndAgeSort(@RequestParam int age,@RequestParam String ageFilter,@RequestParam String sorted) {
        List<User> response = this.userService.getAllAndAgeSort(age,ageFilter,sorted);
        return response;
    }
} // Class end
