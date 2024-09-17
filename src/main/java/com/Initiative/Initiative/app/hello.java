package com.Initiative.Initiative.app;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class hello {
    @GetMapping("/api/hello")
    public String helloUser() {
        return "Hello, the time at the server is now " + new Date() + "\n";
    }
}