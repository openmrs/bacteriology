package org.openmrs.module.bacteriology.api.encounter.postprocessor;

import org.openmrs.Encounter;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.encounter.postprocessor.EncounterUpdatePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BacteriologyPostProcessor implements EncounterUpdatePostProcessor {
    @Override
    public void forRead(Encounter encounter, EncounterTransaction encounterTransaction) {

    }

    @Override
    public void forSave(Encounter encounter, EncounterTransaction encounterTransaction) {

    }
}
