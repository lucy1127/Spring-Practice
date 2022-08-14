package com.example.SpringDataOrder.model;


import com.example.SpringDataOrder.model.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal,Integer> { //<要抓的Table (@Entity),主鍵的型態// >

    Meal findById(int id);
    //List<Meal> findByName(String name);

    Long deleteById(int id);
}
