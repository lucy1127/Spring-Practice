package com.example.project.model;



import com.example.project.model.entity.Tcnud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TcnudRepository  extends JpaRepository<Tcnud,Integer> {

    @Query(value = "select * from tcnud where if(?1 !='',branchNo=?1,1=1) and if(?2 !='',custSeq=?2,1=1)" +
            "and if(?3 !='',stock=?3,1=1)  ",nativeQuery = true)
    List<Tcnud> findByBranchNoAndCustSeqAndStock(String branchNo, String custSeq,String stock);
    @Query(value = "select * from tcnud where if(?1 !='',branchNo=?1,1=1) and if(?2 !='',custSeq=?2,1=1)",nativeQuery = true)
    List<Tcnud> findByBranchNoAndCustSeq(String branchNo, String custSeq);

}
