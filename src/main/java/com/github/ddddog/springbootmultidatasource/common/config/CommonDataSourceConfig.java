package com.github.ddddog.springbootmultidatasource.common.config;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSourceFactory;  
  
@Configuration  
public class CommonDataSourceConfig implements EnvironmentAware {   
 
    private Environment env; 
    @Override   
    public void setEnvironment(Environment environment) {
      this.env = environment;  
    }   
    //注册dataSource   
    @Bean(name = "commonDataSource", destroyMethod = "close", initMethod = "init")
    public DataSource dataSource() throws Exception  {   
    	// 此处不推荐使用实例化一个DruidDataSource的方式，进行数据源的配置，采用DruidDataSourceFactory的方式创建DataSource实例，原理分析可查看设计模式之工厂模式。
    	Properties properties = new Properties();
		properties.put("driverClassName", env.getProperty("common.datasource.driverClassName"));
		properties.put("url", env.getProperty("common.datasource.url"));
		properties.put("username", env.getProperty("common.datasource.username"));
		properties.put("password", env.getProperty("common.datasource.password"));
		properties.put("initialSize", env.getProperty("common.datasource.initialSize"));
		properties.put("minIdle", env.getProperty("common.datasource.minIdle"));
		properties.put("maxActive", env.getProperty("common.datasource.maxActive"));
		properties.put("maxWait", env.getProperty("common.datasource.maxWait"));
		properties.put("timeBetweenEvictionRunsMillis",env.getProperty("common.datasource.timeBetweenEvictionRunsMillis"));
		properties.put("minEvictableIdleTimeMillis", env.getProperty("common.datasource.minEvictableIdleTimeMillis"));
		properties.put("validationQuery", env.getProperty("common.datasource.validationQuery"));
		properties.put("filters", env.getProperty("common.datasource.druid.sys.filters"));
		properties.put("validationQueryTimeout", env.getProperty("common.datasource.validationQueryTimeout"));
		properties.put("testWhileIdle", env.getProperty("common.datasource.testWhileIdle"));
		properties.put("testOnBorrow", env.getProperty("common.datasource.testOnBorrow"));
		properties.put("testOnReturn", env.getProperty("common.datasource.testOnReturn"));
		return DruidDataSourceFactory.createDataSource(properties);		
    }     
} 
