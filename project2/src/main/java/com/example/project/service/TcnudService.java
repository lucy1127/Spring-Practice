package com.example.project.service;


import com.example.project.model.*;
import com.example.project.model.entity.Hcmio;
import com.example.project.model.entity.Mstmb;
import com.example.project.model.entity.Tcnud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TcnudService {
    @Autowired
    private TcnudRepository tcnudRepository;
    public Tcnud createTchud(Hcmio hcmio){

        Tcnud tcnud = new Tcnud();
        tcnud.setTradeDate(hcmio.getTradeDate());
        tcnud.setBranchNo(hcmio.getBranchNo());
        tcnud.setCustSeq(hcmio.getCustSeq());
        tcnud.setDocSeq(hcmio.getDocSeq());
        tcnud.setStock(hcmio.getStock());
        tcnud.setBuyPrice(hcmio.getBuyPrice());

        double amt,fee,cost;
        amt = Math.floor(hcmio.getBuyPrice() * hcmio.getQty());
        fee = Math.floor(amt * 0.001425);
        cost = amt+fee;

        tcnud.setFee(fee);
        tcnud.setQty(hcmio.getQty());
        tcnud.setRemainQty(hcmio.getQty()); //剩餘股數
        tcnud.setCost(cost);


        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmss"));


        tcnud.setModDate(formattedDate);
        tcnud.setModTime(formattedTime);
        tcnudRepository.save(tcnud);
        return tcnud;
    }

}
