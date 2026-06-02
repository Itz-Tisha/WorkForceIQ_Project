package com.example.WorkforceIQ.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.WorkforceIQ.Service.PromotionService;
import com.example.WorkforceIQ.dto.PromotionRequest;
import com.example.WorkforceIQ.entity.Employee;

@RestController
@RequestMapping("/promotion")
@CrossOrigin("*")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/eligible")
    public List<Employee> getEligibleEmployees() {

        return promotionService.getEligibleEmployees();
    }

    @PutMapping("/{id}")
    public Employee promoteEmployee(
            @PathVariable Long id,
            @RequestBody PromotionRequest request) {

        return promotionService.promoteEmployee(id, request);
    }
}