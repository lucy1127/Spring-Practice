package com.example.springwebservice.model;



import com.example.springwebservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findById(int id);

    Long deleteById(int id);

    List<User> findByAgeGreaterThanEqual(int age);
    List<User> findAllByOrderByAgeDesc();
    List<User> findAllByOrderByAgeAsc();

    @Query(value ="SELECT * FROM member WHERE name = ?1 AND age = ?2", nativeQuery = true )
    List<User> findByNameAndAge(String name,int age);

    @Query(value = "INSERT INTO member VALUES (?1,?2,?3)",nativeQuery = true)
    void createUserBySql(int id,String name,int age);
    @Modifying
    @Transactional
    @Query(value = "UPDATE member SET age = ?1,name = ?2 WHERE id = ?3",nativeQuery = true)
    int updateUserBySql(int age,String name,int id);

    @Query(value = "DELETE FROM member WHERE name = ?1 AND age = ?2",nativeQuery = true)
    void deleteUserBySql(String name,int age);


}
