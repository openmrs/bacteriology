package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.*;
import org.openmrs.api.ConceptService;
import org.openmrs.module.bacteriology.BacteriologyConstants;
import org.openmrs.module.bacteriology.api.BacteriologyConcepts;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptor;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptorField;
import org.openmrs.util.OpenmrsUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SpecimenMetadataDescriptor extends ConceptSetDescriptor {
    private Concept specimenId;
    private Concept specimenSource;
    private Concept specimenDateCollected;
    private Concept specimenConstruct;
    private Concept specimenAdditionalAttributes;
    private Concept specimenTestResults;

    public SpecimenMetadataDescriptor(ConceptService conceptService) {
        setup(conceptService, BacteriologyConstants.BACTERIOLOGY_CONCEPT_SOURCE,
                ConceptSetDescriptorField.required("specimenConstruct", BacteriologyConcepts.BACTERIOLOGY_CONCEPT_SET),
                ConceptSetDescriptorField.optional("specimenId", BacteriologyConcepts.SPECIMEN_ID_CODE),
                ConceptSetDescriptorField.required("specimenSource", BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE),
                ConceptSetDescriptorField.required("specimenDateCollected", BacteriologyConcepts.SPECIMEN_COLLECTION_DATE));

        setSpecimenAdditionalAttributes(getConceptByClass(conceptService, BacteriologyConstants.BACTERIOLOGY_ATTRIBUTES_CONCEPT_CLASS));
        setSpecimenTestResults(getConceptByClass(conceptService, BacteriologyConstants.BACTERIOLOGY_RESULTS_CONCEPT_CLASS));
    }


    public SpecimenMetadataDescriptor() {
    }

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

    public void setSpecimenAdditionalAttributes(Concept specimenAdditionalAttributes) {
        this.specimenAdditionalAttributes = specimenAdditionalAttributes;
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


    public Concept getSpecimenAdditionalAttributes() {
        return specimenAdditionalAttributes;
    }

    public Concept getSpecimenTestResults() {
        return specimenTestResults;
    }

    public void setSpecimenTestResults(Concept specimenTestResults) {
        this.specimenTestResults = specimenTestResults;
    }

    public Obs buildObsGroup(Specimen specimen) {
        if (specimen.getExistingObs() != null) {
            specimen.getExistingObs().setVoided(specimen.isVoided());
            setCodedMember(specimen.getExistingObs(), getSpecimenSource(), specimen.getType(), null);
            setFreeTextMember(specimen.getExistingObs(), getSpecimenDateCollected(), specimen.getDateCollected());
            setFreeTextMember(specimen.getExistingObs(), getSpecimenId(), specimen.getId());
            specimen.getExistingObs().addGroupMember(specimen.getAdditionalAttributes());
            specimen.getExistingObs().addGroupMember(specimen.getReports());
            return specimen.getExistingObs();
        } else {
            Obs obs = new Obs();
            Obs specimenSource = buildObsFor(getSpecimenSource(), specimen.getType(), null);
            Obs dateCollected = buildObsFor(getSpecimenDateCollected(), specimen.getDateCollected());
            if(specimen.getId()!=null ) {
                Obs specimenId = buildObsFor(getSpecimenId(), specimen.getId());
                obs.addGroupMember(specimenId);
            }
            Obs additionalAttributes = specimen.getAdditionalAttributes();
            obs.setVoided(specimen.isVoided());
            obs.setConcept(getSpecimenConstruct());
            obs.addGroupMember(specimenSource);
            obs.addGroupMember(dateCollected);
            obs.addGroupMember(specimen.getReports());
            obs.addGroupMember(additionalAttributes);
            return obs;
        }
    }

    public boolean isSpecimen(Obs obsGroup) {
        return obsGroup.getConcept().equals(this.specimenConstruct);
    }

    public Specimen buildSpecimen(Obs obsGroup) {
        if(!isSpecimen(obsGroup))
            return null;

        Specimen specimen = new Specimen();
        specimen.setExistingObs(obsGroup);
        specimen.setUuid(obsGroup.getUuid());
        specimen.setDateCollected(findMember(obsGroup, getSpecimenDateCollected()).getValueDate());
        Obs specimenIdObs=findMember(obsGroup, getSpecimenId());
        if(specimenIdObs!=null)
        specimen.setId(specimenIdObs.getValueText());
        if (getSpecimenSource() != null) {
            specimen.setType(findMember(obsGroup, getSpecimenSource()).getValueCoded());
        }

        if (getSpecimenAdditionalAttributes() != null) {
            specimen.setAdditionalAttributes(findMember(obsGroup, getSpecimenAdditionalAttributes()));
        }

        if (getSpecimenTestResults() != null) {
            specimen.setReports(findMember(obsGroup, getSpecimenTestResults()));
        }
        return specimen;
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
                if (hasConceptMapping(answerConcept, BacteriologyConstants.BACTERIOLOGY_CONCEPT_SOURCE, codeForAnswer)) {
                    return answerConcept;
                }
            }
        }
        throw new IllegalStateException("Cannot find answer mapped with " + BacteriologyConstants.BACTERIOLOGY_CONCEPT_SOURCE + ":" + codeForAnswer + " in the concept " + concept.getName());
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

    private Concept getConceptByClass(ConceptService conceptService, String conceptClassName) {
        ConceptClass conceptClass = conceptService.getConceptClassByName(conceptClassName);
        List<Concept> children = specimenConstruct.getSetMembers();
        for (Concept child : children) {
            if (child.getConceptClass().equals(conceptClass)) {
                return child;
            }
        }
        return null;
    }

    public List<Specimen> getSpecimenFromObs(Set<Obs> obsAtTopLevel) {

        List<Specimen> specimens = new ArrayList<Specimen>();

        for(Obs obs: obsAtTopLevel){
            if(isSpecimen(obs)){
                specimens.add(buildSpecimen(obs));
            }
        }
        return specimens;
    }
}
