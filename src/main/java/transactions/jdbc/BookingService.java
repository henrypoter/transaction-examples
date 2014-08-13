package transactions.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger logger = Logger.getLogger(BookingService.class);

	public void createSchema() {
		try {
			jdbcTemplate.execute("create table bookings("
					+ "FIRST_NAME varchar(5) NOT NULL)");
		} catch (DataIntegrityViolationException e) {
			jdbcTemplate.execute("drop table bookings");
			jdbcTemplate.execute("create table bookings("
					+ "FIRST_NAME varchar(5) NOT NULL)");
		}
	}

	@Transactional
	public void insertBookings(String... bookings) {
		for (String booking : bookings) {
			logger.info("inserting booking: " + booking);
			jdbcTemplate.update("insert into bookings(FIRST_NAME) values (?)",
					booking);
			sleep(1000);
		}
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<String> findAllBookings() {
		return find();
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public List<String> findAllBookingsReadUnCommitted() {
		return find();
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public int differenceBetweenFirstAndSeconFindRepeatableRead() {
		List<String> first = find();
		sleep(2000);
		List<String> second = find();
		return first.size() - second.size();
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public int differenceBetweenFirstAndSeconFindSerializable() {
		List<String> first = find();
		sleep(2000);
		List<String> second = find();
		return first.size() - second.size();
	}

	private List<String> find() {
		RowMapper<String> rowMapper = new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet resultSet, int rowNumber)
					throws SQLException {
				return resultSet.getString("FIRST_NAME");
			}
		};
		logger.info("finding bookings");
		return jdbcTemplate.query("select FIRST_NAME from bookings", rowMapper);
	}

	private void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
