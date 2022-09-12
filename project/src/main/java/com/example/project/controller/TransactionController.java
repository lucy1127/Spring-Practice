package com.example.project.controller;

import com.example.project.controller.dto.request.AddBalanceRequest;
import com.example.project.controller.dto.request.UnrealizedRequest;
import com.example.project.controller.dto.resopnse.SumUnrealizedProfit;
import com.example.project.controller.dto.resopnse.UnrealizedDetailResponse;
import com.example.project.service.TcnudService;
import com.example.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.InvalidPropertiesFormatException;

@RestController
@RequestMapping("/api/v1/unreal")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    

    @PostMapping("/detail")
    public UnrealizedDetailResponse getDetailData(@RequestBody UnrealizedRequest request){

        return transactionService.getDetail(request);
    }

    @PostMapping("/sum")
    public SumUnrealizedProfit getSumData(@RequestBody UnrealizedRequest request) throws InvalidPropertiesFormatException, ChangeSetPersister.NotFoundException {
        if (!transactionService.checkParameter(request).equals("Ok")) {
            return new SumUnrealizedProfit(null, "002", transactionService.checkParameter(request));
        }
        if(request.getStock() != null) {
            return transactionService.sumProfit(request);
        }else{

           // return transactionService.sumNoStockProfit(request.getBranchNo(),request.getCustSeq());
        }
    }

    @PostMapping("/add")
    public UnrealizedDetailResponse addBalanceData(@RequestBody AddBalanceRequest request){
        return transactionService.addBalance(request);
    }


}
