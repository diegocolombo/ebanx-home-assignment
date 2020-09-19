package com.colombo.ebanx.service;

import com.colombo.ebanx.dao.AccountDAO;
import com.colombo.ebanx.dto.EventDTO;
import com.colombo.ebanx.dto.EventResponseDTO;
import com.colombo.ebanx.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.util.Assert.isTrue;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
public class AccountServiceUnitTest {

    private static final String ORIGIN_ID = "1";
    private static final String DESTINATION_ID = "2";

    private final AccountDAO dao = Mockito.mock(AccountDAO.class);
    private final AccountService service = new AccountService(dao);

    @BeforeEach
    public void beforeEach() {
        reset(dao);
    }

    @Test
    public void shouldDepositIntoAnExistingAccount() {
        final Account account = new Account();
        account.setId(DESTINATION_ID);
        account.setBalance(BigDecimal.TEN);

        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.of(account));

        final EventDTO eventDTO = generateEventDTO(null, DESTINATION_ID, EventDTO.DEPOSIT_TYPE);
        final Optional<EventResponseDTO> optEventResponseDTO = service.executeEvent(eventDTO);

        isTrue(optEventResponseDTO.isPresent(), "should be present");
        isTrue(optEventResponseDTO.get().getDestination().equals(account), "destination account should be equals the dao returned object");
        isTrue(account.getBalance().equals(BigDecimal.valueOf(11)), "expected to be 11 but returned " + account.getBalance());
        verify(dao, Mockito.times(1)).save(account);
    }

    @Test
    public void shouldNotDepositIntoANonExistingAccount() {
        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.empty());
        final EventDTO eventDTO = generateEventDTO(null, DESTINATION_ID, EventDTO.DEPOSIT_TYPE);
        final Optional<EventResponseDTO> eventResponseDTO = service.executeEvent(eventDTO);
        isTrue(!eventResponseDTO.isPresent(), "shouldn't be present");
    }

    @Test
    public void shouldWithdrawFromAnExistingAccount() {
        final Account account = new Account();
        account.setId(DESTINATION_ID);
        account.setBalance(BigDecimal.TEN);

        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.of(account));

        final EventDTO eventDTO = generateEventDTO(DESTINATION_ID, null, EventDTO.WITHDRAW_TYPE);
        final Optional<EventResponseDTO> optEventResponseDTO = service.executeEvent(eventDTO);

        isTrue(optEventResponseDTO.isPresent(), "should be present");
        isTrue(optEventResponseDTO.get().getOrigin().equals(account), "destination account should be equals the dao returned object");
        isTrue(account.getBalance().equals(BigDecimal.valueOf(9)), "expected to be 9 but returned " + account.getBalance());
        verify(dao, Mockito.times(1)).save(account);
    }

    @Test
    public void shouldNotWithdrawFromAnNonExistingAccount() {
        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.empty());
        final EventDTO eventDTO = generateEventDTO(null, DESTINATION_ID, EventDTO.WITHDRAW_TYPE);
        final Optional<EventResponseDTO> eventResponseDTO = service.executeEvent(eventDTO);

        isTrue(!eventResponseDTO.isPresent(), "shouldn't be present");
    }

    @Test
    public void shouldTransferFromAnExistingAccountIntoAnotherExistingAccount() {
        final Account originAccount = new Account();
        originAccount.setId(ORIGIN_ID);
        originAccount.setBalance(BigDecimal.valueOf(15));

        final Account destinationAccount = new Account();
        destinationAccount.setId(DESTINATION_ID);
        destinationAccount.setBalance(BigDecimal.valueOf(3));

        when(dao.findById(ORIGIN_ID)).thenReturn(Optional.of(originAccount));
        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.of(destinationAccount));

        final EventDTO eventDTO = generateEventDTO(ORIGIN_ID, DESTINATION_ID, EventDTO.TRANSFER_TYPE);
        final Optional<EventResponseDTO> optEventResponseDTO = service.executeEvent(eventDTO);

        isTrue(optEventResponseDTO.isPresent(), "should be present");
        final EventResponseDTO eventResponseDTO = optEventResponseDTO.get();
        isTrue(eventResponseDTO.getOrigin().equals(originAccount), "origin account should be the same as returned from db");
        isTrue(eventResponseDTO.getDestination().equals(destinationAccount), "destination account should be the same as returned from db");
        isTrue(originAccount.getBalance().equals(BigDecimal.valueOf(14)), "origin balance expected to be 14 but returned " + originAccount.getBalance());
        isTrue(destinationAccount.getBalance().equals(BigDecimal.valueOf(4)), "origin balance expected to be 4 but returned " + destinationAccount.getBalance());
        verify(dao, Mockito.times(1)).save(originAccount);
        verify(dao, Mockito.times(1)).save(destinationAccount);
    }

    @Test
    public void shouldNotTransferFromAnExistingAccountIntoANonExistingOne() {
        final Account originAccount = new Account();
        originAccount.setId(ORIGIN_ID);
        originAccount.setBalance(BigDecimal.valueOf(15));

        when(dao.findById(ORIGIN_ID)).thenReturn(Optional.of(originAccount));
        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.empty());

        final EventDTO eventDTO = generateEventDTO(ORIGIN_ID, DESTINATION_ID, EventDTO.TRANSFER_TYPE);
        final Optional<EventResponseDTO> eventResponseDTO = service.executeEvent(eventDTO);

        isTrue(!eventResponseDTO.isPresent(), "shouldn't be present");
    }

    @Test
    public void shouldNotTransferFromANonExistingAccountIntoAnotherOne() {
        when(dao.findById(ORIGIN_ID)).thenReturn(Optional.empty());
        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.empty());

        final EventDTO eventDTO = generateEventDTO(ORIGIN_ID, DESTINATION_ID, EventDTO.TRANSFER_TYPE);
        final Optional<EventResponseDTO> eventResponseDTO = service.executeEvent(eventDTO);

        isTrue(!eventResponseDTO.isPresent(), "shouldn't be present");
    }

    @Test
    public void shouldNotTransferFromANonExistingAccountIntoAnExistingOne() {
        final Account destinationAccount = new Account();
        destinationAccount.setId(DESTINATION_ID);
        destinationAccount.setBalance(BigDecimal.valueOf(3));

        when(dao.findById(ORIGIN_ID)).thenReturn(Optional.empty());
        when(dao.findById(DESTINATION_ID)).thenReturn(Optional.of(destinationAccount));

        final EventDTO eventDTO = generateEventDTO(ORIGIN_ID, DESTINATION_ID, EventDTO.TRANSFER_TYPE);
        final Optional<EventResponseDTO> eventResponseDTO = service.executeEvent(eventDTO);

        isTrue(!eventResponseDTO.isPresent(), "shouldn't be present");
    }

    @Test
    public void shouldCallDaoRest() {
        service.reset();
        verify(dao, Mockito.times(1)).reset();
    }

    @Test
    public void shouldReturnAccountBalanceWhenAccountExists() {
        final Account account = new Account();
        account.setId(ORIGIN_ID);
        account.setBalance(BigDecimal.TEN);

        when(dao.findById(ORIGIN_ID)).thenReturn(Optional.of(account));

        final Optional<BigDecimal> amount = service.getAmount(ORIGIN_ID);
        isTrue(amount.isPresent(), "should return a value");
        isTrue(amount.get().equals(BigDecimal.TEN), "balance should be 10 but returned " + amount.get());
    }

    @Test
    public void shouldReturnEmptyWhenInvalidAccount() {
        when(dao.findById(ORIGIN_ID)).thenReturn(Optional.empty());

        final Optional<BigDecimal> amount = service.getAmount(ORIGIN_ID);
        isTrue(!amount.isPresent(), "shouldn't return a value");
    }

    private EventDTO generateEventDTO(final String originId, final String destinationId, final String eventType) {
        final EventDTO eventDTO = new EventDTO();
        eventDTO.setOrigin(originId);
        eventDTO.setDestination(destinationId);
        eventDTO.setAmount(BigDecimal.ONE);
        eventDTO.setType(eventType);
        return eventDTO;
    }

}
