package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.module.bacteriology.api.MdrtbConcepts;
import org.openmrs.module.emrapi.concept.EmrConceptService;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptor;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptorField;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
public class SpecimenMetadataDescriptor extends ConceptSetDescriptor {

    public static final String MDRTB_CONCEPT_SOURCE_NAME = "org.openmrs.module.mdrtb";
    private Concept specimenId;
    private Concept specimenSource;//TYPE
    private Concept specimenDateCollected;
    private Concept specimenConstruct;

    private EmrConceptService emrConceptService;

    @Autowired
    public SpecimenMetadataDescriptor(ConceptService conceptService,EmrConceptService emrConceptService) {
        setup(conceptService, MDRTB_CONCEPT_SOURCE_NAME,
                ConceptSetDescriptorField.required("specimenConstruct", MdrtbConcepts.SPECIMEN_CONSTRUCT),
                ConceptSetDescriptorField.optional("specimenId", MdrtbConcepts.SPECIMEN_ID_CODE),
                ConceptSetDescriptorField.required("specimenSource", MdrtbConcepts.SAMPLE_SOURCE_CODE),
                ConceptSetDescriptorField.required("specimenDateCollected", MdrtbConcepts.SPECIMEN_DATE_COLLECTED));

        this.emrConceptService = emrConceptService;
    }

    public Concept getSpecimenDateCollected() {
        return specimenDateCollected;
    }

    public void setSpecimenDateCollected(Concept specimenDateCollected) {
        this.specimenDateCollected = specimenDateCollected;
    }

    public Concept getSpecimenConstruct() {
        return specimenConstruct;
    }

    public void setSpecimenConstruct(Concept specimenConstruct) {
        this.specimenConstruct = specimenConstruct;
    }

    public Concept getSpecimenId() {
        return specimenId;
    }

    public void setSpecimenId(Concept specimenId) {
        this.specimenId = specimenId;
    }

    public Concept getSpecimenSource() {
        return specimenSource;
    }

    public void setSpecimenSource(Concept specimenSource) {
        this.specimenSource = specimenSource;
    }

    public Obs buildObsGroup(Specimen specimen) {
        Assert.notNull(specimen.getSample());

        Obs dateCollected = new Obs();
        dateCollected.setConcept(getSpecimenDateCollected());
        dateCollected.setValueDate(specimen.getSample().getDateCollected());

        Obs group = new Obs();
        group.setConcept(getSpecimenConstruct());
        group.addGroupMember(buildValueObs(getSpecimenId(), specimen.getSample().getIdentifier()));
        group.addGroupMember(buildCoded(getSpecimenSource(), specimen.getSample().getType()));
        group.addGroupMember(dateCollected);
        group.addGroupMember(transformETObsToObs(specimen.getSample().getObservations()));

        return group;
    }

    private Obs transformETObsToObs(List<EncounterTransaction.Observation> observations) {
        return null;
    }

    private Obs buildCoded(Concept concept,String conceptAnswerCode){
        Obs appearance = new Obs();
        appearance.setConcept(concept);
        appearance.setValueCoded(emrConceptService.getConcept(conceptAnswerCode));

        return appearance;
    }

    private Obs buildValueObs(Concept concept, String answerText){
        Obs idObs = new Obs();
        idObs.setConcept(concept);
        idObs.setValueText(answerText);
        return idObs;
    }

}