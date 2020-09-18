package com.colombo.ebanx.dao;

import com.colombo.ebanx.model.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.util.Assert.isTrue;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
public class AccountDAOUnitTest {

    private final AccountDAO accountDAO = new AccountDAO();

    @Test
    public void shouldSaveAndRetrieveAnAccount() {
        final Account account = new Account();
        account.setId("1");
        account.setBalance(BigDecimal.TEN);
        accountDAO.save(account);
        final Optional<Account> optAccount = accountDAO.get(account.getId());
        isTrue(optAccount.isPresent(), "account was not saved");
        isTrue(optAccount.get().equals(account), "the returned account is not the same saved");
    }

    @Test
    public void shouldClearDatabase() {
        final Account account = new Account();
        account.setId("1");
        account.setBalance(BigDecimal.TEN);
        accountDAO.save(account);
        accountDAO.reset();
        isTrue(!accountDAO.get(account.getId()).isPresent(), "db was not cleared");
    }

}
