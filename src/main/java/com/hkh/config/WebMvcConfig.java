package com.hkh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * SpringBoot对静态资源的访问
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/image/**").addResourceLocations("file:D://upload//image//");
		registry.addResourceHandler("/video/**").addResourceLocations("file:D://upload//video//");
		registry.addResourceHandler("/file/**").addResourceLocations("file:D://upload//file//");
		registry.addResourceHandler("/ueditor/**").addResourceLocations("classpath:static//resource//lib//ueditor//");
	}

}
