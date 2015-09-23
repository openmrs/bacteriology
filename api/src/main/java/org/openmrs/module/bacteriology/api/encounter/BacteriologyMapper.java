package org.openmrs.module.bacteriology.api.encounter;

import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BacteriologyMapper {

    public List<Specimen> mapSpecimen(EncounterTransaction encounterTransaction){
        //TODO: Logic to map specimen
        return null;
    }
}
