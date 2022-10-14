package com.example.Spring1.model;

import com.example.Spring1.model.entity.Cashi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CashiRepository extends JpaRepository<Cashi,String>, JpaSpecificationExecutor<Cashi> {
    @Transactional
    @Modifying
    @Query(value = "Delete from cashi where mgni_id =?1 ",nativeQuery = true)
    void deleteCashi(String id);

    @Query(value = "Select * from cashi where mgni_id =?1 ",nativeQuery = true)
    List<Cashi> findCashi(String id);


}
