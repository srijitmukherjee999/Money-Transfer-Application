package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

/*public class JdbcAccountDaoTests extends BaseDaoTests{

    protected static final Account ACCOUNT_1 = new Account(2001, 1001, 1000.00);
    protected static final Account ACCOUNT_2 = new Account(2002, 1002, 500.00);
    protected static final Account ACCOUNT_3 = new Account(2003, 1003, 700.00);

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAccountBalanceByUsername_given_null_throws_exception() {
        sut.getAccountBalanceByUsername(null);
    }
    @Test
    public void getAccountBalanceByUsername_given_invalid_username_returns_null() {
        Assert.assertNull(sut.getAccountBalanceByUsername("invalid"));
    }

    @Test
    public void getAccountBalanceByUsername_given_valid_user_returns_user() {
        Account actualAccount = sut.getAccountBalanceByUsername(ACCOUNT_1.getUserName());

        Assert.assertEquals(ACCOUNT_1, actualAccount);
    }

    @Test
    public void getAccountByUserId_given_invalid_user_id_returns_null() {
        Account actualAccount = sut.getAccountbyUserId(-1);

        Assert.assertNull(actualAccount);
    }

    @Test
    public void getAccountByUserId_given_valid_user_id_returns_account() {
        Account actualUser = sut.getAccountbyUserId(ACCOUNT_1.getId());

        Assert.assertEquals(ACCOUNT_1, actualUser);
    }

    @Test
    public void updateAccountBalance() {
        Account accountToUpdate = sut.getId(1002);

        accountToUpdate.setBalance(600.00);
        Account output = sut.updateAccountBalance(accountToUpdate);
        Assert.assertNotNull(output);

        Account retrievedAccount = sut.getId(1002);
        assertAccountsMatch(output, retrievedAccount);
    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
*/