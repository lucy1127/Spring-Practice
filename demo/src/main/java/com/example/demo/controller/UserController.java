package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public String getUserById(@PathVariable String id, Model model){
        User user = this.userService.getUserById(id);
        model.addAttribute("user",user);
        return "user";
    }
    @GetMapping()
    public String getUserByName(@RequestParam String name,Model model){
        User user = this.userService.getUserByName(name);
        model.addAttribute("user",user);
        return "user";
    }

}
