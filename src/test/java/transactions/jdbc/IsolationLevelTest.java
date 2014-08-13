package transactions.jdbc;

import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jdbc-context.xml" })
@EnableTransactionManagement
public class IsolationLevelTest {

	private static final Logger logger = Logger
			.getLogger(IsolationLevelTest.class);

	@Autowired
	private BookingService bookingService;

	private boolean readUnCommitted;

	@Before
	public void cleanup() {
		bookingService.createSchema();
		readUnCommitted = false;
	}

	@Test
	public void testReadUnComittedShouldBeFalse() {
		Runnable checker = getConcurrentChecker();
		new Thread(checker).start();
		bookingService.insertBookings("book1", "book2", "book3");
		Assert.assertEquals(3, bookingService.findAllBookings().size());
		Assert.assertFalse(readUnCommitted);
	}

	@Test
	public void testReadUnComittedShouldBeTrue() {
		Runnable checker = getConcurrentCheckerReadUncommitted();
		new Thread(checker).start();
		bookingService.insertBookings("book1", "book2", "book3");
		Assert.assertEquals(3, bookingService.findAllBookings().size());
		Assert.assertTrue(readUnCommitted);
	}

	@Test
	public void testRepeatableReadShouldGiveDifferentResultWhileInserting() {
		Runnable concurrentInsert = getConcurrentInsert();
		new Thread(concurrentInsert).start();
		int difference = bookingService
				.differenceBetweenFirstAndSeconFindRepeatableRead();
		Assert.assertEquals(-1, difference);
	}

	@Test
	public void testRepeatableReadShouldGiveSameResultWhileInserting() {
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
				int size = bookingService.findAllBookings().size();
				while (size < 3) {
					size = bookingService.findAllBookings().size();
					logger.info("In thread:" + size);
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
				int size = bookingService.findAllBookingsReadUnCommitted()
						.size();
				while (size < 3) {
					size = bookingService.findAllBookingsReadUnCommitted()
							.size();
					logger.info("In thread:" + size);
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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info("insert running");
				bookingService.insertBookings("book1");
			}
		};
		return insert;
	}

}
