package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.module.bacteriology.BacteriologyConstants;
import org.openmrs.module.bacteriology.api.MdrtbConcepts;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptor;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptorField;
import org.openmrs.util.OpenmrsUtil;

import java.util.Date;

public class SpecimenMetadataDescriptor extends ConceptSetDescriptor {
    private Concept specimenId;
    private Concept specimenSource;//TYPE
    private Concept specimenDateCollected;
    private Concept specimenConstruct;

    public SpecimenMetadataDescriptor(ConceptService conceptService) {
        setup(conceptService, BacteriologyConstants.MDRTB_CONCEPT_SOURCE_NAME,
                ConceptSetDescriptorField.required("specimenConstruct", MdrtbConcepts.SPECIMEN_CONSTRUCT),
                ConceptSetDescriptorField.optional("specimenId", MdrtbConcepts.SPECIMEN_ID_CODE),
                ConceptSetDescriptorField.required("specimenSource", MdrtbConcepts.SAMPLE_SOURCE_CODE),
                ConceptSetDescriptorField.required("specimenDateCollected", MdrtbConcepts.SPECIMEN_DATE_COLLECTED));
    }

    public SpecimenMetadataDescriptor(){}

    public void setSpecimenId(Concept specimenId) {
        this.specimenId = specimenId;
    }

    public void setSpecimenSource(Concept specimenSource) {
        this.specimenSource = specimenSource;
    }

    public void setSpecimenDateCollected(Concept specimenDateCollected) {
        this.specimenDateCollected = specimenDateCollected;
    }

    public void setSpecimenConstruct(Concept specimenConstruct) {
        this.specimenConstruct = specimenConstruct;
    }

    public Concept getSpecimenDateCollected() {
        return specimenDateCollected;
    }

    public Concept getSpecimenConstruct() {
        return specimenConstruct;
    }

    public Concept getSpecimenId() {
        return specimenId;
    }

    public Concept getSpecimenSource() {
        return specimenSource;
    }

    public Obs buildObsGroup(Specimen specimen) {
        Concept sampleType = findAnswer(getSpecimenSource(), specimen.getType().getCodeInEmrConceptSource());
        //TODO: Add liquibase migration for the pre-defined concepts.

        if(specimen.getExistingObs()!=null){
            setCodedMember(specimen.getExistingObs(), getSpecimenSource(), sampleType, null);
            setFreeTextMember(specimen.getExistingObs(), getSpecimenDateCollected(), specimen.getDateCollected());
            setFreeTextMember(specimen.getExistingObs(),getSpecimenId(),specimen.getId());
            specimen.getExistingObs().addGroupMember(specimen.getAdditionalAttributes());
            return specimen.getExistingObs();
        }else{
            Obs specimenSource = buildObsFor(getSpecimenSource(), sampleType, null);
            Obs dateCollected = buildObsFor(getSpecimenDateCollected(), specimen.getDateCollected());
            Obs specimenId = buildObsFor(getSpecimenId(),specimen.getId());

            Obs obs = new Obs();
            obs.setConcept(getSpecimenConstruct());
            obs.addGroupMember(specimenSource);
            obs.addGroupMember(dateCollected);
            obs.addGroupMember(specimenId);
            obs.addGroupMember(specimen.getAdditionalAttributes());
            return obs;
        }
    }

    private void setFreeTextMember(Obs obsGroup, Concept memberConcept, Date memberAnswer) {
        Obs member = findMember(obsGroup, memberConcept);
        boolean needToVoid = member != null && !OpenmrsUtil.nullSafeEquals(memberAnswer, member.getValueDate());
        boolean needToCreate = memberAnswer != null && (member == null || needToVoid);
        if (needToVoid) {
            member.setVoided(true);
            member.setVoidReason(getDefaultVoidReason());
        }
        if (needToCreate) {
            addToObsGroup(obsGroup, buildObsFor(memberConcept, memberAnswer));
        }
    }

    protected Obs buildObsFor(Concept question, Date answer) {
        Obs obs = new Obs();
        obs.setConcept(question);
        obs.setValueDate(answer);
        return obs;
    }

    private void addToObsGroup(Obs obsGroup, Obs member) {
        member.setPerson(obsGroup.getPerson());
        member.setObsDatetime(obsGroup.getObsDatetime());
        member.setLocation(obsGroup.getLocation());
        member.setEncounter(obsGroup.getEncounter());
        obsGroup.addGroupMember(member);
    }

    private void setFreeTextMember(Obs obsGroup, Concept memberConcept, String memberAnswer) {
        Obs member = findMember(obsGroup, memberConcept);
        boolean needToVoid = member != null && !OpenmrsUtil.nullSafeEquals(memberAnswer, member.getValueText());
        boolean needToCreate = memberAnswer != null && (member == null || needToVoid);
        if (needToVoid) {
            member.setVoided(true);
            member.setVoidReason(getDefaultVoidReason());
        }
        if (needToCreate) {
            addToObsGroup(obsGroup, buildObsFor(memberConcept, memberAnswer));
        }
    }

    //TODO: refactor this in emrapi to allow passing of ConceptSourceName
    protected Concept findAnswer(Concept concept, String codeForAnswer) {
        for (ConceptAnswer conceptAnswer : concept.getAnswers()) {
            Concept answerConcept = conceptAnswer.getAnswerConcept();
            if (answerConcept != null) {
                if (hasConceptMapping(answerConcept, BacteriologyConstants.MDRTB_CONCEPT_SOURCE_NAME, codeForAnswer)) {
                    return answerConcept;
                }
            }
        }
        throw new IllegalStateException("Cannot find answer mapped with " + BacteriologyConstants.MDRTB_CONCEPT_SOURCE_NAME + ":" + codeForAnswer + " in the concept " + concept.getName());
    }

    private boolean hasConceptMapping(Concept concept, String sourceName, String codeToLookFor) {
        for (ConceptMap conceptMap : concept.getConceptMappings()) {
            ConceptReferenceTerm conceptReferenceTerm = conceptMap.getConceptReferenceTerm();
            if (sourceName.equals(conceptReferenceTerm.getConceptSource().getName()) && codeToLookFor.equals(conceptReferenceTerm.getCode())) {
                return true;
            }
        }
        return false;
    }


}