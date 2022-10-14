package com.example.Spring1.controller;


import com.example.Spring1.controller.dto.request.CashiRequest;
import com.example.Spring1.controller.dto.request.CreateMgniRequest;
import com.example.Spring1.controller.dto.request.DeleteRequest;
import com.example.Spring1.controller.dto.request.MgniRequest;
import com.example.Spring1.controller.dto.response.CashiResponse;
import com.example.Spring1.controller.dto.response.DeleteResponse;
import com.example.Spring1.controller.dto.response.MgniResponse;
import com.example.Spring1.controller.dto.response.UpdateResponse;
import com.example.Spring1.controller.error.MgniNotFoundException;
import com.example.Spring1.model.MgniRepository;
import com.example.Spring1.model.entity.Mgni;
import com.example.Spring1.service.MgniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Validated
@RestController
@RequestMapping("/spring1")
public class MgniController {
    @Autowired
    MgniService mgniService;


    @PostMapping(value = "/create"/*, produces={"application/xml", "application/json"}*/)
    public Mgni create(@Valid @RequestBody CreateMgniRequest request){
        return this.mgniService.createSettlementMargin(request);
    }

    @GetMapping(value = "/getList"/*, produces={"application/xml", "application/json"}*/)
    public MgniResponse getAllData(){
        if(this.mgniService.getData().isEmpty()){
            return new MgniResponse(null,"Nothing in Mgni.....");
        }
        return new MgniResponse(this.mgniService.getData(),"Success");
    }
    @PostMapping(value = "/getCashiList"/*, produces={"application/xml", "application/json"}*/)
    public CashiResponse getCashiData(@NotNull @RequestParam Integer page,@NotNull @RequestParam Integer size, @Valid @RequestBody CashiRequest request){
        if(this.mgniService.getCashi(request,page,size).isEmpty()){
            return new CashiResponse(null,"Nothing in Cashi...");
        }
        return new CashiResponse(this.mgniService.getCashi(request,page,size),"Success");
    }

    @PostMapping(value = "/getMgniList"/*, produces={"application/xml", "application/json"}*/)
    public MgniResponse getMgniData(@NotNull@RequestParam Integer page,@NotNull@RequestParam Integer size,@Valid @RequestBody MgniRequest request){
        if(this.mgniService.getMgni(request, page, size).isEmpty()){
            return new MgniResponse(null,"Nothing in Mgni....");
        }
        return new MgniResponse(this.mgniService.getMgni(request,page,size),"Success");
    }

    @PostMapping(value = "/update"/*, produces={"application/xml", "application/json"}*/)
    public UpdateResponse updateData(@Valid @RequestBody CreateMgniRequest request){
        try{
            return new UpdateResponse(this.mgniService.updateData(request),"Success");
        }catch (MgniNotFoundException e){
            return new UpdateResponse(null,"This id doesn't exist....");
        }
    }

    @DeleteMapping(value = "/delete")
    public DeleteResponse deleteData(@Valid @RequestBody DeleteRequest request){
        try {
            this.mgniService.deleteData(request);
            return new DeleteResponse("OK");
        }catch (MgniNotFoundException e){
            return new DeleteResponse("This id doesn't exist....");
        }
    }

}
