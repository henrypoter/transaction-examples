package transactions.jdbc;

public interface BookingService {

	/**
	 * Inserts booking into database.
	 * 
	 * @param bookings
	 *            - the books to be inserted.
	 */
	void insertBookings(String... bookings);

	/**
	 * counts all the bookings.
	 * 
	 * @return - the number of bookings.
	 */
	int countAllBookings();

	/**
	 * counts all the bookings including inserted but not committed ones.
	 * 
	 * @return - the number of bookings.
	 */
	int countAllBookingsReadUnCommitted();

	/**
	 * Reads the booking counts two times with repeatable isolation and
	 * subtracts the results.
	 * 
	 * @return - the difference.
	 */
	int differenceBetweenFirstAndSeconFindRepeatableRead();

	/**
	 * Reads the booking counts two times with serializable isolation and
	 * subtracts the results.
	 * 
	 * @return - the difference.
	 */
	int differenceBetweenFirstAndSeconFindSerializable();

}