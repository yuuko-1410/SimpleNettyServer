package com.yuuko.SimpleServer.config;

import com.yuuko.SimpleServer.anno.Login;
import com.yuuko.SimpleServer.anno.Pass;
import com.yuuko.SimpleServer.anno.RequestBody;
import com.yuuko.SimpleServer.utils.ParamUtils;
import com.yuuko.SimpleServer.utils.UrlUtils;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RouterConfig {
    private static final RouterConfig instance = new RouterConfig();
    private RouterConfig(){}
    public static RouterConfig getInstance(){
        return instance;
    }
    private final Map<String, Method> routerList = new HashMap<>();

    public DefaultFullHttpResponse routerSelector(FullHttpRequest request){
        long start = System.currentTimeMillis();//起始时间
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        String uri = UrlUtils.getBaseUrl(request.uri());
        Method method = routerList.get(uri);
        if (method != null){
            method.setAccessible(true);
            Class<?> clazz = method.getDeclaringClass();
            //执行权限认证
            Login login = clazz.getAnnotation(Login.class);
            Pass pass = method.getAnnotation(Pass.class);
            if (login != null && pass == null){
                BaseFilterConfig baseFilter = null;
                //获取接口的实现类
                baseFilter = (BaseFilterConfig) BeanConfig.getInstance()
                        .getInterfaceImplObj(BaseFilterConfig.class.getName());
                if (baseFilter != null){
                    boolean flag = baseFilter.filterConfig(request, response);
                    if (!flag){
                        DefaultFullHttpResponse baseFilterResponse = baseFilter.getFilterResponse(request, response);
                        //如果自定义了Response就返回自定义的，反之返回预设的
                        if (baseFilterResponse!= null) {
                            return baseFilterResponse;
                        }
                        log.error("[{}]\t[{}]ms\tNoLogin",request.uri(),(System.currentTimeMillis()-start));
                        response.content().writeCharSequence("未登录",StandardCharsets.UTF_8);
                        return response;
                    }
                }
            }

            try {
                Object o = clazz.getConstructor().newInstance();
                Annotation[][] anno = method.getParameterAnnotations();
                //请求参数
                Object params = null;
                //用于判断采用何种方式接收参数

                if (anno[0][0].annotationType() == RequestBody.class){
                    Type[] types = method.getGenericParameterTypes();
                    String clazzName = types[0].getTypeName();
                    Class<?> aClass = Class.forName(clazzName);
                    //POST与GET请求
                    if (request.getMethod() == HttpMethod.GET){
                        params = ParamUtils.getBeanToMap(ParamUtils.getParams(request),aClass);
                    }else{
                        params = ParamUtils.getBeanToJson(request, aClass);
                    }
                }
                String rsp = (String) method.invoke(o,params);
                response.content().writeCharSequence(rsp, StandardCharsets.UTF_8);
                log.info("[{}]\t[{}]ms\tOK",request.uri(),(System.currentTimeMillis()-start));
                return response;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            log.error("[{}]\t[{}]ms\tNotFound",request.uri(),(System.currentTimeMillis()-start));
        }
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
    }
    public void put(String name, Method method){
        routerList.put(name,method);
    }

}
