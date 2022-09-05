package com.example.project.controller;

import com.example.project.controller.dto.request.CreateHcmioRequest;
import com.example.project.controller.dto.request.CreateMstmbRequest;
import com.example.project.controller.dto.resopnse.StatusResponse;
import com.example.project.model.entity.Mstmb;
import com.example.project.service.MstmbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mstmb")
public class MstmbController {

    @Autowired
    MstmbService mstmbService;

    @GetMapping()
    public List<Mstmb> getDataList(){
        return this.mstmbService.getMstmbList();
    }

    @GetMapping("/{stock}")
    public List<Mstmb> getData(@PathVariable String stock){
        return this.mstmbService.getDataByStock(stock);
    }

    @PostMapping()
    public StatusResponse createData(@RequestBody CreateMstmbRequest request) {
        String userCreate = this.mstmbService.createMstmb(request);
        return new StatusResponse(userCreate);
    }
    @DeleteMapping("/{stock}")
    public StatusResponse deleteData(@PathVariable String stock) {
        String userDelete = this.mstmbService.deleteMstmb(stock);
        return new StatusResponse(userDelete);
    }


}
