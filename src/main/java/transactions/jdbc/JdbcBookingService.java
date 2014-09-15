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
public class JdbcBookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcBookingService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Inserts booking into database.
     * 
     * @param bookings
     *            - the books to be inserted.
     */
    @Transactional
    public void insertBookings(String... bookings) {
        for (String booking : bookings) {
            LOGGER.info("inserting booking: " + booking);
            jdbcTemplate.update("insert into bookings(FIRST_NAME) values (?)", booking);
            SleepUtil.sleep(1000);
        }
    }

    /**
     * counts all the bookings.
     * 
     * @return - the number of bookings.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int countAllBookings() {
        return countAllBooking();
    }

    /**
     * counts all the bookings including inserted but not committed ones.
     * 
     * @return - the number of bookings.
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public int countAllBookingsReadUnCommitted() {
        return countAllBooking();
    }

    /**
     * Reads the booking counts two times with repeatable isolation and
     * subtracts the results.
     * 
     * @return - the difference.
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int differenceBetweenFirstAndSeconFindRepeatableRead() {
        int first = countAllBooking();
        SleepUtil.sleep(2000);
        int second = countAllBooking();
        return first - second;
    }

    /**
     * Reads the booking counts two times with serializable isolation and
     * subtracts the results.
     * 
     * @return - the difference.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int differenceBetweenFirstAndSeconFindSerializable() {
        int first = countAllBooking();
        SleepUtil.sleep(2000);
        int second = countAllBooking();
        return first - second;
    }

    private int countAllBooking() {
        LOGGER.info("finding bookings");
        return jdbcTemplate.queryForObject("select count(FIRST_NAME) from bookings", Integer.class);
    }

}
