package com.example.project.controller;


import com.example.project.controller.dto.request.CreateHcmioRequest;
import com.example.project.controller.dto.resopnse.StatusResponse;
import com.example.project.model.entity.Hcmio;
import com.example.project.service.HcmioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hcmio")
public class HcmioController {

    @Autowired
    HcmioService hcmioService;

    @GetMapping()
    public List<Hcmio> getDataList(){
        List<Hcmio> dataList = this.hcmioService.getHcmioList();
        return dataList;
    }

    @GetMapping("/date/branch/cust/{doc}")
    public List<Hcmio> getData(@RequestParam String date,@RequestParam String branch,@RequestParam String cust,@PathVariable String doc){
        List<Hcmio> data = this.hcmioService.getDataByDocSeq(date,branch,cust,doc);
        return data;
    }

    @PostMapping()
    public StatusResponse createData(@RequestBody CreateHcmioRequest request) {
        String userCreate = this.hcmioService.createHcmio(request);
        return new StatusResponse(userCreate);
    }

    @DeleteMapping("/{docSeq}")
    public StatusResponse deleteData(@PathVariable String docSeq) {
        String userDelete = this.hcmioService.deleteHcmio(docSeq);
        return new StatusResponse(userDelete);
    }



}
