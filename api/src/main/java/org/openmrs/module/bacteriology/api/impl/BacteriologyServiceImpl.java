/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.bacteriology.api.impl;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.bacteriology.BacteriologyProperties;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.db.BacteriologyServiceDAO;
import org.openmrs.module.bacteriology.api.encounter.BacteriologyMapper;
import org.openmrs.module.bacteriology.api.encounter.EncounterObservationMapper;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.bacteriology.api.specimen.SpecimenMapper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * It is a default implementation of {@link BacteriologyService}.
 */
public class BacteriologyServiceImpl extends BaseOpenmrsService implements BacteriologyService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private BacteriologyServiceDAO dao;

    @Autowired
    private SpecimenMapper specimenMapper;

    @Autowired
    private BacteriologyProperties bacteriologyProperties;

    @Autowired
    private BacteriologyMapper bacteriologyMapper;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(BacteriologyServiceDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public BacteriologyServiceDAO getDao() {
	    return dao;
    }

    @Override
    public void updateEncounter(Encounter encounter, EncounterTransaction encounterTransaction) {

        List<Specimen> specimens = bacteriologyMapper.mapSpecimen(encounterTransaction);

        for(Specimen specimen:specimens){
            org.openmrs.module.bacteriology.api.specimen.Specimen bacteriologySpecimen = specimenMapper.createSpecimen(encounter,specimen);
            Obs bacteriologyObs = bacteriologyProperties.getSpecimenMetadata().buildObsGroup(bacteriologySpecimen);
            encounter.addObs(bacteriologyObs);
        }
    }
}