package org.openmrs.module.bacteriology.api.specimen;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class SpecimenMetadataDescriptorTest {

    @Mock
    private Concept specimenId;

    @Mock
    private Concept specimenDateCollected;

    @Mock
    private Concept specimenConstruct;

    @Mock
    private ConceptService conceptService;

    @Mock
    private Concept specimenSource;

    @Mock
    private Concept specimenAdditionalAttributes;

    @Mock
    private Concept specimenTestResults;

    @Mock
    private Concept labName;

    @Mock
    private Concept typeOfVisit;

    @Mock
    private Concept sputum;

    @Mock
    private Concept tuberculosisSpecimenConstruct;

    private SpecimenMetadataDescriptor metadataDescriptor;

    @Before
    public void setUp() {
        initMocks(this);

        metadataDescriptor = new SpecimenMetadataDescriptor();
        metadataDescriptor.setSpecimenSource(specimenSource);
        metadataDescriptor.setSpecimenConstruct(specimenConstruct);
        metadataDescriptor.setSpecimenDateCollected(specimenDateCollected);
        metadataDescriptor.setSpecimenId(specimenId);
        metadataDescriptor.setSpecimenAdditionalAttributes(specimenAdditionalAttributes);
        metadataDescriptor.setSpecimenTestResults(specimenTestResults);
    }

    @Test
    public void buildSpecimenObsWithoutAnyExistingObs() {
        Date dateCollected = new Date();

        Specimen specimen = new Specimen();
        specimen.setId("id");
        specimen.setType(sputum);
        specimen.setDateCollected(dateCollected);
        specimen.setAdditionalAttributes(setupSpecimenAdditionalAttributes());
        specimen.setExistingObs(null);

        Obs specimenObs = metadataDescriptor.buildObsGroup(specimen);
        assertEquals(specimenConstruct, specimenObs.getConcept());
        Set<Obs> childObs = specimenObs.getGroupMembers();

        Obs dateCollectedObs = getObsWithConcept(childObs, specimenDateCollected);
        Assert.assertNotNull(dateCollectedObs);
        Assert.assertEquals(dateCollected, dateCollectedObs.getValueDate());

        Obs specId = getObsWithConcept(childObs, specimenId);
        Assert.assertNotNull(specId);
        Assert.assertEquals("id", specId.getValueText());

        Obs additionalAttributes = getObsWithConcept(childObs, specimenAdditionalAttributes);
        Assert.assertNotNull(additionalAttributes);

        Obs labNameObs = getObsWithConcept(additionalAttributes.getGroupMembers(), labName);
        Assert.assertNotNull(labNameObs);
        Assert.assertEquals("Some Lab Name", labNameObs.getValueText());

        Obs typeOfVisitObs = getObsWithConcept(additionalAttributes.getGroupMembers(), typeOfVisit);
        Assert.assertNotNull(typeOfVisitObs);
        Assert.assertEquals("Scheduled", typeOfVisitObs.getValueText());

    }

    @Test
    public void buildSpecimenObsWithoutAdditionalAttributesAndReports() {
        Date dateCollected = new Date();

        Specimen specimen = new Specimen();
        specimen.setId("id");
        specimen.setType(sputum);
        specimen.setDateCollected(dateCollected);
        specimen.setAdditionalAttributes(null);
        specimen.setExistingObs(null);
        specimen.setReports(null);

        Obs specimenObs = metadataDescriptor.buildObsGroup(specimen);
        assertEquals(specimenConstruct, specimenObs.getConcept());
        Set<Obs> childObs = specimenObs.getGroupMembers();

        Assert.assertEquals(3, childObs.size());

        Obs specId = getObsWithConcept(childObs, specimenId);
        Assert.assertNotNull(specId);
        Assert.assertEquals("id", specId.getValueText());
    }

    @Test
    public void buildSpecimenObsWithReports() {
        Date dateCollected = new Date();

        Specimen specimen = new Specimen();
        specimen.setId("latestid");
        specimen.setType(sputum);
        specimen.setDateCollected(dateCollected);
        specimen.setReports(setupSpecimenAdditionalAttributes());
        specimen.setExistingObs(getExistingObsInDb());

        Obs specimenObs = metadataDescriptor.buildObsGroup(specimen);
        assertEquals(specimenConstruct, specimenObs.getConcept());
        Set<Obs> childObs = specimenObs.getGroupMembers();

        Obs dateCollectedObs = getObsWithConcept(childObs, specimenDateCollected);
        Assert.assertNotNull(dateCollectedObs);
        Assert.assertEquals(dateCollected, dateCollectedObs.getValueDate());

        Obs specId = getObsWithConcept(childObs, specimenId);
        Assert.assertNotNull(specId);
        Assert.assertEquals("latestid", specId.getValueText()); //id is modified from what is being setup

        Obs additonalAttributes = getObsWithConcept(childObs, specimenAdditionalAttributes);
        Assert.assertNotNull(additonalAttributes);

        Obs labNameObs = getObsWithConcept(additonalAttributes.getGroupMembers(), labName);
        Assert.assertNotNull(labNameObs);
        Assert.assertEquals("Some Lab Name", labNameObs.getValueText());

        Obs typeOfVisitObs = getObsWithConcept(additonalAttributes.getGroupMembers(), typeOfVisit);
        Assert.assertNotNull(typeOfVisitObs);
        Assert.assertEquals("Scheduled", typeOfVisitObs.getValueText());

    }


    @Test
    public void buildSpecimenObsWithSomeExistingObs() {
        Date dateCollected = new Date();

        Specimen specimen = new Specimen();
        specimen.setId("latestid");
        specimen.setType(sputum);
        specimen.setDateCollected(dateCollected);
        specimen.setAdditionalAttributes(setupSpecimenAdditionalAttributes());
        specimen.setExistingObs(getExistingObsInDb());

        Obs specimenObs = metadataDescriptor.buildObsGroup(specimen);
        assertEquals(specimenConstruct, specimenObs.getConcept());
        Set<Obs> childObs = specimenObs.getGroupMembers();

        Obs dateCollectedObs = getObsWithConcept(childObs, specimenDateCollected);
        Assert.assertNotNull(dateCollectedObs);
        Assert.assertEquals(dateCollected, dateCollectedObs.getValueDate());

        Obs specId = getObsWithConcept(childObs, specimenId);
        Assert.assertNotNull(specId);
        Assert.assertEquals("latestid", specId.getValueText()); //id is modified from what is being setup

        Obs additonalAttributes = getObsWithConcept(childObs, specimenAdditionalAttributes);
        Assert.assertNotNull(additonalAttributes);

        Obs labNameObs = getObsWithConcept(additonalAttributes.getGroupMembers(), labName);
        Assert.assertNotNull(labNameObs);
        Assert.assertEquals("Some Lab Name", labNameObs.getValueText());

        Obs typeOfVisitObs = getObsWithConcept(additonalAttributes.getGroupMembers(), typeOfVisit);
        Assert.assertNotNull(typeOfVisitObs);
        Assert.assertEquals("Scheduled", typeOfVisitObs.getValueText());

    }


    @Test
    public void buildSpecimenUsingObsGroup() {
        Obs obsGroup = new Obs();
        obsGroup.setConcept(tuberculosisSpecimenConstruct);
        Obs idObs = new Obs();
        idObs.setConcept(specimenId);
        idObs.setValueText("123");
        obsGroup.addGroupMember(idObs);
        Obs dateCollectedObs = new Obs();
        dateCollectedObs.setConcept(specimenDateCollected);
        dateCollectedObs.setValueDatetime(new Date());
        obsGroup.addGroupMember(dateCollectedObs);
        Obs additionalAttributes = new Obs();
        additionalAttributes.setConcept(specimenAdditionalAttributes);
        obsGroup.addGroupMember(additionalAttributes);
        Specimen specimen = metadataDescriptor.buildSpecimen(obsGroup);


        assertEquals(specimen.getId(), idObs.getValueText());
        assertEquals(specimen.getAdditionalAttributes(), additionalAttributes);
        assertEquals(specimen.getDateCollected(), dateCollectedObs.getValueDatetime());

    }


    private Obs getObsWithConcept(Set<Obs> allObs, Concept concept) {
        for (Obs obs : allObs) {
            if (obs.getConcept().equals(concept))
                return obs;
        }
        return null;
    }

    private Obs getExistingObsInDb() {
        Obs obs = createObs(specimenConstruct, "");
        obs.addGroupMember(createObs(specimenId, "initialId"));
        obs.addGroupMember(createObs(specimenSource, sputum));
        return obs;
    }

    private Obs createObs(Concept concept, String value) {
        Obs obs = new Obs();
        obs.setId(RandomUtils.nextInt());
        obs.setUuid(UUID.randomUUID().toString());
        obs.setConcept(concept);
        obs.setValueText(value);
        return obs;
    }

    private Obs createObs(Concept concept, Concept value) {
        Obs obs = new Obs();
        obs.setId(RandomUtils.nextInt());
        obs.setUuid(UUID.randomUUID().toString());
        obs.setConcept(concept);
        obs.setValueCoded(value);
        return obs;
    }


    private Obs setupSpecimenAdditionalAttributes() {
        Obs labNameObs = new Obs();
        labNameObs.setConcept(labName);
        labNameObs.setValueText("Some Lab Name");

        Obs typeOfVisitObs = new Obs();
        typeOfVisitObs.setConcept(typeOfVisit);
        typeOfVisitObs.setValueText("Scheduled");

        Obs obsGroup = new Obs();
        obsGroup.setConcept(specimenAdditionalAttributes);
        obsGroup.addGroupMember(labNameObs);
        obsGroup.addGroupMember(typeOfVisitObs);

        return obsGroup;
    }

}