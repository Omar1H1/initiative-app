package com.Initiative.app.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Initiative.app.enums.SectorsOfActivity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }

    @GetMapping("/sectors")
    public List<SectorsOfActivity> getSectors() {
        return Arrays.asList(SectorsOfActivity.values());
    }
}
