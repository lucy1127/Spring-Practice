package com.example.springwebservice.service;

import com.example.springwebservice.controller.dto.request.CreateUserRequest;
import com.example.springwebservice.controller.dto.request.UpdateUserRequest;
import com.example.springwebservice.model.UserRepository;
import com.example.springwebservice.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    List<User> userList;

    @Autowired
    private UserRepository userRepository;

    public List<User> getUserList() {
        List<User> response = (List<User>) userRepository.findAll();
        return response;
    }


    public User getUserById(int id) {
        User response = userRepository.findById(id);
        return response;
    }

    public String createUser(CreateUserRequest request){
        //新增一個空的 user的entity
        User user = new User();
        //塞好資料
        user.setId(request.getId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        //user.setAge(request.getAge());
        //儲存進DB
        userRepository.save(user);

        //告訴Controller 完成儲存
        return "OK";
    }

    public String updateUser(int  id,UpdateUserRequest request){

        User user = userRepository.findById(id);

        if(null == user){
            return "FAIL";
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        //user.setAge(request.getAge());

        userRepository.save(user);
        return "OK";

    }

    public String deleteUserById(int id) {
        User user = userRepository.findById(id);

        if(null == user){
            return "FAIL";
        }
        Long count = userRepository.deleteById(id);

        return "OK";
    }

} // Class end
