package com.colombo.ebanx.controller;

import com.colombo.ebanx.dto.EventDTO;
import com.colombo.ebanx.dto.EventResponseDTO;
import com.colombo.ebanx.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
@RestController
public class AccountController {

    public static final ResponseEntity<String> NOT_FOUND_RESPONSE = ResponseEntity.status(HttpStatus.NOT_FOUND).body("0");
    private final AccountService service;

    @Autowired
    public AccountController(final AccountService service) {
        this.service = service;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> reset() {
        service.reset();
        return ResponseEntity.ok("OK");
    }

    @PostMapping(
            value = "/event",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> postEvent(@RequestBody final EventDTO eventDTO) {
        final Optional<EventResponseDTO> eventResponseDTO = service.executeEvent(eventDTO);
        if (eventResponseDTO.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(eventResponseDTO.get());
        } else {
            return NOT_FOUND_RESPONSE;
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance(@RequestParam("account_id") final String accountId) {
        return service.getAmount(accountId)
                .map(BigDecimal::toString)
                .map(ResponseEntity::ok)
                .orElse(NOT_FOUND_RESPONSE);
    }

}
