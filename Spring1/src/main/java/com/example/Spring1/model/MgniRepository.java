package com.example.Spring1.model;

import com.example.Spring1.model.entity.Mgni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MgniRepository extends JpaRepository<Mgni,String> , JpaSpecificationExecutor<Mgni> {
    @Query(value = "select * from mgni where id =?1",nativeQuery = true)
    Mgni findMgni(String id);

    @Transactional
    @Modifying
    @Query(value = "Delete from mgni where id =?1 ",nativeQuery = true)
    void deleteMgni(String id);
}
