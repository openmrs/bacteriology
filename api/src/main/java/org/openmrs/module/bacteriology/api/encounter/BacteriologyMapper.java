package org.openmrs.module.bacteriology.api.encounter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.module.bacteriology.BacteriologyConstants;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BacteriologyMapper {

    public List<Specimen> mapSpecimen(EncounterTransaction encounterTransaction) {
        Map<String, Object> extensions = encounterTransaction.getExtensions();
        Object specimenList = extensions.get(BacteriologyConstants.BACTERIOLOGY_EXTENSION_KEY);

        if (specimenList == null) {
            return new ArrayList<Specimen>();
        }

        return new ObjectMapper().convertValue(specimenList, new TypeReference<List<Specimen>>() {});
    }
}
