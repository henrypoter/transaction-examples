package transactions.jpa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jpa-context.xml"})
@EnableTransactionManagement
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class IsolationLevelTest {

    @Autowired
    private JpaBookingService bookingService;

    private volatile Boolean hasException;

    @Before
    public void before() {
        hasException = false;
    }

    @Test
    public void givenInsertingWhenDefaultIsolationIsActiveThenNoExceptionShouldBeThrown() {
        bookingService.insertBookings("User1", "User2", "User3");
        Assert.assertEquals(3, bookingService.countAllBookings());
    }

    @Test
    public void givenInsertingWhenMethodWithReadUncommitedIsolationLevelCalledThenExceptionShouldBeThrown() throws InterruptedException {
        startConcurrentCheckerReadUncommited();
        bookingService.insertBookings("User1", "User2", "User3");
        while (hasException == null)
            ;
        System.out.println(hasException);
        Assert.assertTrue(hasException);
    }

    private void startConcurrentCheckerReadUncommited() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (hasException) {
                    try {
                        long size = bookingService.countAllBookingsReadUncommited();
                        while (size < 3) {
                            size = bookingService.countAllBookingsReadUncommited();
                        }
                    } catch (InvalidIsolationLevelException e) {
                        hasException = true;
                    } finally {
                        if (hasException == null) {
                            hasException = false;
                        }
                    }
                }
            }
        }).start();
    }

}
