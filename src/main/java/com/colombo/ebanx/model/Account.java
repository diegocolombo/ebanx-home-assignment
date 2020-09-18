package com.colombo.ebanx.model;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
public class Account {

    private String id;
    private BigDecimal balance;

    public Account() {
    }

    public Account(final String id) {
        this.id = id;
    }

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

    public void deposit(final BigDecimal amount) {
        balance = Optional.ofNullable(balance).map(amount::add).orElse(amount);
    }

    public void withdraw(final BigDecimal amount) {
        balance = Optional.ofNullable(balance).map(b -> b.subtract(amount)).orElse(amount.negate());
    }

    public void transfer(final Account destination, final BigDecimal amount) {
        withdraw(amount);
        destination.deposit(amount);
    }
}
