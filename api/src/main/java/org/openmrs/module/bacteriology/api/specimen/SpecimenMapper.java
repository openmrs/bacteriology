package org.openmrs.module.bacteriology.api.specimen;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.emrapi.encounter.ConceptMapper;
import org.openmrs.module.emrapi.encounter.EncounterObservationServiceHelper;
import org.openmrs.module.emrapi.encounter.ObservationMapper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.encounter.exception.ConceptNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecimenMapper {

    @Autowired
    private EncounterObservationServiceHelper encounterObservationServiceHelper;

    @Autowired
    private ConceptMapper conceptMapper;

    @Autowired
    private ObservationMapper observationMapper;

    @Autowired
    private ObsService obsService;

    @Autowired
    private ConceptService conceptService;

    public EncounterObservationServiceHelper getEncounterObservationServiceHelper() {
        return encounterObservationServiceHelper;
    }

    public void setEncounterObservationServiceHelper(EncounterObservationServiceHelper encounterObservationServiceHelper) {
        this.encounterObservationServiceHelper = encounterObservationServiceHelper;
    }

    public ObsService getObsService() {
        return obsService;
    }

    public void setObsService(ObsService obsService) {
        this.obsService = obsService;
    }

    public ConceptService getConceptService() {
        return conceptService;
    }

    public void setConceptService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    private void validate(org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen) {

        if (etSpecimen.getType() == null)
            throw new IllegalArgumentException("Sample Type is mandatory");

        if (etSpecimen.getDateCollected() == null)
            throw new IllegalArgumentException("Sample Date Collected detail is mandatory");
    }

    private void validate(Specimen specimen) {

        if (specimen.getType() == null)
            throw new IllegalArgumentException("Sample Type is mandatory");

        if (specimen.getDateCollected() == null)
            throw new IllegalArgumentException("Sample Date Collected detail is mandatory");
    }

    public Specimen createSpecimen(Encounter encounter, org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen) {
        validate(etSpecimen);

        Specimen bacteriologySpecimen = new Specimen();
        bacteriologySpecimen.setUuid(etSpecimen.getExistingObs());
        bacteriologySpecimen.setId(etSpecimen.getIdentifier());
        bacteriologySpecimen.setDateCollected(etSpecimen.getDateCollected());

        if (StringUtils.isNotEmpty(etSpecimen.getExistingObs())) {
            bacteriologySpecimen.setExistingObs(obsService.getObsByUuid(etSpecimen.getExistingObs()));
        }

        if (etSpecimen.getSample().getAdditionalAttributes() != null) {
            EncounterTransaction.Observation etObs = etSpecimen.getSample().getAdditionalAttributes();
            bacteriologySpecimen.setAdditionalAttributes(encounterObservationServiceHelper.transformEtObs(bacteriologySpecimen.getExistingObs(), etObs));
        }

        bacteriologySpecimen.setType(getSampleTypeConcept(etSpecimen.getType()));

        if (etSpecimen.getReport() != null && etSpecimen.getReport().getResults() != null) {
            EncounterTransaction.Observation etObs = etSpecimen.getReport().getResults();
            bacteriologySpecimen.setReports(encounterObservationServiceHelper.transformEtObs(bacteriologySpecimen.getReports(), etObs));
        }

        return bacteriologySpecimen;
    }


    public org.openmrs.module.bacteriology.api.encounter.domain.Specimen createDomainSpecimen(Specimen specimen) {
        org.openmrs.module.bacteriology.api.encounter.domain.Specimen domainSpecimen = new org.openmrs.module.bacteriology.api.encounter.domain.Specimen();
        validate(specimen);

        domainSpecimen.setIdentifier(specimen.getId());
        domainSpecimen.setDateCollected(specimen.getDateCollected());


        if (specimen.getExistingObs() != null) {
            domainSpecimen.setExistingObs(specimen.getExistingObs().getUuid());
            domainSpecimen.setUuid(specimen.getExistingObs().getUuid());
        }

        if (specimen.getAdditionalAttributes() != null) {
            domainSpecimen.getSample().setAdditionalAttributes(observationMapper.map(specimen.getAdditionalAttributes()));
        }
        if (specimen.getType() != null) {
            ConceptMapper conceptMapper = new ConceptMapper();
            domainSpecimen.setType(conceptMapper.map(specimen.getType()));
        }

        if (specimen.getReports() != null) {
            domainSpecimen.setReport(new org.openmrs.module.bacteriology.api.encounter.domain.Specimen.TestReport());
            domainSpecimen.getReport().setResults(observationMapper.map(specimen.getReports()));
        }
        return domainSpecimen;
    }

    private Concept getSampleTypeConcept(EncounterTransaction.Concept type) {
        Concept sampleType = conceptService.getConceptByUuid(type.getUuid());

        if (sampleType == null)
            throw new ConceptNotFoundException("Sample Type Concept " + type + " is not available");

        return sampleType;
    }

}
