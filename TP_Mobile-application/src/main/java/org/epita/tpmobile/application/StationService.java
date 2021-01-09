package org.epita.tpmobile.application;

import org.epita.tpmobile.domaine.Coordonnee;
import org.epita.tpmobile.domaine.Station;

public interface StationService {
	
	void create(Station station);
	
	void delete();
	
	String getStationByCoordonnee(Station station,String dept,double pasKm);
	
	void chargeFichier(String nbLigne,String dept);

}
