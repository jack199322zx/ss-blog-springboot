package com.sstyle.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
@MapperScan("com.sstyle.server.mapper")//配置mybatis包扫描
public class SsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsServerApplication.class, args);
	}
}
