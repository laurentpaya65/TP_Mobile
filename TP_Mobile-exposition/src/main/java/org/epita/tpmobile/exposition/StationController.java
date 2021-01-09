package org.epita.tpmobile.exposition;

import org.epita.tpmobile.application.StationService;
import org.epita.tpmobile.domaine.Coordonnee;
import org.epita.tpmobile.domaine.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("/api")
public class StationController {
	
	@Autowired
	private StationService stationService;
	
//	http://localhost:8080/api/station/77/0.5
//	{"coordonnee":{"latitude":48.77,"longitude":2.67},
//		"technologie":{"techno":"4G"}
//		}
	// récupération Station la plus proche en fournissant :
	// 	- des coordonnées
	//	- un pas en km définissant un carré autour des coordonnées
	@GetMapping(value = {"/station/{dept}/{pasKm}"},consumes = {"application/json"})
	public String getStation(@RequestBody final Station station,@PathVariable("dept") final String dept, @PathVariable("pasKm") final Double pasKm) {
		System.out.println("latitude="+station.getCoordonnee().getLatitude());
		System.out.println("longitude="+station.getCoordonnee().getLongitude());
		System.out.println("techno="+station.getTechnologie().getTechno());
		System.out.println("pasKm="+pasKm);
		return stationService.getStationByCoordonnee(station,dept, pasKm); 	
	}
	
	// http://localhost:8080/api/chargefichier/ALL/36
	// paramètre {nbLigne} est le nombre de lignes lues
	@GetMapping(value = {"/chargefichier/{nbLigne}/{dept}"})
	public void chargeFichier(@PathVariable("nbLigne") final String nbLigne,
								@PathVariable("dept") final String dept) {
		stationService.chargeFichier(nbLigne, dept);
	}
	
	@PostMapping(value = {"/delete"})
	public void delete() {
		stationService.delete();
	}

}
