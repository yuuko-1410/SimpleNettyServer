package com.yuuko.test;
import com.yuuko.SimpleServer.anno.ScannerPath;
import com.yuuko.SimpleServer.init.SimpleServerBootstrap;

@ScannerPath("com.yuuko.test")
public class App {
    public static void main(String[] args) {
        SimpleServerBootstrap.run(App.class,2333);
    }
}
