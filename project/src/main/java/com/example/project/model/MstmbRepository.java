package com.example.project.model;

import com.example.project.model.entity.Mstmb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MstmbRepository extends JpaRepository<Mstmb,String> {

    List<Mstmb> findByStock(String stock);
    Long deleteByStock(String stock);
}
