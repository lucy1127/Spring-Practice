package com.example.Menu.service;

import com.example.Menu.model.Meal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class MealService {
    List<Meal> mealList;

    public MealService(List<Meal> mealList) {
        this.mealList = new ArrayList<>();
        this.mealList.add(new Meal("hamburger", 100, "This is delicious"));
        this.mealList.add(new Meal("coke", 60, "It's good to drink"));
    }

    public Meal getMealByName(String name) {
        for (int i = 0; i < this.mealList.size(); i++) {
            if (this.mealList.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                return this.mealList.get(i);
            }
        }
        return null;
    }
}

