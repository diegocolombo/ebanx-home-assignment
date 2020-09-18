package com.colombo.ebanx.dto;

import java.math.BigDecimal;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
public class EventDTO {

    public static final String DEPOSIT_TYPE = "deposit";
    public static final String WITHDRAW_TYPE = "withdraw";
    public static final String TRANSFER_TYPE = "transfer";

    private String type;
    private String origin;
    private String destination;
    private BigDecimal amount;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }
}
