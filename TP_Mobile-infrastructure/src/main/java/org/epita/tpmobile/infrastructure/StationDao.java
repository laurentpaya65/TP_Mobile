package org.epita.tpmobile.infrastructure;

import java.util.List;

import org.epita.tpmobile.domaine.Coordonnee;
import org.epita.tpmobile.domaine.Station;

public interface StationDao {

	void create(Station station);
	
	void delete();
	
	List<Station> getStations(Coordonnee coordonnee,double pasLg,double pasLa,String techno);
}
