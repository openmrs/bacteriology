package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.module.bacteriology.api.MdrtbConcepts;
import org.openmrs.module.emrapi.concept.EmrConceptService;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptor;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptorField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecimenMetadataDescriptor extends ConceptSetDescriptor {

    public static final String MDRTB_CONCEPT_SOURCE_NAME = "org.openmrs.module.mdrtb";
    private Concept specimenId;
    private Concept specimenAppearance;
    private Concept specimenComments;
    private Concept specimenSource;//TYPE
    private Concept specimenDateCollected;
    private Concept specimenConstruct;

    private EmrConceptService emrConceptService;

    @Autowired
    public SpecimenMetadataDescriptor(ConceptService conceptService,EmrConceptService emrConceptService) {
        setup(conceptService, MDRTB_CONCEPT_SOURCE_NAME,
                ConceptSetDescriptorField.required("specimenConstruct", MdrtbConcepts.SPECIMEN_CONSTRUCT),
                ConceptSetDescriptorField.optional("specimenId", MdrtbConcepts.SPECIMEN_ID_CODE),
                ConceptSetDescriptorField.required("specimenAppearance", MdrtbConcepts.SPECIMEN_APPEARANCE_CODE),
                ConceptSetDescriptorField.optional("specimenComments", MdrtbConcepts.SPECIMEN_COMMENTS_CODE),
                ConceptSetDescriptorField.required("specimenSource", MdrtbConcepts.SAMPLE_SOURCE_CODE),
                ConceptSetDescriptorField.required("specimenDateCollected", MdrtbConcepts.SPECIMEN_DATE_COLLECTED)
        );

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

    public Concept getSpecimenAppearance() {
        return specimenAppearance;
    }

    public void setSpecimenAppearance(Concept specimenAppearance) {
        this.specimenAppearance = specimenAppearance;
    }

    public Concept getSpecimenComments() {
        return specimenComments;
    }

    public void setSpecimenComments(Concept specimenComments) {
        this.specimenComments = specimenComments;
    }

    public Concept getSpecimenSource() {
        return specimenSource;
    }

    public void setSpecimenSource(Concept specimenSource) {
        this.specimenSource = specimenSource;
    }

    public Obs buildObsGroup(Specimen specimen) {
        Obs dateCollected = new Obs();
        dateCollected.setConcept(getSpecimenDateCollected());
        dateCollected.setValueDate(specimen.getDateCollected());

        Obs group = new Obs();
        group.setConcept(getSpecimenConstruct());
        group.addGroupMember(buildValueObs(getSpecimenId(), specimen.getIdentifier()));
        group.addGroupMember(buildCoded(getSpecimenAppearance(), specimen.getAppearance()));
        group.addGroupMember(buildValueObs(getSpecimenComments(), specimen.getComments()));
        group.addGroupMember(buildCoded(getSpecimenSource(), specimen.getType()));
        group.addGroupMember(dateCollected);

        return group;
    }

    private Obs buildCoded(Concept concept,String conceptAnswerCode){
        Obs appearance = new Obs();
        appearance.setConcept(getSpecimenAppearance());
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