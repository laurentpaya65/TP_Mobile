package org.epita.tpmobile.infrastructure;

import java.util.List;

import javax.persistence.Query;

import org.epita.tpmobile.domaine.Adresse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AdresseDaoImpl implements AdresseDao {

	public Adresse getAdresseByRue(Adresse adresse) {
		String critere = "a.ville = :ville AND a.rue=:rue";
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		Query q = session.createQuery("SELECT a FROM Adresse a WHERE "+critere);
		q.setParameter("ville", adresse.getVille());
		q.setParameter("rue", adresse.getRue());
		List<Adresse> adresses = (List<Adresse>) q.getResultList();
		session.close();
		if (adresses != null && adresses.size() != 0) {
			return adresses.get(0);
		}
		return null;
	}

}
