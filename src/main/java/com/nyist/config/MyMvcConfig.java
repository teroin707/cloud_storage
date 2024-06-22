package com.nyist.config;

import com.nyist.utils.FileTypeUtils;
import com.nyist.utils.MailTask;
import com.nyist.utils.Md5Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    //设置文件虚拟路径映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/yun/avatar/**").addResourceLocations("file:D:\\yunfile\\avatar\\");
        registry.addResourceHandler("/yun/file/**").addResourceLocations("file:D:\\yunfile\\files\\");
    }

    //设置视图跳转
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //访问首页跳转
        registry.addViewController("/").setViewName("loginandreg");
        registry.addViewController("/loginandreg.html").setViewName("loginandreg");
    }
    //拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置公开的资源
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/","/css/**","/js/**","/img/**","/icon/**","/layui/**",
                        "/loginandreg.html","/loginandreg","loginandreg","/user/loginandreg",
                        "/user/login","/user/getmailcode");
    }


    //将使用的工具类注册到容器中（也可通过注解注册）
    @Bean
    public MailTask getMailCode(){
        return new MailTask();
    }
    @Bean
    public Md5Utils md5Utils(){
        return new Md5Utils();
    }
    @Bean
    public FileTypeUtils fileTypeUtils(){
        return new FileTypeUtils();
    }

}
