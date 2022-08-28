package com.yuuko.SimpleServer.config;

import com.yuuko.SimpleServer.anno.Config;
import com.yuuko.SimpleServer.anno.GetMapping;
import com.yuuko.SimpleServer.anno.PostMapping;
import com.yuuko.SimpleServer.anno.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class AnnoConfig {
    private static final AnnoConfig instance = new AnnoConfig();
    private AnnoConfig(){}
    public static AnnoConfig getInstance(){
        return instance;
    }
    public void initAnno(List<String> beanList){
        for (String name : beanList) {
            try {
                Class<?> aClass = Class.forName(name);
                //获取所有类中的配置类，实现配置的自动注入
                Config config = aClass.getAnnotation(Config.class);
                if (config != null){
                    Type[] interfaces = aClass.getGenericInterfaces();
                    for (Type anInterface : interfaces) {
                        BeanConfig.getInstance().putInterfaceImplObj(anInterface.getTypeName(),
                                aClass.getConstructor().newInstance());
                    }
                }
                RestController annotation = aClass.getAnnotation(RestController.class);
                if (annotation == null){
                    continue;
                }
                Method[] methods = aClass.getDeclaredMethods();
                for (Method method : methods) {
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    //根据注解判断是Post还是Get请求
                    if(postMapping != null){
                        RouterConfig.getInstance().put(postMapping.value(),method);
                    }
                    if(getMapping != null){
                        RouterConfig.getInstance().put(getMapping.value(),method);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public void annoRestController(){

    }
    public void annoGetMapping(Method method){
        try {
            method.setAccessible(true);
            Class<?> declaringClass = method.getDeclaringClass();
            Object o = declaringClass.newInstance();
            Object invoke = method.invoke(o);
            System.out.println(invoke);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    public void annoPostMapping(Method method, Class<?> aClass){

    }
}
