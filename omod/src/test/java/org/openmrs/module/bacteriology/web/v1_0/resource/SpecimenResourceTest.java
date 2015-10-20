package org.openmrs.module.bacteriology.web.v1_0.resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SpecimenResourceTest extends MainResourceControllerTest {

    @Before
    public void init() throws Exception {
        executeDataSet("baseBacteriologyData.xml");
        executeDataSet("specimenDataSet.xml");
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


    public void shouldReturnSpecimenByPatientUuid() throws Exception {
        MockHttpServletRequest request = request(RequestMethod.GET, getURI());
        request.addParameter("name", "BACTERIOLOGY CONCEPT SET");
        request.addParameter("patientUuid", "a76e8d23-0c38-408c-b2a8-ea6670f01b51");
        request.addParameter("v", "full");
        MockHttpServletResponse response = handle(request);
        SimpleObject object = deserialize(response);

        List results = (List) object.get("results");
        Specimen specimen = new ObjectMapper().convertValue(results.get(0), Specimen.class);

        assertEquals("SAMPLE12345", specimen.getIdentifier());
        assertEquals("Sample result", specimen.getReport().getResults().getGroupMembers().get(0).getValue());
    }
}