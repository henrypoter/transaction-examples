package transactions.xa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import transactions.jpa.entity.BankAccount;

@Service
public class BankAccountDao {

	private static final Logger logger = Logger.getLogger(BankAccountDao.class);

	@PersistenceContext(unitName = "bankPersistenceUnit")
	private EntityManager entityManager;

	public void save(BankAccount bankAccount) {
		logger.info("inserting bank account");
		entityManager.persist(bankAccount);
	}

	public Integer getAllCount() {
		return entityManager.createQuery("from BankAccount").getResultList()
				.size();
	}

}
