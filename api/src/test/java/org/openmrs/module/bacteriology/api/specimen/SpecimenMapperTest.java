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
import org.openmrs.module.emrapi.encounter.EncounterObservationServiceHelper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    private EncounterObservationServiceHelper encounterObservationServiceHelper;

    @Mock
    private ObsService obsService;

    @Mock
    private Obs additionalAttributeObs;

    @Mock
    private Obs existingObs;

    private SpecimenMapper mapper;

    @Before
    public void setUp(){
        initMocks(this);
        mapper = new SpecimenMapper();
        mapper.setConceptService(conceptService);
        mapper.setEncounterObservationServiceHelper(encounterObservationServiceHelper);
        mapper.setObsService(obsService);
    }

    @Test
    public void testCreateSpecimenWithoutAdditionalAttributesAndExistingObs() throws Exception {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25","yyyy-MM-dd");

        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        etSpecimen.setSample(createSample(sampleDateCollected, "specimenId", "urine_concept_uuid", "", null));
        etSpecimen.setReports(Arrays.asList(createReport("accessionNumber", new Date(), new Date(), new Date(), "report_type_concept_uuid", "existing_report_obs_uuid")));

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
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25","yyyy-MM-dd");

        EncounterTransaction.Observation additionalAttributes = new EncounterTransaction.Observation();
        additionalAttributes.setUuid("some_new_obs_uuid");

        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        etSpecimen.setSample(createSample(sampleDateCollected, "specimenId", "urine_concept_uuid", "existing_obs_uuid", additionalAttributes));
        etSpecimen.setReports(Arrays.asList(createReport("accessionNumber", new Date(), new Date(), new Date(), "report_type_concept_uuid", "existing_report_obs_uuid")));

        when(conceptService.getConceptByUuid("urine_concept_uuid")).thenReturn(urineConcept);
        when(obsService.getObsByUuid("existing_obs_uuid")).thenReturn(existingObs);
        when(encounterObservationServiceHelper.transformEtObs(existingObs, additionalAttributes)).thenReturn(additionalAttributeObs);

        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);

        assertEquals("specimenId", specimen.getId());
        assertEquals(sampleDateCollected, specimen.getDateCollected());
        assertEquals(urineConcept, specimen.getType());
        assertEquals(existingObs,specimen.getExistingObs());
        assertEquals(additionalAttributeObs, specimen.getAdditionalAttributes());
    }

    @Test
    public void testCreateSpecimenWithAdditionalAttributes() throws ParseException {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25","yyyy-MM-dd");

        EncounterTransaction.Observation additionalAttributes = new EncounterTransaction.Observation();
        additionalAttributes.setUuid("some_new_obs_uuid");

        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        etSpecimen.setSample(createSample(sampleDateCollected, "specimenId", "urine_concept_uuid", null, additionalAttributes));
        etSpecimen.setReports(Arrays.asList(createReport("accessionNumber", new Date(), new Date(), new Date(), "report_type_concept_uuid", "existing_report_obs_uuid")));

        when(conceptService.getConceptByUuid("urine_concept_uuid")).thenReturn(urineConcept);
        when(encounterObservationServiceHelper.transformEtObs(null, additionalAttributes)).thenReturn(additionalAttributeObs);

        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);

        assertEquals("specimenId", specimen.getId());
        assertEquals(sampleDateCollected, specimen.getDateCollected());
        assertEquals(urineConcept, specimen.getType());
        assertEquals(null,specimen.getExistingObs());
        assertEquals(additionalAttributeObs, specimen.getAdditionalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecimenWithoutValidSample(){
        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);
        fail("should throw an exception as the specimen doesn't contain sample");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecimenWithoutValidSampleType() throws ParseException {
        Date sampleDateCollected = DateUtils.parseDate("2015-09-25","yyyy-MM-dd");

        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        etSpecimen.setSample(createSample(sampleDateCollected, "specimenId", null, null, null));
        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);
        fail("should throw an exception as the specimen doesn't contain sample");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecimenWithoutValidDateCollected() throws ParseException {

        org.openmrs.module.bacteriology.api.encounter.domain.Specimen etSpecimen = new Specimen();
        etSpecimen.setSample(createSample(null, "specimenId", "urine_concept_uuid", null, null));
        org.openmrs.module.bacteriology.api.specimen.Specimen specimen = mapper.createSpecimen(encounter, etSpecimen);
        fail("should throw an exception as the specimen doesn't contain sample");
    }
    private Specimen.Sample createSample(Date dateCollected, String identifier, String type, String existingSampleObsUuid, EncounterTransaction.Observation additionalAttributes) {
        Specimen.Sample sample = new Specimen.Sample();
        sample.setAdditionalAttributes(additionalAttributes);
        sample.setDateCollected(dateCollected);
        sample.setExistingObs(existingSampleObsUuid);
        sample.setIdentifier(identifier);
        sample.setType(type);
        return sample;
    }

    private org.openmrs.module.bacteriology.api.encounter.domain.Specimen.TestReport createReport(String accessionNumber,Date dateCollected,Date dateOrdered,Date dateStarted,
                                                                                                  String reportTypeConceptUuid, String existingObsUuid){

        EncounterTransaction.Concept reportTypeConcept = new EncounterTransaction.Concept();
        reportTypeConcept.setUuid(reportTypeConceptUuid);

        org.openmrs.module.bacteriology.api.encounter.domain.Specimen.TestReport testReport = new org.openmrs.module.bacteriology.api.encounter.domain.Specimen.TestReport();
        testReport.setAccessionNumber(accessionNumber);
        testReport.setDateCollected(dateCollected);
        testReport.setDateOrdered(dateOrdered);
        testReport.setDateStarted(dateStarted);
        testReport.setReportType(reportTypeConcept);
        testReport.setExistingObs(existingObsUuid);

        return testReport;
    }

    private Date date(String date) throws ParseException {
        return DateUtils.parseDate(date,"yyyy-MM-dd");
    }
}