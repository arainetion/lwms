package com.hy.lwmsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.hy.lwmsbackend.sys.mapper")
@EnableTransactionManagement
public class LwmsBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(LwmsBackEndApplication.class, args);
	}

}
