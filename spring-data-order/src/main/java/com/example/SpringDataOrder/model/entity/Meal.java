package com.example.SpringDataOrder.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor //無參數建構子 public Meal (){};
@Entity
@Table(name = "meal")

public class Meal {
    @Id
   // @GeneratedValue(strategy = GenerationType.AUTO) //沒有值的時候 自動產生
    private Integer id;

    private String name;

    private int price;

}

