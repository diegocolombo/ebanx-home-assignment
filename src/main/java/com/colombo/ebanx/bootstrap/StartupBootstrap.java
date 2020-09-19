package com.colombo.ebanx.bootstrap;

import com.colombo.ebanx.dao.AccountDAO;
import com.colombo.ebanx.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Diego Colombo <diego.colombo@wssim.com.br>
 * @since 0.0.1 2020-09-18
 */
@Component
public class StartupBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final AccountDAO accountDAO;

    @Autowired
    public StartupBootstrap(final AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        createAccounts();
    }

    /**
     * Creates the test accounts
     */
    private void createAccounts() {
        accountDAO.reset();
    }
}
