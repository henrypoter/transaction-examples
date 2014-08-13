package transactions.jpa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jpa-context.xml" })
@EnableTransactionManagement
public class IsolationLevelTest {

	@Autowired
	private BookingService bookingService;

	@Before
	public void setUp() {
		bookingService.emptySchema();
	}

	@Test
	public void insertValidBookingsExpectedToBeSuccesful() {
		bookingService.insertBookings("User1", "User2", "User3");
		Assert.assertEquals(3, bookingService.findAllBookings().size());
	}

	@Test
	public void testInsertingWithTransactionAndReadingUncommited() {
		Runnable checker = getConcurrentCheckerReadUncommited();
		new Thread(checker).start();
		bookingService.insertBookings("User1", "User2", "User3");
	}

	private Runnable getConcurrentCheckerReadUncommited() {
		Runnable checker = new Runnable() {
			@Override
			public void run() {
				int size = bookingService.findAllBookingsReadUncommited()
						.size();
				while (size < 3) {
					sleep(100);
					size = bookingService.findAllBookingsReadUncommited()
							.size();
				}
			}
		};
		return checker;
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
