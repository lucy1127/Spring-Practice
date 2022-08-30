package com.example.project.model;



import com.example.project.model.entity.Tcnud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcnudRepository  extends JpaRepository<Tcnud,Integer> {

    @Query(value = "select * from tcnud where if(?1 !='',tradeDate=?1,1=1) and if(?2 !='',branchNo=?2,1=1)" +
            "and if(?3 !='',custSeq=?3,1=1)  " +"and if(?4 !='',docSeq=?4,1=1)  ",nativeQuery = true)
    List<Tcnud> find(String tradeDate, String branchNo, String custSeq, String docSeq);
    Tcnud findByDocSeq(String docSeq);
    Tcnud findByStock(String stock);
    Long deleteByDocSeq(String docSeq);
}
