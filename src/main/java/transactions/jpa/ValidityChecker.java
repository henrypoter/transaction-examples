package transactions.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import transactions.jpa.entity.Booking;

@Service
public class ValidityChecker {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	public void checkValidity(String... bookings) {
		entityManager.persist(new Booking("validation"));
		throw new RuntimeException("Exception by validation");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void checkValidityRequiresNew(String... bookings) {
		System.out.println("inserting validation");
		entityManager.persist(new Booking("validation"));
	}

	@Transactional(propagation = Propagation.NESTED)
	public void checkValidityNested(String... bookings) {
		entityManager.persist(new Booking("validation"));
		throw new RuntimeException("Exception by validation");
	}

}
