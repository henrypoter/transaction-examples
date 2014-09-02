package transactions.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import transactions.jpa.entity.Booking;

@Service
public class SimpleBookingService implements BookingService {

	@PersistenceContext
	private EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see transactions.jpa.BookingService#insertBookings(java.lang.String)
	 */
	@Override
	@Transactional
	public void insertBookings(String... bookings) {
		insertMultipleBookings(bookings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * transactions.jpa.BookingService#insertBookingsWithRequiredPropagation
	 * (java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertBookingsWithRequiredPropagation(String... bookings) {
		insertMultipleBookings(bookings);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * transactions.jpa.BookingService#insertBookingsWithRequiresNewPropagation
	 * (java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void insertBookingsWithRequiresNewPropagation(String... bookings) {
		insertMultipleBookings(bookings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * transactions.jpa.BookingService#insertBookingsWithNestedPropagation(java
	 * .lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertBookingsWithNestedPropagation(String... bookings) {
		insertMultipleBookings(bookings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * transactions.jpa.BookingService#insertBookingsWithMandatoryPropagation
	 * (java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void insertBookingsWithMandatoryPropagation(String... bookings) {
		insertMultipleBookings(bookings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * transactions.jpa.BookingService#insertBookingsWithNeverPropagation(java
	 * .lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void insertBookingsWithNeverPropagation(String... bookings) {
		insertMultipleBookings(bookings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see transactions.jpa.BookingService#countAllBookings()
	 */
	@Override
	public long countAllBookings() {
		return (long) entityManager.createQuery(
				"select count(b.id) from Booking b").getSingleResult();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see transactions.jpa.BookingService#findAllBookingsReadUncommited()
	 */
	@Override
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public long countAllBookingsReadUncommited() {
		return countAllBookings();
	}

	private void insertMultipleBookings(String... bookings) {
		for (String booking : bookings) {
			Booking persistedBooking = new Booking(booking);
			entityManager.persist(persistedBooking);
		}
	}

}
