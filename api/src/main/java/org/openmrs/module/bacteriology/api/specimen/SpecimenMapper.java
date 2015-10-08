package org.openmrs.module.bacteriology.api.specimen;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.module.bacteriology.BacteriologyProperties;
import org.openmrs.module.emrapi.encounter.EncounterObservationServiceHelper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.encounter.exception.ConceptNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecimenMapper {

    @Autowired
    private EncounterObservationServiceHelper encounterObservationServiceHelper;

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

    private void validate(org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen){

        if(etSpecimen.getType()==null)
            throw new IllegalArgumentException("Sample Type is mandatory");

        if(etSpecimen.getDateCollected() == null)
            throw new IllegalArgumentException("Sample Date Collected detail is mandatory");
    }

    public Specimen createSpecimen(Encounter encounter,org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen){
        validate(etSpecimen);

        Specimen bacteriologySpecimen = new Specimen();
        bacteriologySpecimen.setId(etSpecimen.getIdentifier());
        bacteriologySpecimen.setDateCollected(etSpecimen.getDateCollected());

        if(StringUtils.isNotEmpty(etSpecimen.getExistingObs())){
            bacteriologySpecimen.setExistingObs(obsService.getObsByUuid(etSpecimen.getExistingObs()));
        }

        if(etSpecimen.getSample().getAdditionalAttributes() != null){
            EncounterTransaction.Observation etObs = etSpecimen.getSample().getAdditionalAttributes();
            bacteriologySpecimen.setAdditionalAttributes(encounterObservationServiceHelper.transformEtObs(bacteriologySpecimen.getExistingObs(), etObs));
        }

        bacteriologySpecimen.setType(getSampleTypeConcept(etSpecimen.getType()));

        if(etSpecimen.getReport() != null && etSpecimen.getReport().getResults() != null){
            EncounterTransaction.Observation etObs = etSpecimen.getReport().getResults();
            bacteriologySpecimen.setReports(encounterObservationServiceHelper.transformEtObs(bacteriologySpecimen.getReports(), etObs));
        }

        return bacteriologySpecimen;
    }

    private Concept getSampleTypeConcept(EncounterTransaction.Concept type) {
        Concept sampleType = conceptService.getConceptByUuid(type.getUuid());

        if(sampleType == null)
            throw new ConceptNotFoundException("Sample Type Concept "+ type +" is not available");

        return sampleType;
    }


}
