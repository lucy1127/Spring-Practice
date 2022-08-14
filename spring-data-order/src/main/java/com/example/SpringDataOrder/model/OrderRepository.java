package com.example.SpringDataOrder.model;


import com.example.SpringDataOrder.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    Order findById(int id);
    Long  deleteById(int id);
}
