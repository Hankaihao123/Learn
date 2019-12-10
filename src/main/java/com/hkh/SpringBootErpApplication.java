package com.hkh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = { "com.hkh.sys.dao" })
@SpringBootApplication
public class SpringBootErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootErpApplication.class, args);
	}

}
