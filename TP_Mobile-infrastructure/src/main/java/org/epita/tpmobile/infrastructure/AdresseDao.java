package org.epita.tpmobile.infrastructure;

import org.epita.tpmobile.domaine.Adresse;

public interface AdresseDao {

	Adresse getAdresseByRue(Adresse adresse);
}
