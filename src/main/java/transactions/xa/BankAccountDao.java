package transactions.xa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import transactions.jpa.entity.Account;

@Service
public class BankAccountDao implements AccountDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountDao.class);

    @PersistenceContext(unitName = "bankPersistenceUnit")
    private EntityManager entityManager;

    @Override
    public void save(Account bankAccount) {
        LOGGER.info("inserting bank account");
        entityManager.persist(bankAccount);
    }

    /*
     * (non-Javadoc)
     * 
     * @see transactions.xa.AccountDao#getAllCount()
     */
    @Override
    public long getAllCount() {
        return entityManager.createQuery("select count(ba.id) from BankAccount ba", Long.class).getSingleResult();
    }

}
