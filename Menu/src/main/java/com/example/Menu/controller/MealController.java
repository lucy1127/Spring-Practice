package com.example.Menu.controller;

import com.example.Menu.model.Meal;
import com.example.Menu.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/Meal")

public class MealController {
    @Autowired
    private MealService mealService;

    @GetMapping()
    public String getMealByName(@RequestParam String name, Model model){
        Meal meal = this.mealService.getMealByName(name);
        model.addAttribute("meal",meal);
        return "meal";
    }

}
