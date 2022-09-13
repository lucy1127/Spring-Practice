package com.example.project.model;


import com.example.project.model.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday,Integer> {
    Holiday findByHoliday(String holiday);
}
