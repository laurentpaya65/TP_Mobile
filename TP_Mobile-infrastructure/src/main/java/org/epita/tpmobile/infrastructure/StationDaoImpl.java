package org.epita.tpmobile.infrastructure;

import java.util.List;

import javax.persistence.Query;

import org.epita.tpmobile.domaine.Coordonnee;
import org.epita.tpmobile.domaine.Station;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class StationDaoImpl implements StationDao {

	public void create(Station station) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(station);
		session.getTransaction().commit();
		session.close();

	}
	

	public void delete() {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		// delete des stations
		Query q = session.createQuery("DELETE FROM Station");
		int rowDelete = q.executeUpdate();
		System.out.println("lignes Station delete="+rowDelete);
		// delete des opérateurs
		q = session.createQuery("DELETE FROM Operateur");
		rowDelete = q.executeUpdate();
		System.out.println("lignes Operateur delete="+rowDelete);
		// delete des techno
		q = session.createQuery("DELETE FROM Technologie");
		rowDelete = q.executeUpdate();
		System.out.println("lignes Techno delete="+rowDelete);
		// delete des adresses
		q = session.createQuery("DELETE FROM Adresse");
		rowDelete = q.executeUpdate();
		System.out.println("lignes Adresse delete="+rowDelete);
		// delete des Coordonnees
		q = session.createQuery("DELETE FROM Coordonnee");
		rowDelete = q.executeUpdate();
		System.out.println("lignes Coordonnee delete="+rowDelete);
		// delete des Coordonnees
		q = session.createQuery("DELETE FROM Departement");
		rowDelete = q.executeUpdate();
		System.out.println("lignes Departement delete="+rowDelete);
		session.getTransaction().commit();
		session.close();
	}


	public List<Station> getStations(Coordonnee coordonnee,double pasLg,double pasLa,String techno) {
		String critere = "c.latitude < :latmax AND c.latitude > :latmin AND c.longitude < :longmax AND c.longitude > :longmin";
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		Query q;
		if (techno.equals("")) {
			q = session.createQuery("SELECT s FROM Station AS s JOIN s.coordonnee AS c WHERE "+critere );
		} else {
			q = session.createQuery("SELECT s FROM Station AS s JOIN s.coordonnee AS c "+
															  " JOIN s.technologie AS t "+
															  " WHERE "+critere+
															  " AND t.techno=:techno");
			q.setParameter("techno", techno);
		}
		q.setParameter("latmax", coordonnee.getLatitude() + pasLa);
		q.setParameter("latmin", coordonnee.getLatitude() - pasLa);
		q.setParameter("longmax", coordonnee.getLongitude() + pasLg);
		q.setParameter("longmin", coordonnee.getLongitude() - pasLg);
		List<Station> stations = (List<Station>) q.getResultList();
		session.close();
		if (stations != null && stations.size() != 0) {
			return stations;
		}
		
		return null;
	}

}
