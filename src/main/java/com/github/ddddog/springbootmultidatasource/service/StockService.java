package com.github.ddddog.springbootmultidatasource.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.ddddog.springbootmultidatasource.bus.entity.Stock;
import com.github.ddddog.springbootmultidatasource.mapper.bus.StockMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service  
@Transactional(readOnly=true,rollbackFor=Exception.class) 
public class StockService {
	@Autowired
	private StockMapper stockMapper;
	
	public Stock findById(Integer id){
		return this.stockMapper.selectByPrimaryKey(id);
	}
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public  void save(Stock stock){
		this.stockMapper.updateByPrimaryKey(stock);
	}
	
	//注意：方法的@Transactional会覆盖类上面声明的事务  
    //Propagation.REQUIRED ：有事务就处于当前事务中，没事务就创建一个事务  
    //isolation=Isolation.DEFAULT：事务数据库的默认隔离级别  
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)
	public void insert (Stock stock){
		this.stockMapper.insert(stock);
	}
    
    public PageInfo<Stock> queryPage(String number,int pageNum,int pageSize){  
        Page<Stock> page = PageHelper.startPage(pageNum, pageSize);  
        //PageHelper会自动拦截到下面这查询sql
        Stock stock =new Stock();
        if(StringUtils.isNotBlank(number)){
        	//stock.setNumber(number);
        }	
        this.stockMapper.select(stock);  
        return page.toPageInfo();  
    }
    
    //测试事务  
    //注意：方法的@Transactional会覆盖类上面声明的事务  
    //Propagation.REQUIRED ：有事务就处于当前事务中，没事务就创建一个事务  
    //isolation=Isolation.DEFAULT：事务数据库的默认隔离级别  
    @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.DEFAULT,readOnly=false)  
    public void testTransational(){  
        //制造异常  
        //如果类上面没有@Transactional,方法上也没有，哪怕throw new RuntimeException,数据库也会提交  
    	this.stockMapper.deleteByPrimaryKey("1");
        throw new RuntimeException("事务异常测试");  
    }  

}
