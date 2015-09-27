package org.openmrs.module.bacteriology.api.encounter;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BacteriologyMapperTest {
    @Test
    public void testMapSpecimen() throws Exception {

        String extensionValue = "[{\"sample\": {\"identifier\": \"123\"}, \"reports\": [{\"accessionNumber\": \"acc123\"}, {\"accessionNumber\": \"acc134\"}]}]";

        Map<String, Object> extensions = new HashMap<String, Object>();
        extensions.put("mdrtb.specimen", new ObjectMapper().readValue(extensionValue, Object.class));

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(extensions);

        BacteriologyMapper mapper = new BacteriologyMapper();
        List<Specimen> specimenList = mapper.mapSpecimen(encounterTransaction);

        Assert.assertEquals(1, specimenList.size());
        Assert.assertEquals(Specimen.class, specimenList.get(0).getClass());
        Assert.assertEquals("123", specimenList.get(0).getSample().getIdentifier());
        Assert.assertEquals("acc123", specimenList.get(0).getReports().get(0).getAccessionNumber());
        Assert.assertEquals("acc134", specimenList.get(0).getReports().get(1).getAccessionNumber());
    }

    @Test
    public void testSpeicmenWithoutAnyExtensions() {
        Map<String, Object> extensions = new HashMap<String, Object>();

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(extensions);

        BacteriologyMapper mapper = new BacteriologyMapper();
        List<Specimen> specimenList = mapper.mapSpecimen(encounterTransaction);
        Assert.assertEquals(0, specimenList.size());


    }

    @Test
    public void testSpeicmenWithEmptyExtensions() throws IOException {
        String extensionValue = "[]";

        Map<String, Object> extensions = new HashMap<String, Object>();
        extensions.put("mdrtb.specimen", new ObjectMapper().readValue(extensionValue, Object.class));

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(extensions);

        BacteriologyMapper mapper = new BacteriologyMapper();
        List<Specimen> specimenList = mapper.mapSpecimen(encounterTransaction);
        Assert.assertEquals(0, specimenList.size());


    }
}