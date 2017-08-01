package org.openmrs.module.bacteriology.api.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bacteriology.api.BacteriologyConcepts;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimens;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.utils.HibernateLazyLoader;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BacteriologyServiceTest extends BaseModuleContextSensitiveTest {

    private BacteriologyService bacteriologyService;

    @Autowired
    private EncounterService encounterService;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private ObsService obsService;

    @Before
    public void setup() throws Exception {
        executeDataSet("baseBacteriologyData.xml");
        executeDataSet("specimenTestData.xml");
        bacteriologyService =  Context.getService(BacteriologyService.class);
    }

    @Test
    public void ensureEncounterIsUpdatedWithNewSpecimenObs() throws Exception {
        Encounter encounter = encounterService.getEncounter(3);

        Resource etRequest = new ClassPathResource("encounterTransactionWithBasicSpecimen.json");
        EncounterTransaction encounterTransaction = new ObjectMapper().readValue(etRequest.getInputStream(), EncounterTransaction.class);
        bacteriologyService.updateEncounter(encounter, encounterTransaction);

        Obs specimenObs = findMember(encounter.getObsAtTopLevel(false), getConcept(BacteriologyConcepts.BACTERIOLOGY_CONCEPT_SET));
        assertNotNull(specimenObs);

        Obs specimenId = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_ID_CODE));
        assertNotNull(specimenId);
        assertEquals("SAMP1234", specimenId.getValueText());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Obs specimenDateCollected = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_COLLECTION_DATE));
        assertNotNull(specimenDateCollected);
        assertEquals("2015-09-18", sdf.format(specimenDateCollected.getValueDate()));

        Obs specimenSource = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE));
        assertNotNull(specimenSource);
        assertNotNull(specimenSource.getValueCoded());
        assertEquals(getConcept("URINE"), specimenSource.getValueCoded());
    }

    @Test
    public void ensureEncounterIsUpdatedWithExistingSpecimenObs() throws Exception {
        executeDataSet("existingSpecimenObs.xml");
        Encounter encounter = encounterService.getEncounter(102);

        Resource etRequest = new ClassPathResource("encounterTransactionWithBasicSpecimenExistingObs.json");
        EncounterTransaction encounterTransaction = new ObjectMapper().readValue(etRequest.getInputStream(), EncounterTransaction.class);
        bacteriologyService.updateEncounter(encounter, encounterTransaction);

        Obs specimenObs = findMember(encounter.getObsAtTopLevel(false), getConcept(BacteriologyConcepts.BACTERIOLOGY_CONCEPT_SET));
        assertNotNull(specimenObs);

        Obs specimenId = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_ID_CODE));
        assertNotNull(specimenId);
        assertEquals("SAMP1234", specimenId.getValueText());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Obs specimenDateCollected = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_COLLECTION_DATE));
        assertNotNull(specimenDateCollected);
        assertEquals("2015-09-18", sdf.format(specimenDateCollected.getValueDate()));

        Obs specimenSource = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE));
        assertNotNull(specimenSource);
        assertNotNull(specimenSource.getValueCoded());
        assertEquals(getConcept("URINE"), specimenSource.getValueCoded());

        Obs additionalAttributes = findMember(specimenObs.getGroupMembers(), getConcept("BACTERIOLOGY ADDITIONAL ATTRIBUTES"));
        assertNotNull(additionalAttributes);

        Obs weight = findMember(additionalAttributes.getGroupMembers(), getConcept("WEIGHT (KG)"));
        assertNotNull(weight);
        assertEquals(70, weight.getValueNumeric().intValue());

        Obs results = findMember(specimenObs.getGroupMembers(), getConcept("BACTERIOLOGY RESULTS"));
        assertNotNull(results);

        Obs weightResult = findMember(results.getGroupMembers(), getConcept("WEIGHT (KG)"));
        assertNotNull(weightResult);
        assertEquals(360, weightResult.getValueNumeric().intValue());
    }

    @Test
    public void ensureExistingSpecimenObsVoidedWhenSpecimenIsVoided() throws Exception {
        executeDataSet("existingSpecimenObs.xml");
        Encounter encounter = encounterService.getEncounter(102);

        Resource etRequest = new ClassPathResource("encounterTransactionWithSpecimenVoided.json");
        EncounterTransaction encounterTransaction = new ObjectMapper().readValue(etRequest.getInputStream(), EncounterTransaction.class);
        bacteriologyService.updateEncounter(encounter, encounterTransaction);

        Obs specimenObs = findMember(encounter.getObsAtTopLevel(true), getConcept(BacteriologyConcepts.BACTERIOLOGY_CONCEPT_SET));
        assertEquals(specimenObs.getVoided(), true);

        Obs specimenId = findMember(specimenObs.getGroupMembers(true), getConcept(BacteriologyConcepts.SPECIMEN_ID_CODE));
        assertNotNull(specimenId);
        assertEquals(specimenId.getVoided(), true);

        Obs specimenDateCollected = findMember(specimenObs.getGroupMembers(true), getConcept(BacteriologyConcepts.SPECIMEN_COLLECTION_DATE));
        assertEquals(specimenDateCollected.getVoided(), true);

        Obs specimenSource = findMember(specimenObs.getGroupMembers(true), getConcept(BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE));
        assertEquals(specimenSource.getVoided(), true);

        Obs additionalAttributes = findMember(specimenObs.getGroupMembers(true), getConcept("BACTERIOLOGY ADDITIONAL ATTRIBUTES"));
        assertEquals(additionalAttributes.getVoided(), true);

        Obs weight = findMember(additionalAttributes.getGroupMembers(true), getConcept("WEIGHT (KG)"));
        assertEquals(weight.getVoided(), true);

        Obs results = findMember(specimenObs.getGroupMembers(true), getConcept("BACTERIOLOGY RESULTS"));
        assertEquals(results.getVoided(), true);

        Obs weightResult = findMember(results.getGroupMembers(true), getConcept("WEIGHT (KG)"));
        assertEquals(weightResult.getVoided(), true);
    }

    @Test
    public void ensureEncounterIsUpdatedWithAdditionalAttributes() throws Exception {
        Encounter encounter = encounterService.getEncounter(3);

        Resource etRequest = new ClassPathResource("encounterTransactionWithAdditionalAttr.json");
        EncounterTransaction encounterTransaction = new ObjectMapper().readValue(etRequest.getInputStream(),
                EncounterTransaction.class);
        bacteriologyService.updateEncounter(encounter, encounterTransaction);

        Obs specimenObs = findMember(encounter.getObsAtTopLevel(false), getConcept(BacteriologyConcepts.BACTERIOLOGY_CONCEPT_SET));
        assertNotNull(specimenObs);

        Obs specimenId = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_ID_CODE));
        assertNotNull(specimenId);
        assertEquals("SAMP1234", specimenId.getValueText());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Obs specimenDateCollected = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_COLLECTION_DATE));
        assertNotNull(specimenDateCollected);
        assertEquals("2015-09-18", sdf.format(specimenDateCollected.getValueDate()));

        Obs specimenSource = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE));
        assertNotNull(specimenSource);
        assertNotNull(specimenSource.getValueCoded());
        assertEquals(getConcept("URINE"), specimenSource.getValueCoded());

        Obs additionalAttributes = findMember(specimenObs.getGroupMembers(), getConcept("BACTERIOLOGY ADDITIONAL ATTRIBUTES"));
        assertNotNull(additionalAttributes);

        Obs weight = findMember(additionalAttributes.getGroupMembers(), getConcept("WEIGHT (KG)"));
        assertNotNull(weight);
        assertEquals(45, weight.getValueNumeric().intValue());
    }

    @Test
    public void ensureEncounterIsUpdatedWithAdditionalAttributesAndReports() throws Exception {
        Encounter encounter = encounterService.getEncounter(3);

        Resource etRequest = new ClassPathResource("encounterTransactionWithAdditionalAttrAndReports.json");
        EncounterTransaction encounterTransaction = new ObjectMapper().readValue(etRequest.getInputStream(), EncounterTransaction.class);
        bacteriologyService.updateEncounter(encounter, encounterTransaction);

        Obs specimenObs = findMember(encounter.getObsAtTopLevel(false),
                getConcept(BacteriologyConcepts.BACTERIOLOGY_CONCEPT_SET));
        assertNotNull(specimenObs);

        Obs specimenId = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_ID_CODE));
        assertNotNull(specimenId);
        assertEquals("SAMP1234", specimenId.getValueText());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Obs specimenDateCollected = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_COLLECTION_DATE));
        assertNotNull(specimenDateCollected);
        assertEquals("2015-09-18", sdf.format(specimenDateCollected.getValueDate()));

        Obs specimenSource = findMember(specimenObs.getGroupMembers(), getConcept(BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE));
        assertNotNull(specimenSource);
        assertNotNull(specimenSource.getValueCoded());
        assertEquals(getConcept("URINE"), specimenSource.getValueCoded());

        Obs additionalAttributes = findMember(specimenObs.getGroupMembers(), getConcept("BACTERIOLOGY ADDITIONAL ATTRIBUTES"));
        assertNotNull(additionalAttributes);

        Obs weight = findMember(additionalAttributes.getGroupMembers(), getConcept("WEIGHT (KG)"));
        assertNotNull(weight);
        assertEquals(45, weight.getValueNumeric().intValue());

        Obs results = findMember(specimenObs.getGroupMembers(), getConcept("BACTERIOLOGY RESULTS"));
        assertNotNull(results);

        Obs weightResult = findMember(results.getGroupMembers(), getConcept("WEIGHT (KG)"));
        assertNotNull(weightResult);
        assertEquals(90, weightResult.getValueNumeric().intValue());
    }

    @Test
    public void ensureEncounterIsUpdatedWithTheGivenSpecimenData() throws Exception {
        executeDataSet("existingSpecimenObs.xml");

        Obs obsGroup = obsService.getObs(111);

        Specimen.TestReport testReport = new Specimen.TestReport();
        Obs bacteriologyResults = obsService.getObs(113);
        Obs bacteriologyResultsMember = bacteriologyResults.getGroupMembers().iterator().next();
        EncounterTransaction.Observation resultsObservation = new EncounterTransaction.Observation().setUuid(bacteriologyResults.getUuid()).setConcept(new EncounterTransaction.Concept(bacteriologyResults.getConcept().getUuid()));
        resultsObservation.addGroupMember(new EncounterTransaction.Observation().setUuid(bacteriologyResultsMember.getUuid()).setConcept(new EncounterTransaction.Concept(bacteriologyResultsMember.getConcept().getUuid())).setValue(105));
        testReport.setResults(resultsObservation);

        Specimen.Sample sample = new Specimen.Sample();
        Obs additionalAttribute = obsService.getObs(112);
        Obs additionalAttributeMember = additionalAttribute.getGroupMembers().iterator().next();
        EncounterTransaction.Observation additionalAttributes = new EncounterTransaction.Observation().setUuid(additionalAttribute.getUuid()).setConcept(new EncounterTransaction.Concept(additionalAttribute.getConcept().getUuid()));
        additionalAttributes.addGroupMember(new EncounterTransaction.Observation().setUuid(additionalAttributeMember.getUuid()).setConcept(new EncounterTransaction.Concept(additionalAttributeMember.getConcept().getUuid())).setValue(72));
        sample.setAdditionalAttributes(additionalAttributes);

        Concept specimenType = conceptService.getConcept(BacteriologyConcepts.SPECIMEN_SAMPLE_SOURCE);
        EncounterTransaction.Concept type = new EncounterTransaction.Concept();
        type.setUuid(specimenType.getUuid());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2015-11-04");

        Specimen specimen = new Specimen();
        specimen.setExistingObs(obsGroup.getUuid());
        specimen.setType(type);
        specimen.setSample(sample);
        specimen.setReport(testReport);
        specimen.setDateCollected(date);
        specimen.setIdentifier("identifier");
        specimen.setVoided(false);
        specimen.setUuid("specimenUuid");

        Specimen savedSpecimen = bacteriologyService.saveSpecimen(specimen);

        assertEquals(savedSpecimen.getSample().getAdditionalAttributes().getGroupMembers().iterator().next().getValue(), Integer.valueOf("72"));
        assertEquals(savedSpecimen.getReport().getResults().getGroupMembers().iterator().next().getValue(), Integer.valueOf("105"));
    }

    @Test
    public void ensureEncounterAndAllObsAreVoidedWhenSpecimenIsVoidedAndSaved() throws Exception {

        executeDataSet("existingSpecimenObs.xml");

        Obs obsGroup = obsService.getObs(100);
        Specimen specimen = bacteriologyService.getSpecimen(obsGroup);
        specimen.setVoided(true);
        bacteriologyService.saveSpecimen(specimen);

        assertAllVoidedAndChildrenVoided(Collections.singleton(obsGroup));
    }

    private void assertAllVoidedAndChildrenVoided(Set<Obs> obsList) {
        for (Obs obs : obsList) {
            assertTrue(obs.getVoided());
            if (obs.hasGroupMembers()) {
                assertAllVoidedAndChildrenVoided(obs.getGroupMembers());
            }
        }
    }

    @Test
    public void ensureDomainSpecimenIsTransformed(){
        Date dateCollected = new Date();

        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = new org.openmrs.module.bacteriology.api.specimen.Specimen();
        specimen.setTypeFreeText("other type");
        specimen.setType(getConcept("SPUTUM"));
        specimen.setDateCollected(dateCollected);
        specimen.setId("id");
        specimen.setUuid("uuid");
        Specimen specimen1 = bacteriologyService.createDomainSpecimen(specimen);
        assertEquals("SPUTUM", specimen1.getType().getName());
        assertEquals(dateCollected, specimen1.getDateCollected());
        assertEquals("id", specimen1.getIdentifier());
        assertEquals("other type", specimen1.getTypeFreeText());
    }

    @Test
    public void ensureGetSpecimenReturnsCorrectSpecimen() throws Exception{
        executeDataSet("existingSpecimenObs.xml");

        Obs obsGroup = obsService.getObs(100);

        Specimen specimen = bacteriologyService.getSpecimen(obsGroup);
        assertEquals("SAMPLE12345",specimen.getIdentifier());
        assertNull(specimen.getReport());
        assertEquals("e26cea2c-1b9f-4afe-b211-f3ef6c88afaa",specimen.getUuid());
        assertEquals("e26cea2c-1b9f-4afe-b211-f3ef6c88afaa",specimen.getExistingObs());
    }

    @Test
    public void ensureGetSpecimensReturnsCorrectSpecimens() throws Exception{
        executeDataSet("existingSpecimenObs.xml");

        Obs obsGroup = obsService.getObs(100);

        Collection<Obs> obsGroups = Arrays.asList(obsGroup);
        Specimens specimens = bacteriologyService.getSpecimens(obsGroups);

        assertEquals("SAMPLE12345", specimens.get(0).getIdentifier());
        assertEquals("e26cea2c-1b9f-4afe-b211-f3ef6c88afaa", specimens.get(0).getUuid());

        specimens = bacteriologyService.getSpecimens(Arrays.<Obs>asList());
        assertEquals(0, specimens.size());
    }

    private Concept getConcept(String name) {
        Concept concept = conceptService.getConceptByName(name);
        return new HibernateLazyLoader().load(concept);
    }

    private Obs findMember(Set<Obs> obsList, Concept concept) {
        for (Obs candidate : obsList) {
            candidate = new HibernateLazyLoader().load(candidate);
            if (candidate.getConcept().equals(concept)) {
                return candidate;
            }
        }
        return null;
    }


}
