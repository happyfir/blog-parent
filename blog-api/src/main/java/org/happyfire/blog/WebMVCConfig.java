package org.happyfire.blog;

import org.happyfire.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.xml.ws.Action;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        //跨域配置
//        registry.addMapping("/**").allowedOrigins("http://127.0.0.1:8080");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //TODO 后续需要是再配置
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }
}
