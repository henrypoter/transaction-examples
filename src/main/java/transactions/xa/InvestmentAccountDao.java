package transactions.xa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import transactions.jpa.entity.Account;

@Service
public class InvestmentAccountDao implements AccountDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAccountDao.class);

    @PersistenceContext(unitName = "investmentPersistenceUnit")
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * 
     * @see transactions.xa.AccountDao#save(transactions.jpa.entity.Account)
     */
    @Override
    public void save(Account investmentAccount) {
        LOGGER.info("inserting investment account");
        entityManager.persist(investmentAccount);
    }

    /*
     * (non-Javadoc)
     * 
     * @see transactions.xa.AccountDao#getAllCount()
     */
    @Override
    public long getAllCount() {
        return entityManager.createQuery("select count(ia.id) from InvestmentAccount ia", Long.class).getSingleResult();
    }

}
