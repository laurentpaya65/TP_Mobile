package org.epita.tpmobile.infrastructure;

import org.epita.tpmobile.domaine.Technologie;

public interface TechnologieDao {
	
	Technologie getTechnologieByTechno(String techno);

}
