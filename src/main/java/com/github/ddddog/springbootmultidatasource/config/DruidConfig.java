package com.github.ddddog.springbootmultidatasource.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

public class DruidConfig {
	private Logger logger = LoggerFactory.getLogger(DruidConfig.class);

	@Value("${spring.datasource.druidLoginName}")
	private String druidLoginName;

	@Value("${spring.datasource.druidPassword}")
	private String druidPassword;
	// 配置文件
	@Autowired
	private Environment env;

	@Bean(name = "dataSource", destroyMethod = "close", initMethod = "init")
	// 默认为主数据源
	@Primary
	public DataSource getDataSource() throws Exception {
		// 此处不推荐使用实例化一个DruidDataSource的方式，进行数据源的配置，采用DruidDataSourceFactory的方式创建DataSource实例，原理分析可查看设计模式之工厂模式。
		Properties properties = new Properties();
		properties.put("driverClassName", env.getProperty("spring.datasource.driverClassName"));
		properties.put("url", env.getProperty("spring.datasource.url"));
		properties.put("username", env.getProperty("spring.datasource.username"));
		properties.put("password", env.getProperty("spring.datasource.password"));
		properties.put("initialSize", env.getProperty("spring.datasource.initialSize"));
		properties.put("minIdle", env.getProperty("spring.datasource.minIdle"));
		properties.put("maxActive", env.getProperty("spring.datasource.maxActive"));
		properties.put("maxWait", env.getProperty("spring.datasource.maxWait"));
		properties.put("timeBetweenEvictionRunsMillis",env.getProperty("spring.datasource.timeBetweenEvictionRunsMillis"));
		properties.put("minEvictableIdleTimeMillis", env.getProperty("spring.datasource.minEvictableIdleTimeMillis"));
		properties.put("validationQuery", env.getProperty("spring.datasource.validationQuery"));
		properties.put("filters", env.getProperty("spring.datasource.druid.sys.filters"));
		properties.put("validationQueryTimeout", env.getProperty("spring.datasource.validationQueryTimeout"));
		properties.put("testWhileIdle", env.getProperty("spring.datasource.testWhileIdle"));
		properties.put("testOnBorrow", env.getProperty("spring.datasource.testOnBorrow"));
		properties.put("testOnReturn", env.getProperty("spring.datasource.testOnReturn"));
		return DruidDataSourceFactory.createDataSource(properties);
	}

	///////// 下面是druid 监控访问的设置 /////////////////
	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*"); // url 匹配
		reg.addInitParameter("allow", "192.168.16.110,127.0.0.1"); // IP白名单
																	// (没有配置或者为空，则允许所有访问)
		reg.addInitParameter("deny", "192.168.16.111"); // IP黑名单
														// (存在共同时，deny优先于allow)
		reg.addInitParameter("loginUsername", this.druidLoginName);// 登录名
		reg.addInitParameter("loginPassword", this.druidPassword);// 登录密码
		reg.addInitParameter("resetEnable", "false"); // 禁用HTML页面上的“Reset All”功能
		return reg;
	}

	@Bean(name = "druidWebStatFilter")
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"); // 忽略资源
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
		filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
		return filterRegistrationBean;
	}
}
