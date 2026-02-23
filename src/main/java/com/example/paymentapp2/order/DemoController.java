package com.example.paymentapp2.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final ManualJpaService manualJpaService;

    @GetMapping
    public String runDemo() {
        manualJpaService.demoFlow();
        return "Done";
    }
}
