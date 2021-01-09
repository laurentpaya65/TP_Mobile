package org.epita.tpmobile.infrastructure;

import java.util.List;

import javax.persistence.Query;
import javax.websocket.Session;

import org.epita.tpmobile.domaine.Departement;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class DepartementDaoImpl implements DepartementDao {

	public void create(Departement departement) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		org.hibernate.Session   session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(departement);
		session.getTransaction().commit();
		session.close();
	}

	public Departement getDepartement(String dept) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		org.hibernate.Session   session = sessionFactory.openSession();
		Query q = session.createQuery("SELECT d FROM Departement d WHERE d.dept=:dept");
		q.setParameter("dept", dept);
		List<Departement> departements = (List<Departement>) q.getResultList();
		session.close();
		if (departements != null && departements.size() != 0) {
			return departements.get(0);
		}
		return null;
	}

}
