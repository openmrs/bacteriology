package org.openmrs.module.bacteriology.web.v1_0.resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SpecimenResourceTest extends MainResourceControllerTest {

    SpecimenResource specimenResource;

    @Before
    public void init() throws Exception {
        executeDataSet("baseBacteriologyData.xml");
        executeDataSet("specimenDataSet.xml");
        specimenResource = new SpecimenResource();
    }

    @Override
    public String getURI() {
        return "specimen";
    }

    @Override
    public String getUuid() {
        return null;
    }

    @Override
    public long getAllCount() {
        return 0;
    }

    @Test
    public void shouldReturnSpecimenByPatientUuid() throws Exception {
        MockHttpServletRequest request = request(RequestMethod.GET, getURI());
        request.addParameter("name", "BACTERIOLOGY CONCEPT SET");
        request.addParameter("patientUuid", "a76e8d23-0c38-408c-b2a8-ea6670f01b51");
        request.addParameter("v", "full");
        MockHttpServletResponse response = handle(request);
        SimpleObject object = deserialize(response);

        List results = (List) object.get("results");
        Specimen specimen = new ObjectMapper().convertValue(results.get(1), Specimen.class);

        assertEquals("SAMPLE12345", specimen.getIdentifier());
        assertEquals("Sample result", specimen.getReport().getResults().getGroupMembers().get(0).getValue());
    }

    @Test
    public void shouldReturnAllSpecimensSortedByDateCollected() throws Exception {
        MockHttpServletRequest request = request(RequestMethod.GET, getURI());
        request.addParameter("name", "BACTERIOLOGY CONCEPT SET");
        request.addParameter("patientUuid", "a76e8d23-0c38-408c-b2a8-ea6670f01b51");
        request.addParameter("v", "full");
        MockHttpServletResponse response = handle(request);
        SimpleObject object = deserialize(response);

        List results = (List) object.get("results");
        assertEquals(3, results.size());
        Specimen firstSpecimen = new ObjectMapper().convertValue(results.get(0), Specimen.class);
        Specimen secondSpecimen = new ObjectMapper().convertValue(results.get(1), Specimen.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String firstSpecimenDate = dateFormat.format(firstSpecimen.getDateCollected());
        String secondSpecimenDate = dateFormat.format(secondSpecimen.getDateCollected());

        assertEquals("2008-08-20", firstSpecimenDate);
        assertEquals("2008-08-19", secondSpecimenDate);
    }

    @Test
    public void shouldSaveEditedSpecimen() throws Exception {
        Specimen specimen = specimenResource.getByUniqueId("uuid300");
        assertNotEquals(specimen, null);
        EncounterTransaction.Observation microResultObs = specimen.getReport().getResults().getGroupMembers()
                .stream()
                .filter(observation -> observation.getConcept().getName().equalsIgnoreCase("Microscopy RESULTS"))
                .findFirst().get();
        EncounterTransaction.Observation editObs = microResultObs.getGroupMembers()
                .stream()
                .findFirst().get();
        editObs.setValue("Edited Sample result");
        specimenResource.save(specimen);
        specimen = specimenResource.getByUniqueId("uuid300");
        assertFalse(specimen.isVoided());
    }
}