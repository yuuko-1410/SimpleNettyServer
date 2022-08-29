package com.yuuko.test.controller;

import com.yuuko.SimpleServer.anno.*;
import com.yuuko.test.pojo.User;
import java.util.Map;

@Login
@RestController
public class BaseController {

    @Pass
    @PostMapping("/hello")
    String getName(@RequestBody User user){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user.getUsername();
    }

    @Pass
    @GetMapping("/add")
    String getPassword(@RequestBody Map params){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return params.toString();
    }
}
