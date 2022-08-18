package com.example.springwebservice.model;



import com.example.springwebservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    User findById(int id);
    Long deleteById(int id);
}
