package com.colombo.ebanx.model;

import java.math.BigDecimal;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
public class Account {

    private String id;
    private BigDecimal balance;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }
}
