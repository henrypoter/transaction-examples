package transactions.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import util.SleepUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jdbc-context.xml"})
@EnableTransactionManagement
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class IsolationLevelTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IsolationLevelTest.class);

    @Autowired
    private JDBCBookingService bookingService;

    private boolean readUnCommitted;

    @Test
    public void givenInsertingWhenNoReadingUncomittedMethodIsUsedThenReadUnComittedShouldBeFalse() {
        startConcurrentChecker(false);
        bookingService.insertBookings("book1", "book2", "book3");
        Assert.assertEquals(3, bookingService.countAllBookings());
        Assert.assertFalse(readUnCommitted);
    }

    @Test
    public void givenInsertingWhenReadingUncomittedMethodIsUsedThenReadUnComittedShouldBeTrue() {
        startConcurrentChecker(true);
        bookingService.insertBookings("book1", "book2", "book3");
        Assert.assertEquals(3, bookingService.countAllBookings());
        Assert.assertTrue(readUnCommitted);
    }

    @Test
    public void givenInsertingWhenRepeatableReadIsUsedThenRepeatableReadShouldGiveDifferentResult() {
        startConcurrentInsert();
        int difference = bookingService.differenceBetweenFirstAndSeconFindRepeatableRead();
        Assert.assertEquals(-1, difference);
    }

    @Test
    public void givenInsertingWhenSerializableReadIsUsedThenRepeatableReadShouldGiveSameResult() {
        startConcurrentInsert();
        int difference = bookingService.differenceBetweenFirstAndSeconFindSerializable();
        Assert.assertEquals(0, difference);
    }

    private void startConcurrentChecker(final boolean checkReadUnCommitted) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = countAllBookings(checkReadUnCommitted);
                while (size < 3) {
                    size = countAllBookings(checkReadUnCommitted);
                    LOGGER.info("In thread:" + size);
                    if (0 < size && size < 3) {
                        readUnCommitted = true;
                    }
                }
            }
        }).start();
    }

    private int countAllBookings(final boolean checkReadUnCommitted) {
        return checkReadUnCommitted ? bookingService.countAllBookingsReadUnCommitted() : bookingService.countAllBookings();
    }

    private void startConcurrentInsert() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SleepUtil.sleep(1000);
                LOGGER.info("insert running");
                bookingService.insertBookings("book1");
            }
        }).start();
    }

}
