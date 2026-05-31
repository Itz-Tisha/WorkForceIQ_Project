package com.example.WorkforceIQ.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.WorkforceIQ.Service.DepartmentAnalysisService;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/ai")
public class AIAnalysisController {

    @Autowired
    private DepartmentAnalysisService service;

    @GetMapping("/departments")
    public Map<String, Object> analyze() {
        return service.analyzeDepartments();
    }
}