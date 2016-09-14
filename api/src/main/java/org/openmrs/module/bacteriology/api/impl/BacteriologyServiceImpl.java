/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.bacteriology.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.bacteriology.BacteriologyConstants;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.db.BacteriologyServiceDAO;
import org.openmrs.module.bacteriology.api.encounter.BacteriologyMapper;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimens;
import org.openmrs.module.bacteriology.api.specimen.SpecimenMapper;
import org.openmrs.module.bacteriology.api.specimen.SpecimenMetadataDescriptor;
import org.openmrs.module.emrapi.encounter.ConceptMapper;
import org.openmrs.module.emrapi.encounter.ObservationMapper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * It is a default implementation of {@link BacteriologyService}.
 */
public class BacteriologyServiceImpl extends BaseOpenmrsService implements BacteriologyService {

    protected final Log log = LogFactory.getLog(this.getClass());

    private BacteriologyServiceDAO dao;

    @Autowired
    private SpecimenMapper specimenMapper;

    @Autowired
    private BacteriologyMapper bacteriologyMapper;

    @Autowired
    private ConceptMapper conceptMapper;

    @Autowired
    private ObservationMapper observationMapper;

    @Autowired
    private ObsService obsService;

    @Autowired
    private ConceptService conceptService;
    

    /**
     * @param dao the dao to set
     */
    public void setDao(BacteriologyServiceDAO dao) {
        this.dao = dao;
    }

    @Override
    public void updateEncounter(Encounter encounter, EncounterTransaction encounterTransaction) {

        List<Specimen> specimens = bacteriologyMapper.mapSpecimen(encounterTransaction);

        for (Specimen specimen : specimens) {
            org.openmrs.module.bacteriology.api.specimen.Specimen bacteriologySpecimen = specimenMapper.createSpecimen(encounter, specimen);
            Obs bacteriologyObs = SpecimenMetadataDescriptor.get(conceptService).buildObsGroup(bacteriologySpecimen);
            if(specimen.isVoided()) voidBacteriologyObsUponSpecimenVoided(bacteriologyObs);
            encounter.addObs(bacteriologyObs);
        }
    }

    @Override
    public Specimen getSpecimen(Obs obsGroup){
        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = SpecimenMetadataDescriptor.get(conceptService).buildSpecimen(obsGroup);
        return createDomainSpecimen(specimen);
    }

    @Override
    public void updateEncounterTransaction(Encounter encounter, EncounterTransaction encounterTransaction) {
        List<org.openmrs.module.bacteriology.api.specimen.Specimen> bacteriologySpecimenList = SpecimenMetadataDescriptor.get(conceptService).getSpecimenFromObs(encounter.getObsAtTopLevel(false));

        List<Specimen> specimens = new ArrayList<Specimen>();
        for (org.openmrs.module.bacteriology.api.specimen.Specimen bacteriologySpecimen : bacteriologySpecimenList) {
            specimens.add(createDomainSpecimen(bacteriologySpecimen));
        }

        Map<String, Object> extensions = new HashMap<String, Object>();
        extensions.put(BacteriologyConstants.BACTERIOLOGY_EXTENSION_KEY, specimens);
        encounterTransaction.setExtensions(extensions);

        removeSpecimenObsFromEncounterTransactionObs(encounter, encounterTransaction);

    }

    private void removeSpecimenObsFromEncounterTransactionObs(Encounter encounter, EncounterTransaction encounterTransaction) {
        List<EncounterTransaction.Observation> ETObsList = encounterTransaction.getObservations();
        Map<String, Object> ETUuidObservationMap = new HashMap<String, Object>();
        for (EncounterTransaction.Observation observation : ETObsList) {
            ETUuidObservationMap.put(observation.getUuid(), observation);
        }

        List<Obs> obsGroupAtSpecimenLevel = SpecimenMetadataDescriptor.get(conceptService).getSpecimenObsGroups(encounter.getObsAtTopLevel(false));
        for (Obs obsGroup : obsGroupAtSpecimenLevel) {
            ETObsList.remove(ETUuidObservationMap.get(obsGroup.getUuid()));
        }
        encounterTransaction.setObservations(ETObsList);

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

        domainSpecimen.setType(conceptMapper.map(specimen.getType()));
        domainSpecimen.setTypeFreeText(specimen.getTypeFreeText());

        if (specimen.getReports() != null) {
            domainSpecimen.setReport(new org.openmrs.module.bacteriology.api.encounter.domain.Specimen.TestReport());
            domainSpecimen.getReport().setResults(observationMapper.map(specimen.getReports()));
        }
        return domainSpecimen;
    }

    @Override
    public Specimen saveSpecimen(Specimen specimen) {
        Obs obs = obsService.getObsByUuid(specimen.getExistingObs());
        Encounter encounter = obs.getEncounter();

        org.openmrs.module.bacteriology.api.specimen.Specimen bacteriologySpecimen = specimenMapper.createSpecimen(encounter, specimen);
        Obs bacteriologyObs = SpecimenMetadataDescriptor.get(conceptService).buildObsGroup(bacteriologySpecimen);
        encounter.addObs(bacteriologyObs);

        return specimen;
    }

    @Override
    public Specimens getSpecimens(Collection<Obs> observations) {
        Specimens specimens = new Specimens();
        for (Obs observation : observations) {
            Specimen specimen = getSpecimen(sortGroupMembersByConceptId(observation));
            specimens.add(specimen);
        }
        return specimens;
    }

    private Obs sortGroupMembersByConceptId(Obs observation){
        if(observation != null){
            Concept concept = observation.getConcept();
            if(concept.isSet()){
                List<Concept> setMembers = observation.getConcept().getSetMembers();
                Set<Obs> sortedGroupMembers = new LinkedHashSet<Obs>();
                for (Concept setMember : setMembers) {
                    for (Obs obs : observation.getGroupMembers()) {
                        if (obs.getConcept().equals(setMember)) {
                            sortedGroupMembers.add(obs);
                            sortGroupMembersByConceptId(obs);
                        }
                    }
                }
                observation.setGroupMembers(sortedGroupMembers);
            }
        }
        return observation;
    }

    private void voidBacteriologyObsUponSpecimenVoided(Obs bacteriologyObs) {
        bacteriologyObs.setVoided(true);
        Set<Obs> members = bacteriologyObs.getGroupMembers();
        if (members != null) {
            for (Obs member : members) {
                voidBacteriologyObsUponSpecimenVoided(member);
            }
        }
    }
}
