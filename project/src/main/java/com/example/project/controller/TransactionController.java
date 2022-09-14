package com.example.project.controller;

import com.example.project.controller.dto.request.AddBalanceRequest;
import com.example.project.controller.dto.request.SettlementRequest;
import com.example.project.controller.dto.request.UnrealizedRequest;
import com.example.project.controller.dto.resopnse.SumUnrealizedProfit;
import com.example.project.controller.dto.resopnse.UnrealizedDetail;
import com.example.project.controller.dto.resopnse.UnrealizedDetailResponse;
import com.example.project.controller.error.InvalidPropertiesFormatException;
import com.example.project.controller.error.MstmbNotFoundException;
import com.example.project.controller.error.TcnudNotFoundException;
import com.example.project.service.TransactionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/unreal")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @PostMapping("/detail")
    public UnrealizedDetailResponse getDetailData(@RequestBody UnrealizedRequest request) {

        UnrealizedDetailResponse response = new UnrealizedDetailResponse();

        try {
            String check = checkParameter(request);
            if (!check.equals("Ok")) {
                throw new InvalidPropertiesFormatException(check);
            }
            response.setResultList(transactionService.getDetail(request));
            response.setResponseCode("000");
            if (response.getResultList().isEmpty()) {
                response.setMessage("獲利區間裡無符合資料");
            } else {
                response.setMessage("");
            }
        } catch (TcnudNotFoundException e) {
            response.setResponseCode("001");
            response.setMessage("查無符合資料");
        } catch (InvalidPropertiesFormatException e) {
            response.setResponseCode("002");
            response.setMessage(e.getErrorMessage());
        } catch (Exception e) {
            response.setResponseCode("005");
            response.setMessage("伺服器忙碌中，請稍後嘗試");
        }

        return response;
    }

    @PostMapping("/sum")
    public SumUnrealizedProfit getSumData(@RequestBody UnrealizedRequest request) {
        SumUnrealizedProfit response = new SumUnrealizedProfit();

        try {
            String check = checkParameter(request);
            if (!check.equals("Ok")) {
                throw new InvalidPropertiesFormatException(check);
            }
            response.setResultList(transactionService.sumProfit(request));
            response.setResponseCode("000");
            if (response.getResultList().isEmpty()) {
                response.setMessage("獲利區間裡無符合資料");
            } else {
                response.setMessage("");
            }
        } catch (TcnudNotFoundException e) {
            response.setResponseCode("001");
            response.setMessage("查無符合資料");
        } catch (InvalidPropertiesFormatException e) {
            response.setResponseCode("002");
            response.setMessage(e.getErrorMessage());
        } catch (Exception e) {
            response.setResponseCode("005");
            response.setMessage("伺服器忙碌中，請稍後嘗試");
        }

        return response;

    }

    @PostMapping("/add")
    public UnrealizedDetailResponse addBalanceData(@RequestBody AddBalanceRequest request) {
        try {
            String check = checkRequestParameter(request);
            if (!check.equals("Ok")) {
                throw new InvalidPropertiesFormatException(check);
            }

            List<UnrealizedDetail> results = transactionService.addBalance(request);
            return new UnrealizedDetailResponse(results, "000", "");

        } catch (MstmbNotFoundException e) {
            return new UnrealizedDetailResponse(null, "001", "查無符合股票資料");
        } catch (InvalidPropertiesFormatException e) {
            return new UnrealizedDetailResponse(null, "002", e.getErrorMessage());
        } catch (Exception e) {
            return new UnrealizedDetailResponse(null, "005", "伺服器忙碌中，請稍後嘗試");
        }
    }

    @PostMapping("/today")
    public String queryTodaySettlement(@RequestBody SettlementRequest request) {
        return transactionService.todaySettlement(request);
    }

    //優化
    public String checkParameter(UnrealizedRequest request) {
        if (request.getBranchNo().isEmpty()) {
            return "分行不能為空值";
        }
        if (request.getBranchNo().length() > 4) {
            return "分行最多為4碼";
        }
        if (request.getCustSeq().isEmpty()) {
            return "客戶帳號不能為空值";
        }
        if (request.getCustSeq().length() > 7) {
            return "客戶帳號最多為7碼";
        }
        if (request.getStock() != null) {
            if (request.getStock().length() > 6) {
                return "股票最多為6碼";
            }
        }
        if (request.getMaxProfit() != null && request.getMinProfit() != null) {
            if (request.getMaxProfit() < request.getMinProfit()) {
                return "輸入區間的格式錯誤";
            }
        }
        if (request.getMaxProfit() != null && request.getMinProfit() != null) {
            if (request.getMinProfit().isNaN() || request.getMaxProfit().isNaN()) {
                return "請輸入數字!!!";
            }
        }


        return "Ok";
    }

    public String checkRequestParameter(AddBalanceRequest request) {
        if (request.getBranchNo().isEmpty()) {
            return "分行不能為空值";
        }
        if (request.getBranchNo().length() > 4) {
            return "分行最多為4碼";
        }
        if (request.getCustSeq().isEmpty()) {
            return "客戶帳號不能為空值";
        }
        if (request.getCustSeq().length() > 7) {
            return "客戶帳號最多為7碼";
        }
        if (request.getTradeDate().isEmpty()) {
            return "交易日期不能為空值";
        }
        if (request.getTradeDate() != null) {
            if (request.getTradeDate().length() > 8) {
                return "交易日期最多為8碼";
            }
        }

        if (request.getStock().isEmpty()) {
            return "股票不能為空值";
        }
        if (request.getStock() != null) {
            if (request.getStock().length() > 6) {
                return "股票最多為6碼";
            }
        }
        if (request.getBuyPrice() == null) {
            return "請輸入買進價格 !!!";
        }
        if(request.getBuyPrice() != null && request.getBuyPrice().isNaN()){
            return "請輸入數字 !!!";
        }

        if (request.getQty() == null) {
            return "請輸入股數 !!!";
        }
        if (request.getQty() != null && request.getQty() > 1000000000 ) {
            return "輸入太多股數 !!!";
        }


        return "Ok";
    }
}
