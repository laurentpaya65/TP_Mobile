package org.epita.tpmobile.infrastructure;

import org.epita.tpmobile.domaine.Departement;

public interface DepartementDao {
	
	void create(Departement departement);
	
	Departement getDepartement(String dept);

}
