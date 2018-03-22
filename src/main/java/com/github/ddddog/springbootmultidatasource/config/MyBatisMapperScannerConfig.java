package com.github.ddddog.springbootmultidatasource.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.github.ddddog.springbootmultidatasource.bus.config.MyBatisBusConfig;
import com.github.ddddog.springbootmultidatasource.common.config.MyBatisCommonConfig;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
// 必须在MyBatisConfig注册后再加载MapperScannerConfigurer，否则会报错
@AutoConfigureAfter({MyBatisBusConfig.class,MyBatisCommonConfig.class})
public class MyBatisMapperScannerConfig implements EnvironmentAware{
	
	private Environment env;
	
	@Bean
	public MapperScannerConfigurer mapperScannerBusConfigurer() {
		
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setSqlSessionTemplateBeanName("busSqlSessionTemplate");
		mapperScannerConfigurer.setBasePackage(env.getProperty("mapper.bus.basePackage"));
		
		// 初始化扫描器的相关配置，这里我们要创建一个Mapper的父类
		Properties properties = new Properties();
		properties.setProperty("mappers", env.getProperty("mapper.bus.mappers"));
		properties.setProperty("notEmpty", env.getProperty("mapper.bus.not-empty"));
		properties.setProperty("IDENTITY", env.getProperty("mapper.bus.identity"));

		mapperScannerConfigurer.setProperties(properties);

		return mapperScannerConfigurer;
	}
	
	@Bean
	public MapperScannerConfigurer mapperScannerCommonConfigurer() {
		
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("commonSqlSessionFactory");
		mapperScannerConfigurer.setSqlSessionTemplateBeanName("commonSqlSessionTemplate");
		mapperScannerConfigurer.setBasePackage(env.getProperty("mapper.common.basePackage"));
		
		// 初始化扫描器的相关配置，这里我们要创建一个Mapper的父类
		Properties properties = new Properties();
		properties.setProperty("mappers", env.getProperty("mapper.common.mappers"));
		properties.setProperty("notEmpty", env.getProperty("mapper.common.not-empty"));
		properties.setProperty("IDENTITY", env.getProperty("mapper.common.identity"));


		mapperScannerConfigurer.setProperties(properties);

		return mapperScannerConfigurer;
	}

	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
	}

}
