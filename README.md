# SimpleNettyServer 一个基于Netty的简易Http服务器框架
目前已实现的基础功能：
- Get/Post请求参数获取
- 基于注解的基础版拦截器
- 基础版功能的自动配置
### 入门：
> 启动服务器：

```java
//扫描路径
@ScannerPath("com.yuuko.test")
public class App {
    public static void main(String[] args) {
        SimpleServerBootstrap.run(App.class,2333);
    }
}
```

> HelloWorld:
```java
@RestController
public class BaseController {
    @GetMapping("/hello")
    String getName(@RequestBody Map map){
        return "Hello World";
    }
}
```
在浏览器输入``localhost:端口/hello``浏览器正常输出Hello World
> 请求参数获取：

在接口参数中使用``@RequestBody``注解，支持Map类型与实体类类型的接收
```java
@GetMapping("/login")
String login(@RequestBody User user){
    String xxx = user.getUsername;
    String xxx = user.getPassword;
    return xxx;
}
```
> 拦截器的基础使用

在需要登陆的Controller上使用``@Login``注解则整个Controller上经过的请求将会被拦截器拦截。在使用了``@Login``注解的Controller中，在接口上添加``@Pass``注解，经过的请求将会被拦截器忽略

配置拦截器：

```java
//使用@Config启用该配置
@Config
public class MyFilterConfig implements BaseFilterConfig {
    @Override
    public boolean filterConfig(FullHttpRequest request, DefaultFullHttpResponse response) {
        //返回true则该请求将顺利通过，返回flase将被拦截
        return false;
    }

    @Override
    public DefaultFullHttpResponse getFilterResponse(FullHttpRequest request, DefaultFullHttpResponse response) {
        //拦截后可配置自定义响应
        return null;
    }
}
```

tips:目前仅支持Get请求参数与Post表单与Json格式的请求参数，其他类型正在支持


