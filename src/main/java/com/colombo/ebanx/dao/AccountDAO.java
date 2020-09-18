package com.colombo.ebanx.dao;

import com.colombo.ebanx.model.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * Simulates a DAO, doing operations over a map
 *
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since v0.0.1 2020-09-18
 */
@Component
@ApplicationScope
public class AccountDAO {

    private static final Map<String, Account> ACCOUNTS = new WeakHashMap<>();

    public void reset() {
        ACCOUNTS.clear();
    }

    public void save(final Account account) {
        ACCOUNTS.put(account.getId(), account);
    }

    public Optional<Account> get(final String id) {
        return Optional.ofNullable(ACCOUNTS.get(id));
    }

}
