package com.github.ddddog.springbootmultidatasource.common.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageHelper;

@Configuration
@AutoConfigureAfter({CommonDataSourceConfig.class})
//@MapperScan(basePackages = "com.github.ddddog.springbootLearning.mapper.common", sqlSessionTemplateRef  = "commonSqlSessionTemplate")
//@EnableConfigurationProperties(MybatisProperties.class)
public class MyBatisCommonConfig implements EnvironmentAware{
    @Autowired
    //配置文件
    private Environment env;
    @Autowired
    //默认为配置文件中的数据源
    @Qualifier("commonDataSource")
    DataSource commonDataSource;
    
    //根据数据源创建sqlSessionFactory
    @Bean(name="commonSqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception{
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        //指定数据源
        factoryBean.setDataSource(commonDataSource);
        //指定封装类所在包
        factoryBean.setTypeAliasesPackage(env.getProperty("mybatis.common.typeAliasesPackage"));
        //指定mapper.xml文件所在
        Resource[] resource = new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.common.mapperLocations"));
        factoryBean.setMapperLocations(resource);
        
        //添加分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        factoryBean.setPlugins(new Interceptor[]{pageHelper});
        return factoryBean;
    }
    
    @Bean(name="commonSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("commonSqlSessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
  /*  //将要执行的sql进行日志打印(不想拦截，就把这方法注释掉)  
    @Bean  
    public SqlPrintInterceptor sqlPrintInterceptor(){  
        return new SqlPrintInterceptor();  
    }*/
    
 
    @Bean(name = "commonTransactionManager")  
    public DataSourceTransactionManager transactionManager() throws Exception{  
        return new DataSourceTransactionManager(commonDataSource);  
    }

	@Override
	public void setEnvironment(Environment arg0) {
		this.env = arg0;	
	}  
}
