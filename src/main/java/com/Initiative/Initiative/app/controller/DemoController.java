package com.Initiative.Initiative.app.controller;

import java.util.HashMap;
import java.util.Map;

import com.Initiative.Initiative.app.auth.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/public")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public Map <String, String> sayHello() {
    HashMap<String, String> map = new HashMap<>();
    map.put("msg", "hello from Springboot");
        return map;
    }

    @GetMapping("/secure")
    public ResponseEntity<String> sayHelloFromSecure() {
       return ResponseEntity.ok("Hello From Spring secure endpoint");
    }
}
