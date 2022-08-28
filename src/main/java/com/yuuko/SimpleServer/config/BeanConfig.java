package com.yuuko.SimpleServer.config;

import java.util.HashMap;
import java.util.Map;

public class BeanConfig {
    private static final BeanConfig instance = new BeanConfig();
    private BeanConfig(){}
    public static BeanConfig getInstance(){
        return instance;
    }
    private final Map<String,Object> interfaceImplObj = new HashMap<>();

    public void putInterfaceImplObj(String path,Object obj) {
        interfaceImplObj.put(path,obj);
    }

    public Object getInterfaceImplObj(String path) {
        return interfaceImplObj.get(path);
    }

    public Map<String, Object> getInterfaceImplObj() {
        return interfaceImplObj;
    }
}
