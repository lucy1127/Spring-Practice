package com.example.project.model;


import com.example.project.model.entity.Hcmio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HcmioRepository extends JpaRepository<Hcmio,Integer> {

    Hcmio findFirstByOrderByDocSeqDesc();
}
