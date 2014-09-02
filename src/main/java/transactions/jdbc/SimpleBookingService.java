package transactions.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import util.SleepUtil;

@Service
public class SimpleBookingService implements BookingService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleBookingService.class);

	/* (non-Javadoc)
	 * @see transactions.jdbc.BS#insertBookings(java.lang.String)
	 */
	@Override
	@Transactional
	public void insertBookings(String... bookings) {
		for (String booking : bookings) {
			LOGGER.info("inserting booking: " + booking);
			jdbcTemplate.update("insert into bookings(FIRST_NAME) values (?)",
					booking);
			SleepUtil.sleep(1000);
		}
	}

	/* (non-Javadoc)
	 * @see transactions.jdbc.BS#countAllBookings()
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int countAllBookings() {
		return countAllBooking();
	}

	/* (non-Javadoc)
	 * @see transactions.jdbc.BS#countAllBookingsReadUnCommitted()
	 */
	@Override
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public int countAllBookingsReadUnCommitted() {
		return countAllBooking();
	}

	/* (non-Javadoc)
	 * @see transactions.jdbc.BS#differenceBetweenFirstAndSeconFindRepeatableRead()
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public int differenceBetweenFirstAndSeconFindRepeatableRead() {
		int first = countAllBooking();
		SleepUtil.sleep(2000);
		int second = countAllBooking();
		return first - second;
	}

	/* (non-Javadoc)
	 * @see transactions.jdbc.BS#differenceBetweenFirstAndSeconFindSerializable()
	 */
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public int differenceBetweenFirstAndSeconFindSerializable() {
		int first = countAllBooking();
		SleepUtil.sleep(2000);
		int second = countAllBooking();
		return first - second;
	}

	private int countAllBooking() {
		LOGGER.info("finding bookings");
		return jdbcTemplate.queryForObject(
				"select count(FIRST_NAME) from bookings", Integer.class);
	}

}
