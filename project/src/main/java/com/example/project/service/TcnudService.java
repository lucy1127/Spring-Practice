package com.example.project.service;


import com.example.project.controller.dto.request.UpdateTcnudRequest;
import com.example.project.model.TcnudRepository;
import com.example.project.model.entity.Tcnud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TcnudService {
    @Autowired
    private TcnudRepository tcnudRepository;

    public List<Tcnud> getTcnudList(){
        return tcnudRepository.findAll();
    }
    public List<Tcnud> getDataByDocSeq(String date, String branch, String cust, String doc){
        return tcnudRepository.find(date,branch,cust,doc);
    }
    public String upDateTchud(String type,String stock,double price,int qty){
        Tcnud tcnud = tcnudRepository.findByStock(stock);

        if(null == tcnud){
            return "FAIL";
        }

        if(type.equals("B")||type.equals("b")){
            double addPrice; //成本 當時股價*股數+現買股價*股數/總股數
            addPrice =  (tcnud.getPrice()*tcnud.getQty() + price*qty) / (tcnud.getQty() +qty);
            tcnud.setPrice(addPrice);
            int addQty;
            addQty = tcnud.getQty() + qty;
            tcnud.setQty(addQty);
            tcnud.setRemainQty(addQty);

            double amt,fee,cost;
            amt = addPrice * addQty;
            fee = Math.round(amt*0.001425);
            cost = amt +fee;

            tcnud.setFee(fee);
            tcnud.setCost(cost);

        }else if(type.equals("S")||type.equals("s")){
            int reduceQty;
            reduceQty = tcnud.getQty() - qty;
            tcnud.setRemainQty(reduceQty);

            double amt,fee,cost;
            amt = tcnud.getPrice() * reduceQty;
            fee = Math.round(amt*0.001425);
            cost = amt +fee;

            tcnud.setFee(fee);
            tcnud.setCost(cost);

        }else{
            return "Type is not correct";
        }

        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmss"));
        tcnud.setModDate(formattedDate);
        tcnud.setModTime(formattedTime);

        tcnudRepository.save(tcnud);
        return "OK";
    }
    @Transactional
    public String deleteTcnud(String docSeq){
        tcnudRepository.deleteByDocSeq(docSeq);
        return "Delete Success";
    }


}
