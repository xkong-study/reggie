package com.example.config;

import com.example.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport{
    protected void addResourceHandlers(ResourceHandlerRegistry registry){
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设计对象转换器，底层使用jackson将java对象转换成json对象
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的转换器对象追加到mvc集合框架的转换器合集中
        converters.add(0,messageConverter);
    }
}
