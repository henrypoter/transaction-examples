package transactions.jpa;

public interface BookingService {

	/**
	 * Inserts booking into database.
	 * 
	 * @param bookings
	 *            - the bookings to be inserted.
	 */
	void insertBookings(String... bookings);

	/**
	 * Inserts booking into database with REQUIRED propagation mode.
	 * 
	 * @param bookings
	 *            - the bookings to be inserted.
	 */
	void insertBookingsWithRequiredPropagation(String... bookings);

	/**
	 * Inserts booking into database with REQUIRES_NEW propagation mode.
	 * 
	 * @param bookings
	 *            - the bookings to be inserted.
	 */
	void insertBookingsWithRequiresNewPropagation(String... bookings);

	/**
	 * Inserts booking into database with NESTED propagation mode.
	 * 
	 * @param bookings
	 *            - the bookings to be inserted.
	 */
	void insertBookingsWithNestedPropagation(String... bookings);

	/**
	 * Inserts booking into database with MANDATORY propagation mode.
	 * 
	 * @param bookings
	 *            - the bookings to be inserted.
	 */
	void insertBookingsWithMandatoryPropagation(String... bookings);

	/**
	 * Inserts booking into database with NEVER propagation mode.
	 * 
	 * @param bookings
	 *            - the bookings to be inserted.
	 */
	void insertBookingsWithNeverPropagation(String... bookings);

	/**
	 * counts all the bookings.
	 * 
	 * @return - the number of bookings.
	 */
	long countAllBookings();

	/**
	 * counts all the bookings including inserted but not committed ones.
	 * 
	 * @return - the number of bookings.
	 */
	long countAllBookingsReadUncommited();

}