package org.epita.tpmobile.infrastructure;

import java.util.List;

import javax.persistence.Query;

import org.epita.tpmobile.domaine.Technologie;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TechnologieDaoImpl implements TechnologieDao {

	public Technologie getTechnologieByTechno(String techno) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		Query q = session.createQuery("SELECT t FROM Technologie t WHERE t.techno=:techno");
		q.setParameter("techno", techno);
		List<Technologie> technologies = (List<Technologie>) q.getResultList();
		session.close();
		if (technologies != null && technologies.size() != 0) {
			return technologies.get(0);
		}
		return null;
	}

}
