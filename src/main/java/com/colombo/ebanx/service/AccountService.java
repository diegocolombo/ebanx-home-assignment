package com.colombo.ebanx.service;

import com.colombo.ebanx.dao.AccountDAO;
import com.colombo.ebanx.dto.EventDTO;
import com.colombo.ebanx.dto.EventResponseDTO;
import com.colombo.ebanx.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
@Component
@ApplicationScope
public class AccountService {

    private final AccountDAO dao;

    @Autowired
    public AccountService(final AccountDAO dao) {
        this.dao = dao;
    }

    public Optional<EventResponseDTO> executeEvent(final EventDTO event) {
        switch (event.getType().toLowerCase()) {
            case EventDTO.DEPOSIT_TYPE: return executeDeposit(event);
            case EventDTO.WITHDRAW_TYPE: return executeWithdraw(event);
            case EventDTO.TRANSFER_TYPE: return executeTransfer(event);
        }
        return Optional.empty();
    }

    public Optional<BigDecimal> getAmount(final String id) {
        return dao.findById(id).map(Account::getBalance);
    }

    public void reset() {
        dao.reset();
    }

    private Optional<EventResponseDTO> executeDeposit(final EventDTO eventDTO) {
        return executeDestinationAction(eventDTO.getDestination(), account -> account.deposit(eventDTO.getAmount()));
    }

    private Optional<EventResponseDTO> executeWithdraw(final EventDTO eventDTO) {
        return executeDestinationAction(eventDTO.getDestination(), account -> account.withdraw(eventDTO.getAmount()));
    }

    private Optional<EventResponseDTO> executeTransfer(final EventDTO eventDTO) {
        final Optional<Account> optOrigin = dao.findById(eventDTO.getOrigin());
        final Optional<Account> optDestination = dao.findById(eventDTO.getDestination());

        if (!optOrigin.isPresent() || !optDestination.isPresent()) {
            return Optional.empty();
        }

        final Account originAccount = optOrigin.get();
        final Account destinationAccount = optDestination.get();

        originAccount.transfer(destinationAccount, eventDTO.getAmount());
        dao.save(originAccount);
        dao.save(destinationAccount);

        return Optional.of(new EventResponseDTO(originAccount, destinationAccount));
    }

    private Optional<EventResponseDTO> executeDestinationAction(final String destination,
                                                                final Consumer<Account> consumer) {
        return dao.findById(destination)
                .map(account -> {
                    consumer.accept(account);
                    dao.save(account);
                    final EventResponseDTO eventResponseDTO = new EventResponseDTO();
                    eventResponseDTO.setDestination(account);
                    return eventResponseDTO;
                });
    }

}
