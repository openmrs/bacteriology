package org.openmrs.module.bacteriology.api.specimen;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.encounter.mapper.ObsMapper;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SpecimenMapperTest {

    @Mock
    private Encounter encounter;

    @Mock
    private ConceptService conceptService;

    @Mock
    private Concept urineConcept;

    @Mock
    private ObsMapper obsMapper;

    @Mock
    private ObsService obsService;

    @Mock
    private Obs additionalAttributeObs;

    @Mock
    private Obs resultsObs;

    @Mock
    private Obs existingObs;

    private SpecimenMapper mapper;

    @Before
    public void setUp() {
        initMocks(this);
        mapper = new SpecimenMapper();
        mapper.setConceptService(conceptService);
        mapper.setObsMapper(obsMapper);
        mapper.setObsService(obsService);
    }

    @Test
    public void testCreateSpecimenWithoutAdditionalAttributesAndExistingObs() throws Exception {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25", "yyyy-MM-dd");

        Specimen etSpecimen = createNewSpecimen(sampleDateCollected, "specimenId", "urine_concept_uuid", "", null,null);

        when(conceptService.getConceptByUuid("urine_concept_uuid")).thenReturn(urineConcept);

        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);

        assertEquals("specimenId", specimen.getId());
        assertEquals(sampleDateCollected, specimen.getDateCollected());
        assertEquals(urineConcept, specimen.getType());
        assertEquals(null, specimen.getExistingObs());
        assertEquals(null, specimen.getAdditionalAttributes());
    }

    @Test
    public void testCreateSpecimenWithExistingObsAndAdditionalAttributes() throws ParseException {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25", "yyyy-MM-dd");

        EncounterTransaction.Observation additionalAttributes = new EncounterTransaction.Observation();
        additionalAttributes.setUuid("some_new_obs_uuid");

        Specimen etSpecimen = createNewSpecimen(sampleDateCollected, "specimenId", "urine_concept_uuid", "existing_obs_uuid", additionalAttributes,null);

        when(conceptService.getConceptByUuid("urine_concept_uuid")).thenReturn(urineConcept);
        when(obsService.getObsByUuid("existing_obs_uuid")).thenReturn(existingObs);
        when(obsMapper.transformEtObs(encounter, null, additionalAttributes)).thenReturn(
                additionalAttributeObs);

        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);

        verify(obsMapper, timeout(1)).transformEtObs(encounter, null, additionalAttributes);
        assertEquals("specimenId", specimen.getId());
        assertEquals(sampleDateCollected, specimen.getDateCollected());
        assertEquals(urineConcept, specimen.getType());
        assertEquals(existingObs, specimen.getExistingObs());
        assertEquals(additionalAttributeObs, specimen.getAdditionalAttributes());
    }

    @Test
    public void testCreateSpecimenWithAdditionalAttributes() throws ParseException {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25", "yyyy-MM-dd");

        EncounterTransaction.Observation additionalAttributes = new EncounterTransaction.Observation();
        additionalAttributes.setUuid("some_new_obs_uuid");

        Specimen etSpecimen = createNewSpecimen(sampleDateCollected, "specimenId", "urine_concept_uuid", "", additionalAttributes,null);

        when(conceptService.getConceptByUuid("urine_concept_uuid")).thenReturn(urineConcept);
        when(obsMapper.transformEtObs(encounter, null, additionalAttributes)).thenReturn(additionalAttributeObs);

        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);

        assertEquals("specimenId", specimen.getId());
        assertEquals(sampleDateCollected, specimen.getDateCollected());
        assertEquals(urineConcept, specimen.getType());
        assertEquals(null, specimen.getExistingObs());
        assertEquals(additionalAttributeObs, specimen.getAdditionalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecimenWithoutValidSample() {
        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        mapper.createSpecimen(encounter, etSpecimen);
        fail("should throw an exception as the specimen doesn't contain sample");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecimenWithoutValidSampleType() throws ParseException {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25", "yyyy-MM-dd");

        Specimen etSpecimen = createNewSpecimen(sampleDateCollected, "specimenId", null, "", null,null);
        mapper.createSpecimen(encounter, etSpecimen);
        fail("should throw an exception as the specimen doesn't contain type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecimenWithoutValidDateCollected() throws ParseException {

        Specimen etSpecimen = createNewSpecimen(null, "specimenId", "urine_concept_uuid", "", null,null);
        mapper.createSpecimen(encounter, etSpecimen);
        fail("should throw an exception as the specimen doesn't contain date collected");
    }

    @Test
    public void testCreateSpecimenWithResults() throws ParseException {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25", "yyyy-MM-dd");

        EncounterTransaction.Observation results = new EncounterTransaction.Observation();
        results.setUuid("some_new_obs_uuid");

        Specimen etSpecimen = createNewSpecimen(sampleDateCollected, "specimenId", "urine_concept_uuid", "", null,results);

        when(conceptService.getConceptByUuid("urine_concept_uuid")).thenReturn(urineConcept);
        when(obsMapper.transformEtObs(encounter, null, results)).thenReturn(resultsObs);

        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);

        assertEquals("specimenId", specimen.getId());
        assertEquals(sampleDateCollected, specimen.getDateCollected());
        assertEquals(urineConcept, specimen.getType());
        assertEquals(null, specimen.getExistingObs());
        assertEquals(resultsObs, specimen.getReports());
    }

    private Specimen createNewSpecimen(Date dateCollected, String identifier, String type, String existingSampleObsUuid, EncounterTransaction.Observation additionalAttributes, EncounterTransaction.Observation resultObs) {
        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        etSpecimen.setDateCollected(dateCollected);
        etSpecimen.setExistingObs(existingSampleObsUuid);
        etSpecimen.setIdentifier(identifier);
        etSpecimen.setType(null);
        if (type != null) {
            EncounterTransaction.Concept concept = new EncounterTransaction.Concept();
            concept.setUuid(type);
            etSpecimen.setType(concept);
        }
        Specimen.Sample sample = new Specimen.Sample();
        sample.setAdditionalAttributes(additionalAttributes);
        etSpecimen.setSample(sample);

        Specimen.TestReport report = new Specimen.TestReport();
        report.setResults(resultObs);
        etSpecimen.setReport(report);
        return etSpecimen;
    }

}
