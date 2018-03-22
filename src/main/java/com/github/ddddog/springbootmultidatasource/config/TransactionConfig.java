package com.github.ddddog.springbootmultidatasource.config;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.github.ddddog.springbootmultidatasource.bus.config.MyBatisBusConfig;
import com.github.ddddog.springbootmultidatasource.common.config.MyBatisCommonConfig;  
  
  
/** 
 * Created by :  Sorata   2017/6/27 0027 下午 1:38. 
 */  
@Configuration  
@EnableTransactionManagement  
@ConditionalOnBean({MyBatisBusConfig.class,MyBatisCommonConfig.class})  
public class TransactionConfig  implements TransactionManagementConfigurer{  
  
    @Resource  
    private MyBatisBusConfig myBatisBusConfig;  
  
    @Resource  
    private MyBatisCommonConfig myBatisCommonConfig;  

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager(){
		
		try {
			return myBatisBusConfig.busTransactionManager();
		} catch (Exception e) {
			return null;
		}
	}
  
}