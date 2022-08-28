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
        return user.getUsername();
    }
    @GetMapping("/add")
    String getPassword(@RequestBody Map user){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user.toString();
    }
}
