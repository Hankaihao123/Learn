package com.hkh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Data;

/*
 * SpringBoot对静态资源的访问
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "project")
public class WebMvcConfig implements WebMvcConfigurer {

	private String ueditorvideo;
	private String ueditorimage;
	private String ueditorfile;
	private String headimageurl;
	private String ueditorpath;
	private String workflow;
	private String activiti;
	private String editorapp;
	private String WorkFlowDefineImage;

	public String addfile(String arg) {
		return "file:" + arg;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/image/**").addResourceLocations(addfile(ueditorimage));
		registry.addResourceHandler("/video/**").addResourceLocations(addfile(ueditorvideo));
		registry.addResourceHandler("/file/**").addResourceLocations(addfile(ueditorfile));
		registry.addResourceHandler("/headimage/**").addResourceLocations(addfile(headimageurl));
		registry.addResourceHandler("/ueditor/**").addResourceLocations(ueditorpath);
		registry.addResourceHandler("/workflow/**").addResourceLocations(addfile(workflow));
		registry.addResourceHandler("/WorkFlowDefineImage/**").addResourceLocations(addfile(WorkFlowDefineImage));
		registry.addResourceHandler("/activiti/**").addResourceLocations(activiti);
		registry.addResourceHandler("/editor-app/**").addResourceLocations(editorapp);
	}

}
