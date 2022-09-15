package com.example.project.service;


import com.example.project.controller.dto.request.CreateMstmbRequest;
import com.example.project.controller.dto.request.StockRequest;
import com.example.project.controller.dto.resopnse.StockResponse;
import com.example.project.model.MstmbRepository;
import com.example.project.model.entity.Mstmb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MstmbService {

    @Autowired
    private MstmbRepository mstmbRepository;

    public List<Mstmb> getMstmbList(){
        return mstmbRepository.findAll();
    }

    public String createMstmb(CreateMstmbRequest request){

        if(request.getStock().isEmpty() || request.getStockName().isEmpty() || request.getMarketType().isEmpty()){
            return "Something missing...Check it again....";
        }

        Mstmb mstmb = new Mstmb();
        mstmb.setStock(request.getStock());
        mstmb.setStockName(request.getStockName());

        switch (request.getMarketType()){

            case "T": // 上市
                mstmb.setMarketType("上市") ;
                break;
            case "O": // 上櫃
                mstmb.setMarketType("上櫃") ;
                break;
            case "R": // 興櫃
                mstmb.setMarketType("興櫃") ;
                break;
            default:
                return "Type is not correct";
        }

        mstmb.setNowPrice(makeCurPrice());


        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        mstmb.setModDate(formattedDate);
        mstmb.setModTime(formattedTime);

        mstmbRepository.save(mstmb);

        return "Create Success";
    }

    @Cacheable(cacheNames = "mstmb_cache", key = "#request.getStock()")
    public StockResponse getStockDetail(StockRequest request){
        StockResponse response = new StockResponse();
        Mstmb mstmb = mstmbRepository.findByStock(request.getStock());
        if(null == mstmb){
            response.setResponse("無此檔股票資訊");
            return response;
        }


        response.setMstmb(mstmb);
        response.setResponse("Success");
        return response;
    }

    @CachePut(value = "mstmb_cache", key = "#request.getStock()")
    public StockResponse updateCacheStock(StockRequest request){
        StockResponse response = new StockResponse();
        Mstmb mstmb = mstmbRepository.findByStock(request.getStock());

        if(null == mstmb){
            response.setResponse("無此檔股票資訊");
            return response;
        }

        mstmb.setNowPrice(makeCurPrice());
        mstmbRepository.save(mstmb);
        response.setMstmb(mstmb);
        response.setResponse("Success");
        return response;
    }

    public double makeCurPrice(){
        double min = 10.99;
        double max = 500.99;

        return Math.round(((Math.random()*max)+min)*100.0)/100.0;
    }

}
