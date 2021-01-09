package org.epita.tpmobile.infrastructure;

import java.util.List;

import javax.persistence.Query;

import org.epita.tpmobile.domaine.Operateur;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class OperateurDaoImpl implements OperateurDao {

	public Operateur getOperateurByNom(String nom) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		Query q = session.createQuery("SELECT o FROM Operateur o WHERE o.nom = :nom");
		q.setParameter("nom", nom);
		List<Operateur> operateurs = (List<Operateur>) q.getResultList();
		session.close();
		if (operateurs != null && operateurs.size() != 0 ) {
			return operateurs.get(0);
		}
		return null;
	}

}
