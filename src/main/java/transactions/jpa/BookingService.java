package transactions.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import transactions.jpa.entity.Booking;

@Service
public class BookingService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ValidityChecker validityChecker;

	@Autowired
	private JpaTransactionManager transactionManager;

	@Transactional
	public void emptySchema() {
		Query deleteQuery = entityManager.createQuery("delete from Booking");
		deleteQuery.executeUpdate();
	}

	@Transactional
	public void empty() {
		entityManager.clear();
	}

	@Transactional
	public void insertBookings(String... bookings) {
		insertMultipleBookings(bookings);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertBookingsWithRequiredPropagation(String... bookings) {
		insertMultipleBookings(bookings);

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertBookingsWithRequiresNewPropagation(String... bookings) {
		insertMultipleBookings(bookings);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertBookingsWithNestedPropagation(String... bookings) {
		insertMultipleBookings(bookings);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void insertBookingsWithMandatoryPropagation(String... bookings) {
		insertMultipleBookings(bookings);
	}

	@Transactional(propagation = Propagation.NEVER)
	public void insertBookingsWithNeverPropagation(String... bookings) {
		TransactionStatus status = transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		System.out.println("Completed: " + status.isCompleted());
		insertMultipleBookings(bookings);
	}

	public List<String> findAllBookings() {
		entityManager.getEntityManagerFactory().getProperties();
		List<String> result = new ArrayList<String>();
		TypedQuery<Booking> query = entityManager.createQuery("from Booking",
				Booking.class);
		List<Booking> bookings = query.getResultList();
		for (Booking booking : bookings) {
			result.add(booking.getName());
		}
		return result;

	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public List<String> findAllBookingsReadUncommited() {
		return findAllBookings();
	}

	private void insertMultipleBookings(String... bookings) {
		for (String booking : bookings) {
			Booking persistedBooking = new Booking(booking);
			entityManager.persist(persistedBooking);
		}
	}

}
