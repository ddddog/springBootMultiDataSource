package com.github.ddddog.springbootmultidatasource.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.ddddog.springbootmultidatasource.bus.entity.Stock;
import com.github.ddddog.springbootmultidatasource.service.LogService;
import com.github.ddddog.springbootmultidatasource.service.StockService;
import com.github.pagehelper.PageInfo;

@RestController
public class StockController {
	private Logger logger = LoggerFactory.getLogger(StockController.class);
    @Autowired
    StockService stockRepository;
    @Autowired
    LogService logService;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Autowired
    RedissonClient redissonClient;
    final static Integer id = 1;
    @RequestMapping("/addStock")
    public void addStock() {
        RLock lock = redissonClient.getLock("redisson:lock:stock:" + id);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                lock.lock();
                try {
                    Stock stock = stockRepository.findById(id);
                    stock.setNumber(stock.getNumber() + 1);
                    stockRepository.save(stock);
                    logService.info(JSON.toJSONString(stock));
                } finally {
                    lock.unlock();
                }
            });
        }
    }
    
    
    /** 
     * 测试插入 
     * @return 
     */  
    @RequestMapping("/add")  
    public void add(){  
    	for(int i=0;i<100;i++){
    		Stock stock = new Stock();
    		stock.setNumber((int)(Math.random()*100));
    		this.stockRepository.insert(stock); 
    	}
    }  
    /** 
     * 测试分页插件 
     * @return 
     */  
    @RequestMapping("/queryPage")   
    public void queryPage(){  
        PageInfo<Stock> page = this.stockRepository.queryPage("", 1, 2); 
        System.out.println("总页数=" + page.getPages());  
        System.out.println("总记录数=" + page.getTotal()) ;
        for(Stock u : page.getList()){
            System.out.println(u.getId() + " \t " + u.getNumber());  
        }
    }  
    /** 
     * 测试事务 
     * @return 
     */  
    @RequestMapping("/testTransational")   
    public void test(){  
        try {  
            this.stockRepository.testTransational();  
        } catch (Exception e) {  
            System.out.println(e.getMessage()); 
        }  
          
    }  
    @RequestMapping("/log")
    public void addLog() {
    	logService.info("testlog");
    }
}
