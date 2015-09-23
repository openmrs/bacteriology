package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.Encounter;
import org.openmrs.module.bacteriology.api.encounter.EncounterObservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecimenMapper {

    @Autowired
    private EncounterObservationMapper encounterObservationMapper;

    public Specimen createSpecimen(Encounter encounter,org.openmrs.module.bacteriology.api.encounter.domain.Specimen specimen){


        return null;
    }

}
