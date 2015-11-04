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

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.bacteriology.BacteriologyConstants;
import org.openmrs.module.bacteriology.BacteriologyProperties;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.db.BacteriologyServiceDAO;
import org.openmrs.module.bacteriology.api.encounter.BacteriologyMapper;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.bacteriology.api.specimen.SpecimenMapper;
import org.openmrs.module.emrapi.encounter.ConceptMapper;
import org.openmrs.module.emrapi.encounter.ObservationMapper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ConceptMapper conceptMapper;

    @Autowired
    private ObservationMapper observationMapper;

    public void setDao(BacteriologyServiceDAO dao) {
	    this.dao = dao;
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

    @Override
    public Specimen getSpecimenFromObs(Obs obsGroup){
        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = bacteriologyProperties.getSpecimenMetadata().buildSpecimen(obsGroup);
        return createDomainSpecimen(specimen);
    }

    @Override
    public void updateEncounterTransaction(Encounter encounter, EncounterTransaction encounterTransaction) {
        List<org.openmrs.module.bacteriology.api.specimen.Specimen> bacteriologySpecimenList =  bacteriologyProperties.getSpecimenMetadata().getSpecimenFromObs(encounter.getObsAtTopLevel(false));

        List<Specimen> specimens = new ArrayList<Specimen>();
        for(org.openmrs.module.bacteriology.api.specimen.Specimen bacteriologySpecimen: bacteriologySpecimenList) {
            specimens.add(createDomainSpecimen(bacteriologySpecimen));
        }

        Map<String,Object> extensions = new HashMap<String, Object>();
        extensions.put(BacteriologyConstants.BACTERIOLOGY_EXTENSION_KEY,specimens);
        encounterTransaction.setExtensions(extensions);
    }


    private void validate(org.openmrs.module.bacteriology.api.specimen.Specimen specimen) {

        if (specimen.getType() == null)
            throw new IllegalArgumentException("Sample Type is mandatory");

        if (specimen.getDateCollected() == null)
            throw new IllegalArgumentException("Sample Date Collected detail is mandatory");
    }

    public org.openmrs.module.bacteriology.api.encounter.domain.Specimen createDomainSpecimen(org.openmrs.module.bacteriology.api.specimen.Specimen specimen) {
        org.openmrs.module.bacteriology.api.encounter.domain.Specimen domainSpecimen = new org.openmrs.module.bacteriology.api.encounter.domain.Specimen();
        validate(specimen);
        domainSpecimen.setIdentifier(specimen.getId());
        domainSpecimen.setDateCollected(specimen.getDateCollected());


        if (specimen.getExistingObs() != null) {
            domainSpecimen.setExistingObs(specimen.getExistingObs().getUuid());
            domainSpecimen.setUuid(specimen.getExistingObs().getUuid());
        }

        if (specimen.getAdditionalAttributes() != null) {
            domainSpecimen.setSample(new Specimen.Sample());
            domainSpecimen.getSample().setAdditionalAttributes(observationMapper.map(specimen.getAdditionalAttributes()));
        }
        if (specimen.getType() != null) {
            domainSpecimen.setType(conceptMapper.map(specimen.getType()));
        }

        if (specimen.getReports() != null) {
            domainSpecimen.setReport(new org.openmrs.module.bacteriology.api.encounter.domain.Specimen.TestReport());
            domainSpecimen.getReport().setResults(observationMapper.map(specimen.getReports()));
        }
        return domainSpecimen;
    }
}