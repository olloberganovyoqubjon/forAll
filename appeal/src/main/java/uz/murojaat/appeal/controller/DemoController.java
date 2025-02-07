package uz.murojaat.appeal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }
}
