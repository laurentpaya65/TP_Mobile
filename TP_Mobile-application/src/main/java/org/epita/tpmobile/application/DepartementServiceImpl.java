package org.epita.tpmobile.application;

import org.epita.tpmobile.domaine.Departement;
import org.epita.tpmobile.infrastructure.DepartementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartementServiceImpl implements DepartementService {

	@Autowired
	private DepartementDao departementDao;
	 
	public void create(String dept) {
		if (departementDao.getDepartement(dept) == null) {
			Departement departement = new Departement();
			departement.setDept(dept);
			departementDao.create(departement);
		}

	}

}
