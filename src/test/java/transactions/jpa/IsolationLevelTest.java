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

import util.SleepUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jpa-context.xml" })
@EnableTransactionManagement
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class IsolationLevelTest {

	@Autowired
	private BookingService bookingService;

	private boolean hasException;

	@Before
	public void before() {
		hasException = false;
	}

	@Test
	public void insertValidBookingShouldBeSuccesful() {
		bookingService.insertBookings("User1", "User2", "User3");
		Assert.assertEquals(3, bookingService.countAllBookings());
	}

	@Test
	public void callingMethodWithReadUncommitedIsolationLevelShouldThrowException()
			throws InterruptedException {
		Runnable checker = getConcurrentCheckerReadUncommited();
		Thread checkerThread = new Thread(checker);
		checkerThread.start();
		bookingService.insertBookings("User1", "User2", "User3");
		checkerThread.join();
		Assert.assertTrue(hasException);
	}

	private Runnable getConcurrentCheckerReadUncommited() {
		Runnable checker = new Runnable() {
			@Override
			public void run() {
				try {
					long size = bookingService.countAllBookingsReadUncommited();
					while (size < 3) {
						SleepUtil.sleep(100);
						size = bookingService.countAllBookingsReadUncommited();
					}
				} catch (InvalidIsolationLevelException e) {
					hasException = true;
				}
			}
		};
		return checker;
	}

}
