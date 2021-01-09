package org.epita.tpmobile.infrastructure;

import org.epita.tpmobile.domaine.Operateur;

public interface OperateurDao {

	Operateur getOperateurByNom(String nom);
}
