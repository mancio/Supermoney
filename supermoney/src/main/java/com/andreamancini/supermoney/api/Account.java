package com.andreamancini.supermoney.api;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Account {
	
	private static final AtomicInteger COUNTER = new AtomicInteger();

    private final int id;

    private String name;
    
    private String user;

    //is more precise than double
    private BigDecimal money;

    

    public Account(String name, String user, BigDecimal money) {
        this.id = COUNTER.getAndIncrement();
        this.name = name;
        this.user = user;
        this.money = money;
        
    }
    
    public Account() {
        this.id = COUNTER.getAndIncrement();
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getmoney() {
        return money;
    }

    public void setBalance(BigDecimal money) {
        this.money = money;
    }
    
    public void sub(BigDecimal amount) {
        this.money = money.subtract(amount);
    }

    public void add(BigDecimal amount) {
        this.money = money.add(amount);
    }

    

}
