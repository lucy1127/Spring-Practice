package com.example.project.service;


import com.example.project.controller.dto.request.CreateHcmioRequest;
import com.example.project.controller.dto.request.UpdateTcnudRequest;
import com.example.project.model.HcmioRepository;
import com.example.project.model.TcnudRepository;
import com.example.project.model.entity.Hcmio;
import com.example.project.model.entity.Tcnud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HcmioService {

    @Autowired //將該元件初始化後自動帶入
    private HcmioRepository hcmioRepository;
    @Autowired
    private TcnudRepository tcnudRepository;

    @Autowired
    private TcnudService tcnudService;

    public List<Hcmio> getHcmioList(){
        return hcmioRepository.findAll();
    }

    public List<Hcmio> getDataByDocSeq(String date,String branch,String cust,String doc){
        return hcmioRepository.find(date,branch,cust,doc);
    }

    public String createHcmio(CreateHcmioRequest request){

        //hcmio
        Hcmio hcmio = new Hcmio();
        hcmio.setTradeDate(request.getTradeDate());
        hcmio.setDocSeq(request.getDocSeq());
        hcmio.setStock(request.getStock());
        hcmio.setBsType(request.getBsType());
        hcmio.setPrice(request.getPrice());
        hcmio.setQty(request.getQty());

        int qty;
        double amt,price,fee,tax;
        price = request.getPrice();
        qty = request.getQty();
        amt = Math.round(price * qty);
        fee = Math.round(amt * 0.001425);
        tax = Math.round(amt * 0.003);

        double netAmt;
        if(request.getBsType().equals("B")){
            netAmt = -(amt+fee);
        }else{
            netAmt = amt-fee-tax;
        }

        //hcmio
        hcmio.setAmt(amt);
        hcmio.setFee(fee);
        hcmio.setTax(tax);
        hcmio.setNetAmt(netAmt);

        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        hcmio.setModDate(formattedDate);
        hcmio.setModTime(formattedTime);


        if(null != tcnudRepository.findByStock(request.getStock())){ //找到相同的股票
            String oldTcund = this.tcnudService.upDateTchud(request.getBsType(),request.getStock(),request.getPrice(),request.getQty());

        }else{
            //tcnud
            Tcnud tcnud = new Tcnud();
            tcnud.setTradeDate(request.getTradeDate());
            tcnud.setDocSeq(request.getDocSeq());
            tcnud.setStock(request.getStock());
            tcnud.setPrice(request.getPrice());
            tcnud.setFee(fee);
            tcnud.setQty(request.getQty());
            tcnud.setRemainQty(request.getQty()); //剩餘股數
            tcnud.setCost(netAmt);

            tcnud.setModDate(formattedDate);
            tcnud.setModTime(formattedTime);
            tcnudRepository.save(tcnud);
        }

        hcmioRepository.save(hcmio);

        return "Create Success";

    }
    @Transactional
    public String deleteHcmio(String docSeq){
        hcmioRepository.deleteByDocSeq(docSeq);
        return "Delete Success";
    }

}
