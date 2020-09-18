package com.colombo.ebanx.dto;

import com.colombo.ebanx.model.Account;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
public class EventResponseDTO {

    private Account origin;
    private Account destination;

    public EventResponseDTO() {
    }

    public EventResponseDTO(final Account origin, final Account destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Account getOrigin() {
        return origin;
    }

    public void setOrigin(final Account origin) {
        this.origin = origin;
    }

    public Account getDestination() {
        return destination;
    }

    public void setDestination(final Account destination) {
        this.destination = destination;
    }
}
