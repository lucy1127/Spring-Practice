package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service

public class UserService {

    List<User> userList;


    public UserService(List<User> userList) {
        this.userList = new ArrayList<>();
        this.userList.add(new User("1", "Andy", 22));
    }

    public User getUserById(String id) {

        for (int i = 0; i < this.userList.size(); i++) {
            if (this.userList.get(i).getId().equals(id)) {
                return this.userList.get(i);
            }
        }
        return null;
    }

    @GetMapping()
    public User getUserByName(String name) {
        for (int i = 0; i < this.userList.size(); i++) {
            if (this.userList.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                return this.userList.get(i);
            }
        }
        return null;
    }
}