package com.example.project.controller;


import com.example.project.controller.dto.request.CreateMstmbRequest;
import com.example.project.controller.dto.request.StockRequest;
import com.example.project.controller.dto.resopnse.StatusResponse;
import com.example.project.controller.dto.resopnse.StockResponse;
import com.example.project.model.MstmbRepository;
import com.example.project.model.entity.Mstmb;
import com.example.project.service.MstmbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableScheduling
@RequestMapping("/mstmb")
public class MstmbController {

    @Autowired
    MstmbService mstmbService;
    @Autowired
    MstmbRepository mstmbRepository;

    @GetMapping()
    public List<Mstmb> getDataList(){
        return this.mstmbService.getMstmbList();
    }

    @PostMapping()
    public StatusResponse createData(@RequestBody CreateMstmbRequest request) {
        String userCreate = this.mstmbService.createMstmb(request);
        return new StatusResponse(userCreate);
    }

    @PostMapping("/stock")
    public StockResponse getData(@RequestBody StockRequest request){
        return mstmbService.getStockDetail(request);
    }

    @PostMapping("/updatePrice")
    public StockResponse updatePrice(@RequestBody StockRequest request){
        return mstmbService.updateCacheStock(request);
    }

    @Scheduled(fixedRate = 500000)
    public void updatePrice(){
        for(Mstmb mstmb:mstmbRepository.findAll()){
            mstmb.setNowPrice(makeCurPrice());
            mstmbRepository.save(mstmb);
        }
    }
    public double makeCurPrice(){
        double min = 100.99;
        double max = 500.99;
        return Math.round(((Math.random()*max)+min)*100.0)/100.0;
    }

}
