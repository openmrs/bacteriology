package org.openmrs.module.bacteriology.web.v1_0.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;

public class SpecimenResourceTest extends MainResourceControllerTest {

    private static final String SPECIMEN_DATA_SET_XML = "specimenDataSet.xml";

    @Before
    public void init() throws Exception {
        executeDataSet(SPECIMEN_DATA_SET_XML);
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
    public void shouldReturnDefaultAndSelfLinkForCustomUuid() throws Exception {
        String obsGroupUUid = "e26cea2c-1b9f-4afe-b211-f3ef6c88afaa";

        MockHttpServletRequest request = request(RequestMethod.GET, getURI() + "/" + obsGroupUUid);
        MockHttpServletResponse response = handle(request);
        SimpleObject object = deserialize(response);
        String data =(String)object.get("identifier");
        Assert.assertEquals(data, "SAMPLE12345");
    }
}