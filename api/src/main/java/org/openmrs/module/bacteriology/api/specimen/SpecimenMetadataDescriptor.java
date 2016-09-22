package org.openmrs.module.bacteriology.api.specimen;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.module.bacteriology.BacteriologyConstants;
import org.openmrs.module.bacteriology.api.BacteriologyConcepts;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptor;
import org.openmrs.module.emrapi.descriptor.ConceptSetDescriptorField;

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
    private Concept specimenSourceFreeText;

    private static SpecimenMetadataDescriptor specimenMetadataDescriptor;

    public static SpecimenMetadataDescriptor get(ConceptService conceptService){
        if(specimenMetadataDescriptor == null){
            specimenMetadataDescriptor = new SpecimenMetadataDescriptor(conceptService);
        }

        return specimenMetadataDescriptor;
    }

    private SpecimenMetadataDescriptor(ConceptService conceptService) {
        setup(conceptService, BacteriologyConstants.BACTERIOLOGY_CONCEPT_SOURCE,
                ConceptSetDescriptorField.required("specimenConstruct", BacteriologyConcepts.BACTERIOLOGY_CONCEPT_SET),
                ConceptSetDescriptorField.optional("specimenId", BacteriologyConcepts.SPECIMEN_ID_CODE),
                ConceptSetDescriptorField.required("specimenSource", BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE),
                ConceptSetDescriptorField
                        .optional("specimenSourceFreeText", BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE_FREE_TEXT),
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
            setSpecimenSourceMember(specimen.getExistingObs(), getSpecimenSource(), specimen.getType());
            setFreeTextMember(specimen.getExistingObs(), getSpecimenDateCollected(), specimen.getDateCollected());
            setFreeTextMember(specimen.getExistingObs(), getSpecimenId(), specimen.getId());
            setFreeTextMember(specimen.getExistingObs(),getSpecimenSourceFreeText(), specimen.getTypeFreeText());
            specimen.getExistingObs().addGroupMember(specimen.getAdditionalAttributes());
            specimen.getExistingObs().addGroupMember(specimen.getReports());
            specimen.getExistingObs().setObsDatetime(specimen.getDateCollected());
            if(CollectionUtils.isNotEmpty(specimen.getExistingObs().getGroupMembers())) {
                setGroupMembersObsDateTime(specimen.getExistingObs().getGroupMembers(), specimen.getDateCollected());
            }
            return specimen.getExistingObs();
        } else {
            Obs obsGroup = new Obs();
            obsGroup.setObsDatetime(specimen.getDateCollected());
//            setCodedMember(obsGroup, getSpecimenSource(), specimen.getType(), null);
            Obs specimenSource = buildObsFor(getSpecimenSource(), specimen.getType(), null);
            specimenSource.setObsDatetime(obsGroup.getObsDatetime());
            Obs dateCollected = buildObsFor(getSpecimenDateCollected(), specimen.getDateCollected());
            dateCollected.setObsDatetime(obsGroup.getObsDatetime());
            if(specimen.getId()!=null ) {
                Obs specimenId = buildObsFor(getSpecimenId(), specimen.getId());
                specimenId.setObsDatetime(obsGroup.getObsDatetime());
                obsGroup.addGroupMember(specimenId);
//                setFreeTextMember(obsGroup, getSpecimenId(), specimen.getId());
            }
            setFreeTextMember(obsGroup,getSpecimenSourceFreeText(),specimen.getTypeFreeText());

            Obs additionalAttributes = specimen.getAdditionalAttributes();
            if(additionalAttributes != null)
              additionalAttributes.setObsDatetime(obsGroup.getObsDatetime());
            obsGroup.setVoided(specimen.isVoided());
            obsGroup.setConcept(getSpecimenConstruct());
            obsGroup.addGroupMember(specimenSource);
            obsGroup.addGroupMember(dateCollected);
            obsGroup.addGroupMember(specimen.getReports());
            obsGroup.addGroupMember(additionalAttributes);
            setGroupMembersObsDateTime(obsGroup.getGroupMembers(), specimen.getDateCollected());
            return obsGroup;
        }
    }

    private void setGroupMembersObsDateTime(Set<Obs> groupMembers, Date dateCollected) {
        for(Obs groupMemberObs : groupMembers) {
            groupMemberObs.setObsDatetime(dateCollected);
            if(CollectionUtils.isNotEmpty(groupMemberObs.getGroupMembers())) {
                setGroupMembersObsDateTime(groupMemberObs.getGroupMembers(), dateCollected);
            }
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

        Obs specimenSourceFreeText = findMember(obsGroup, getSpecimenSourceFreeText());
        if(specimenSourceFreeText!=null){
            specimen.setTypeFreeText(specimenSourceFreeText.getValueText());
        }
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


    private void setSpecimenSourceMember(Obs existingObs, Concept memberConcept, Concept memberAnswer) {
        Obs specimenSourceMember = findMember(existingObs, memberConcept);
        boolean needToCreate = memberConcept != null && (specimenSourceMember == null);
        if (needToCreate) {
            existingObs.addGroupMember(buildObsFor(memberConcept, memberAnswer, null));
        } else
            specimenSourceMember.setValueCoded(memberAnswer);
    }

    private void setFreeTextMember(Obs obsGroup, Concept memberConcept, Date memberAnswer) {
        Obs member = findMember(obsGroup, memberConcept);
        boolean needToCreate = memberAnswer != null && (member == null);
        if (needToCreate) {
            addToObsGroup(obsGroup, buildObsFor(memberConcept, memberAnswer));
        } else if (member != null) {
            member.setValueDate(memberAnswer);
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
        boolean needToCreate = memberAnswer != null && (member == null);
        if (needToCreate) {
            addToObsGroup(obsGroup, buildObsFor(memberConcept, memberAnswer));
        } else if(member != null){
            member.setValueText(memberAnswer);
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

    public List<Obs> getSpecimenObsGroups(Set<Obs> obsAtTopLevel) {
        List<Obs> obsGroup = new ArrayList<Obs>();

        for(Obs obs: obsAtTopLevel){
            if(isSpecimen(obs)){
                obsGroup.add(obs);
            }
        }
        return obsGroup;
    }

    public Concept getSpecimenSourceFreeText() {
        return specimenSourceFreeText;
    }

    public void setSpecimenSourceFreeText(Concept specimenSourceFreeText) {
        this.specimenSourceFreeText = specimenSourceFreeText;
    }
}
