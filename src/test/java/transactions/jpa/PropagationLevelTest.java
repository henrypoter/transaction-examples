package transactions.jpa;

import javax.transaction.SystemException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jpa-context.xml" })
@EnableTransactionManagement
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PropagationLevelTest {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private JpaTransactionManager transactionManager;

	@Test
	public void testInserting() {
		bookingService.insertBookings("User 1");
		Assert.assertEquals(1, bookingService.findAllBookings().size());
	}

	@Test
	public void testEmpty() {
		Assert.assertEquals(0, bookingService.findAllBookings().size());
	}

	@Test
	public void testPropagationRequired() throws SystemException {
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		bookingService.insertBookingsWithRequiredPropagation("User 1");
		transactionManager.rollback(status);
		Assert.assertEquals(0, bookingService.findAllBookings().size());

	}

	@Test
	public void testPropagationRequiresNew() {
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		bookingService.insertBookingsWithRequiresNewPropagation("User 1");
		transactionManager.rollback(status);
		Assert.assertEquals(1, bookingService.findAllBookings().size());
	}

	@Test
	public void testPropagationNested() {
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		bookingService.insertBookingsWithNestedPropagation("User 1");
		transactionManager.rollback(status);
		Assert.assertEquals(0, bookingService.findAllBookings().size());
	}

	@Test(expected = IllegalTransactionStateException.class)
	public void testPropagationMandatory() {
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		transactionManager.commit(status);
		bookingService.insertBookingsWithMandatoryPropagation("User 1");
	}

	@Test
	public void testPropagationMandatorySuccess() {
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		bookingService.insertBookingsWithMandatoryPropagation("User 1");
		transactionManager.rollback(status);
		Assert.assertEquals(0, bookingService.findAllBookings().size());
	}

	@Test
	public void testPropagationNever() {
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		try {
			bookingService.insertBookingsWithNeverPropagation("User 1");
			Assert.fail();
		} catch (IllegalTransactionStateException e) {
			transactionManager.rollback(status);
		}
	}

}
