package transactions.xa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import transactions.jpa.entity.InvestmentAccount;

@Service
public class InvestmentAccountDao {

	private static final Logger logger = Logger
			.getLogger(InvestmentAccountDao.class);

	@PersistenceContext(unitName = "investmentPersistenceUnit")
	private EntityManager entityManager;

	public void save(InvestmentAccount investmentAccount) {
		logger.info("inserting investment account");
		entityManager.persist(investmentAccount);
	}

	public Integer getAllCount() {
		return entityManager.createQuery("from InvestmentAccount")
				.getResultList().size();
	}

}
