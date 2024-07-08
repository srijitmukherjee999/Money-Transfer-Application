package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;


/*public class JdbcTransferDaoTests extends BaseDaoTests{

    protected static final Transfer TRANSFER_1 = new Transfer(3001, 2, 2, 2003, 2002, 100.00);
    protected static final Transfer TRANSFER_2 = new Transfer(3002, 1, 1, 2003, 2001, 200.00);
    protected static final Transfer TRANSFER_3 = new Transfer(3003, 1, 3, 2001, 2002, 300.00);
    protected static final Transfer TRANSFER_4 = new Transfer(3004, 1, 2, 2002, 2003, 100.00);

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferById_given_invalid_id_returns_null() {
        Transfer actualTransfer = sut.getTransferById(-1);

        Assert.assertNull(actualTransfer);
    }

    @Test
    public void getTransferById_given_valid_id_returns_transfer() {
        Transfer actualTransfer = sut.getTransferById(TRANSFER_1.getId());

        Assert.assertEquals(TRANSFER_1, actualTransfer);

    }

    @Test
    public void getTransfers_returns_sent_or_recieved() {
        List<Transfer> transfers = sut.getListOfTransfersBySentOrReceived();

        Assert.assertNotNull(transfers);
        Assert.assertEquals(3, transfers.size());
        Assert.assertEquals(TRANSFER_1, transfers.get(0));
        Assert.assertEquals(TRANSFER_3, transfers.get(1));
        Assert.assertEquals(TRANSFER_4, transfers.get(2));

    }
    @Test
    public void getTransferByUserId_given_invalid_user_id_returns_null() {
        Transfer actualTransfer = sut.getTransferbyUserId(-1);

        Assert.assertNull(actualTransfer);
    }

    @Test
    public void getTransferByUserId_given_valid_user_id_returns_transfer() {
        Transfer actualTransfer = sut.getTransferbyUserId(TRANSFER_1.getId());

        Assert.assertEquals(TRANSFER_1, actualTransfer);
    }

    @Test(expected = DaoException.class)
    public void createTransfer_with_null_username() {
        Transfer newTransfer = new Transfer();
        newTransfer.setUsernameFrom(null);
        newTransfer.setUsernameTo(null);
        sut.createTransferByUserName(newTransfer, newTransfer);
    }

    @Test(expected = DaoException.class)
    public void createTransfer_with_existing_username() {
        Transfer newTransfer = new Transfer();
        newTransfer.setUsernameFrom(TRANSFER_1.getUsername());
        newTransfer.setUsernameTo(TRANSFER_2.getUsername);

        sut.createTransferByUserName(newTransfer, newTransfer);
    }

    @Test
    public void getTransfers_returns_pending_transfers() {
        List<Transfer> transfers = sut.getListOfTransfersByPending();

        Assert.assertNotNull(transfers);
        Assert.assertEquals(1, transfers.size());
        Assert.assertEquals(TRANSFER_2, transfers.get(0));
    }
    @Test
    public void getTransfers_returns_transfers_to_approve_or_reject() {
        List<Transfer> transfers = sut.getListOfTransfersToApproveOrReject();

        Assert.assertNotNull(transfers);
        Assert.assertEquals(1, transfers.size());
        Assert.assertEquals(TRANSFER_2, transfers.get(0));
    }

    @Test
    public void updateTransfer() {
        Transfer transferToUpdate = sut.getTransferById(TRANSFER_2.getId());

        transferToUpdate.setTransferStatusId(2);

        Transfer updatedTransfer = sut.updateTransfer(transferToUpdate);
        Assert.assertNotNull(updatedTransfer);

        Transfer retrievedTransfer = sut.getTransferById(TRANSFER_2.getId());
        Assert.assertEquals(transferToUpdate, retrievedTransfer);
    }
}
*/

