package org.openmrs.module.bacteriology.api.encounter;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BacteriologyMapperTest {
    @Test
    public void testMapSpecimen() throws Exception {
        String extensionValue = "[\n" +
                "      {\n" +
                "        \"sample\": {},\n" +
                "        \"dateCollected\": \"2015-10-14T18:30:00.000Z\",\n" +
                "        \"type\": {\n" +
                "          \"uuid\": \"958cd481-53cb-446a-b4c5-9d1dc00ded24\",\n" +
                "          \"name\": \"Urine\",\n" +
                "          \"shortName\": null,\n" +
                "          \"description\": null,\n" +
                "          \"dataType\": null,\n" +
                "          \"conceptClass\": \"Sample\"\n" +
                "        },\n" +
                "        \"identifier\": \"123\"\n" +
                "      }]";

        Map<String, Object> extensions = new HashMap<String, Object>();
        extensions.put("mdrtbSpecimen", new ObjectMapper().readValue(extensionValue, Object.class));

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(extensions);

        BacteriologyMapper mapper = new BacteriologyMapper();
        List<Specimen> specimenList = mapper.mapSpecimen(encounterTransaction);

        Assert.assertEquals(1, specimenList.size());
        Assert.assertEquals(Specimen.class, specimenList.get(0).getClass());
        Assert.assertEquals("123", specimenList.get(0).getIdentifier());
        Assert.assertEquals("Urine", specimenList.get(0).getType().getName());
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
        extensions.put("mdrtbSpecimen", new ObjectMapper().readValue(extensionValue, Object.class));

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(extensions);

        BacteriologyMapper mapper = new BacteriologyMapper();
        List<Specimen> specimenList = mapper.mapSpecimen(encounterTransaction);
        Assert.assertEquals(0, specimenList.size());


    }
}