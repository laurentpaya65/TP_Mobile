package org.epita.tpmobile.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.epita.tpmobile.domaine.Adresse;
import org.epita.tpmobile.domaine.Coordonnee;
import org.epita.tpmobile.domaine.Operateur;
import org.epita.tpmobile.domaine.Station;
import org.epita.tpmobile.domaine.Technologie;
import org.epita.tpmobile.infrastructure.AdresseDao;
import org.epita.tpmobile.infrastructure.DepartementDao;
import org.epita.tpmobile.infrastructure.OperateurDao;
import org.epita.tpmobile.infrastructure.StationDao;
import org.epita.tpmobile.infrastructure.TechnologieDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {

	@Autowired
	private StationDao stationDao;
	@Autowired
	private OperateurDao operateurDao;
	@Autowired
	private TechnologieDao technologieDao;
	@Autowired
	private AdresseDao adresseDao;
	@Autowired
	private DepartementDao departementDao;
	@Autowired
	private DepartementService departementService;
	
    String lignePrecedent = "";
	
	public void create(Station station) {
		Operateur operateur = operateurDao.getOperateurByNom(station.getOperateur().getNom());
		if (operateur != null) {
			station.setOperateur(operateur);
		}
		Technologie technologie = technologieDao.getTechnologieByTechno(station.getTechnologie().getTechno());
		if (technologie != null ) {
			station.setTechnologie(technologie);
		}
		Adresse adresse = adresseDao.getAdresseByRue(station.getAdresse());
		if (adresse != null ) {
			station.setAdresse(adresse);
		}
		stationDao.create(station);
	}
	
	@Transactional
	public void delete() {
		stationDao.delete();
		
	}


	public String getStationByCoordonnee(Station station,String dept, double pasKm) {
		// on delete et recrée si le département n'a pas été trouvé
		if (departementDao.getDepartement(dept) == null) {
			// réinitialisation de la base
			delete();
			chargeFichier("ALL", dept);
			departementService.create(dept);
		}
		
		// controle saisie des coordonnees
		if (station.getCoordonnee() == null  ) {
				return "Coordonnees incompletes";
		}
		
		// controle et init de la technologie
		Technologie technologie = station.getTechnologie();
		String techno = "";
		if (technologie != null &&
			technologieDao.getTechnologieByTechno(technologie.getTechno()) != null) {
			techno = technologie.getTechno();
		}
		System.out.println("technologie="+techno);
		
		// conversion pas en km en degre
		double pasLa = pasKm / 111;
		double pasLg = pasKm / 76;
		// !!!!!! recherche des stations par coordonnées et techno !!!!!!!!!!!!!!!!!
		List<Station> stations = stationDao.getStations(station.getCoordonnee(), pasLg,pasLa,techno);
		if (stations == null ) {
			return "Pas de station trouvée dans la rayon donnée";
		}
		System.out.println("nombre stations trouvées = "+stations.size());
		
		// recherche de la station la plus proche en distance
		double distanceMin = distance(station.getCoordonnee(), stations.get(0).getCoordonnee());
		Station station1 = new Station();
		station1=stations.get(0);
		System.out.println("station la plus proche à : "+distanceMin+"km dans la ville de "+station1.getAdresse().getVille()+" de latitude "+station1.getCoordonnee().getLatitude()+
				" et de longitude "+station1.getCoordonnee().getLongitude());
		for (Station s : stations) {
			System.out.println("station : "+distance(station.getCoordonnee(), s.getCoordonnee())+"km dans la ville de "+s.getAdresse().getVille()+" de latitude "+s.getCoordonnee().getLatitude()+
										" et de longitude "+s.getCoordonnee().getLongitude());
			if ( distance(station.getCoordonnee(), s.getCoordonnee()) < distanceMin ) {
				distanceMin = distance(station.getCoordonnee(), s.getCoordonnee());
				station1 = s;
				System.out.println("station la plus proche : "+distanceMin+"km dans la ville de "+station1.getAdresse().getVille()+" de latitude "+station1.getCoordonnee().getLatitude()+
						" et de longitude "+station1.getCoordonnee().getLongitude());
			}
		}
		double distance = Math.round(distanceMin * 1000) / 1000.0;
		String fiche="------ Station la plus proche à "+distance+"km sur "+stations.size()+" trouvées---------------\n";
		fiche+="station : "+station1.getAdresse().getRue()+"\n";
		fiche+="à "+station1.getAdresse().getVille()+" dans le departement "+station1.getAdresse().getNomdept();
		fiche+=" de latitude "+station1.getCoordonnee().getLatitude()+
				" et de longitude "+station1.getCoordonnee().getLongitude();
		return fiche;
	}

	public void chargeFichier(String nbLigne, String dept) {
		int nbLigneALire = 0;
		int nbLigneACharger = 50000;
		if (nbLigne.equals("ALL")) {
			nbLigneALire = 1000000;
		} else {
			nbLigneALire = Integer.parseInt(nbLigne);
		}
		String csvFile = "C:/Users/laure/OneDrive/Documents/SpringSP_Cours_Correction/Exercice vacances/4G/qos-arcep-habitations-data-2020-v2.csv";
		String[] elements;
		Station station = new Station();
	  	try {
	  		BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
	          String line;
	          int cptLu = 0;
	          int cptACharger = 0;
	          
	          while ((line = bufferedReader.readLine()) != null && cptLu < nbLigneALire && cptACharger < nbLigneACharger) {
	          	System.out.println("n° ligne="+cptLu+"//"+line);
	            elements = traiteLigne(line);
	            if (elements.length >= 27) {
		            // on charge 1 departement
		            if (dept.equals(elements[24]) || elements[4].equals("operateur") ) {
			            station= construitStation(elements);
			            create(station);
			            cptACharger += 1;
		            }
		            cptLu += 1;
	            }
	          }
	      } catch (IOException ex) {
	          ex.printStackTrace();
	      }
	}
	
	private String[] traiteLigne(String ligne) {
		String[] mots;
		ligne = lignePrecedent + ligne;
		mots  = ligne.split(";",-1);
		if (mots.length < 27) {
			return mots;
		}
		lignePrecedent = "";
//		System.out.println("taille ligne = "+mots.length);
//        System.out.println("operateur="+mots[4]);
//        System.out.println("techno="+mots[5]);
//        System.out.println("latitude="+mots[7]);
//        System.out.println("longitude="+mots[8]);
//        System.out.println("ville="+mots[16]);
//        System.out.println("rue="+mots[14]);
//        System.out.println("dept="+mots[24]);
//        System.out.println("nom dept="+mots[26]);
//        System.out.println("region="+mots[25]);
		return mots;
	}
	
	private Station construitStation(String[] elements) {
		Station station = new Station();
		Operateur operateur = new Operateur();
		operateur.setNom(elements[4]);
		station.setOperateur(operateur);
		
		Technologie technologie = new Technologie();
		technologie.setTechno(elements[5]);
		station.setTechnologie(technologie);
		
		Coordonnee coordonnee = new Coordonnee();

		if (elements[4].equals("operateur")) {
			coordonnee.setLatitude(0);
			coordonnee.setLongitude(0);
		} else {
			System.out.println("latitude="+Double.parseDouble(elements[7].replace(",",".")));
			coordonnee.setLatitude(Double.parseDouble(elements[7].replace(",",".")));
			coordonnee.setLongitude(Double.parseDouble(elements[8].replace(",",".")));
		}
		station.setCoordonnee(coordonnee);
		
		Adresse adresse = new Adresse();
		adresse.setVille(elements[16]);
		adresse.setRue(elements[14]);
		adresse.setDept(elements[24]);
		adresse.setNomdept(elements[26]);
		adresse.setRegion(elements[25]);
		station.setAdresse(adresse);
		
		return station;
	}
	
	private double distance(Coordonnee crdDonnee,Coordonnee crdTable) {
		return Math.sqrt( Math.pow( (crdTable.getLatitude() - crdDonnee.getLatitude() )*111 , 2) +
				Math.pow( (crdDonnee.getLongitude() - crdTable.getLongitude())*76 , 2) );
	}

}
