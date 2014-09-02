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
@ContextConfiguration(locations = { "classpath:jdbc-context.xml" })
@EnableTransactionManagement
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class IsolationLevelTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IsolationLevelTest.class);

	@Autowired
	private BookingService bookingService;

	private boolean readUnCommitted;

	@Test
	public void readUnComittedShouldBeFalseWhenNoReadingUncomittedMethodIsUsed() {
		Runnable checker = getConcurrentChecker();
		new Thread(checker).start();
		bookingService.insertBookings("book1", "book2", "book3");
		Assert.assertEquals(3, bookingService.countAllBookings());
		Assert.assertFalse(readUnCommitted);
	}

	@Test
	public void readUnComittedShouldBeTrueWhenReadingUncomittedMethodIsUsed() {
		Runnable checker = getConcurrentCheckerReadUncommitted();
		new Thread(checker).start();
		bookingService.insertBookings("book1", "book2", "book3");
		Assert.assertEquals(3, bookingService.countAllBookings());
		Assert.assertTrue(readUnCommitted);
	}

	@Test
	public void repeatableReadShouldGiveDifferentResultWhenRepeatableReadIsUsed() {
		Runnable concurrentInsert = getConcurrentInsert();
		new Thread(concurrentInsert).start();
		int difference = bookingService
				.differenceBetweenFirstAndSeconFindRepeatableRead();
		Assert.assertEquals(-1, difference);
	}

	@Test
	public void testRepeatableReadShouldGiveSameResultWhenSerializableReadIsUsed() {
		Runnable concurrentInsert = getConcurrentInsert();
		new Thread(concurrentInsert).start();
		int difference = bookingService
				.differenceBetweenFirstAndSeconFindSerializable();
		Assert.assertEquals(0, difference);
	}

	private Runnable getConcurrentChecker() {
		Runnable checker = new Runnable() {
			@Override
			public void run() {
				int size = bookingService.countAllBookings();
				while (size < 3) {
					size = bookingService.countAllBookings();
					LOGGER.info("In thread:" + size);
					if (0 < size && size < 3) {
						readUnCommitted = true;
					}
				}
			}
		};
		return checker;
	}

	private Runnable getConcurrentCheckerReadUncommitted() {
		Runnable checker = new Runnable() {
			@Override
			public void run() {
				int size = bookingService.countAllBookingsReadUnCommitted();
				while (size < 3) {
					size = bookingService.countAllBookingsReadUnCommitted();
					LOGGER.info("In thread:" + size);
					if (0 < size && size < 3) {
						readUnCommitted = true;
					}
				}
			}
		};
		return checker;
	}

	private Runnable getConcurrentInsert() {
		Runnable insert = new Runnable() {
			@Override
			public void run() {
				SleepUtil.sleep(1000);
				LOGGER.info("insert running");
				bookingService.insertBookings("book1");
			}
		};
		return insert;
	}

}
