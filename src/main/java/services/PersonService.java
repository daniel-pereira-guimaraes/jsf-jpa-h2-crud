package services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.Person;

public class PersonService {

	private static PersonService instance;
	private EntityManagerFactory emf;
	private EntityManager em;
	
	private PersonService() {
		super();
		emf = Persistence.createEntityManagerFactory("test");
		em = emf.createEntityManager();
	}
	
	public static PersonService getInstance() {
		if (instance == null)
			instance = new PersonService();
		return instance;
	}

	public Person insert(Person person) {
		em.getTransaction().begin();
		try {
			em.persist(person);
			em.getTransaction().commit();
		} catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
		return person;
	}

	public Person update(Person person) {
		em.getTransaction().begin();
		try {
			person = em.merge(person);
			em.getTransaction().commit();
		} catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
		return person;
	}

	public void delete(Long id) {
		em.getTransaction().begin();
		try {
			Person p = em.find(Person.class, id);
			em.remove(p);
			em.getTransaction().commit();
		} catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Person> selectByText(String text)  {
		String sql = "SELECT p FROM Person p";
		boolean hasFilter = text != null && !text.isEmpty();
		if (hasFilter) {
			sql += " WHERE p.firstName LIKE :text OR p.lastName LIKE :text";
			text = "%" + text + "%";
		}
		Query query = em.createQuery(sql);
		if (hasFilter)
			query.setParameter("text", text);
		return query.getResultList();
	}
	
	public Person selectById(Long id) {
		return em.find(Person.class, id);
	}

}
