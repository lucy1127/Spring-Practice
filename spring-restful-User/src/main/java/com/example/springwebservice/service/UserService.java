package com.example.springwebservice.service;

import com.example.springwebservice.controller.dto.request.CreateUserRequest;
import com.example.springwebservice.controller.dto.request.UpdateUserRequest;
import com.example.springwebservice.model.UserRepository;
import com.example.springwebservice.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    List<User> userList;

    @Autowired
    private UserRepository userRepository;

    public List<User> getUserList() {
        List<User> response = userRepository.findAll();
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
        user.setAge(request.getAge());
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
        user.setAge(request.getAge());

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

    public List<User> getAgeGreaterThanEquals(int age){
        List<User> response = new ArrayList<>();
        if (0 < age) {
            response = userRepository.findByAgeGreaterThanEqual(age);
        }else {
            response = userRepository.findAll();
        }
        return response;
    }

    public List<User> getAllAndAgeSort(int age,String ageFilter,String sorted){
        List<User> response = new ArrayList<>();
        if(0 >= age){

            switch (sorted){

                case "asc":
                    response = userRepository.findAllByOrderByAgeAsc();
                    break;
                case "desc":
                    response = userRepository.findAllByOrderByAgeDesc();
                    break;
                default:
                    response = userRepository.findAll();
                    break;
            }
        }
        return response;
    }

    public List<User> findUserByQuery(String name,int age){ //新增
        List<User> response = userRepository.findByNameAndAge(name,age);

        return response;
    }

    public String createUserBySql(CreateUserRequest request){
        userRepository.createUserBySql(request.getId(),request.getName(), request.getAge());
        return "Ok";
    }

    public String updateUserBySql(int id,UpdateUserRequest request){

        int count = userRepository.updateUserBySql(request.getAge(),request.getName(), id);

        if(0<count){
            return "Ok";
        }else{
            return "FAIL";
        }
    }

    public String deleteUserBySql(String name,int age){

        userRepository.deleteUserBySql(name,age);
        return "Ok";
    }

    public List<String> getNoRepeatUser(){
        List<User> response = userRepository.findAll();

        List<String> newResponse = response.stream().map(User::getName).distinct().collect(Collectors.toList());
        return newResponse;
    }
    public Map<Integer,String> getMap(){
        List<User> response = userRepository.findAll();

        Map<Integer,String> newResponse = response.stream().collect(Collectors.toMap(u -> u.getId(),u -> u.getName()));

        return newResponse;
    }
    public Optional<User> getFirst(){
        List<User> response = userRepository.findAll();
        String s = "KZ";
        Optional<User> newResponse = response.stream().filter(u -> u.getName().equals(s)).findFirst();
        return newResponse;
    }

    public List<User> getOrderBy() {
        List<User> userList = userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getAge)
                        .reversed().thenComparing(User::getId))
                .collect(Collectors.toList());
        return userList;
    }
    public String getAllByString() {
        String userList = "";
        userList += userRepository.findAll().stream().map(p -> p.getName() + "," + p.getAge()).collect(Collectors.joining("|"));
        return userList;
    }



} // Class end
