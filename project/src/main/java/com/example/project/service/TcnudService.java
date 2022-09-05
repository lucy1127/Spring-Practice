package com.example.project.service;


import com.example.project.model.TcnudRepository;
import com.example.project.model.entity.Hcmio;
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
    public void createTchud(Hcmio hcmio){
        Tcnud tcnud = new Tcnud();
        tcnud.setTradeDate(hcmio.getTradeDate());
        tcnud.setDocSeq(hcmio.getDocSeq());
        tcnud.setStock(hcmio.getStock());
        tcnud.setPrice(hcmio.getPrice());

        double amt,fee;
        amt = Math.floor(hcmio.getPrice() * hcmio.getQty());
        fee = Math.floor(amt * 0.001425);

        double cost;
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
    }
    public void upDateTchud(String type, String stock, double price, int qty){
        Tcnud tcnud = tcnudRepository.findByStock(stock);

        double amt;
        if(type.equals("B")||type.equals("b")){
            double addPrice; //成本 當時股價*股數+現買股價*股數/總股數
            addPrice =  (tcnud.getPrice()*tcnud.getQty() + price*qty) / (tcnud.getQty() +qty);
            tcnud.setPrice(addPrice);
            int addQty;
            addQty = tcnud.getQty() + qty;
            tcnud.setQty(addQty);
            tcnud.setRemainQty(addQty);

            amt = addPrice * addQty;

            tcnud.setFee(Math.floor(amt*0.001425));
            tcnud.setCost(amt+Math.floor(amt*0.001425));

        }else if(type.equals("S")||type.equals("s")){
            int reduceQty;
            reduceQty = tcnud.getQty() - qty;

            tcnud.setRemainQty(reduceQty);
            amt = tcnud.getPrice() * reduceQty;

            tcnud.setFee(Math.floor(amt*0.001425));
            tcnud.setCost(amt +Math.floor(amt*0.001425));

            if(reduceQty == 0) {
                tcnudRepository.deleteByStock(stock);
            }
        }

        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmss"));
        tcnud.setModDate(formattedDate);
        tcnud.setModTime(formattedTime);

        tcnudRepository.save(tcnud);
    }

//    @Transactional
//    public String deleteTcnud(String docSeq){
//        tcnudRepository.deleteByDocSeq(docSeq);
//        return "Delete Success";
//    }


}
