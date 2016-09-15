package org.openmrs.module.bacteriology.api.encounter.postprocessor;

import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.encounter.postprocessor.EncounterTransactionHandler;
import org.springframework.stereotype.Component;

@Component
public class BacteriologyTransactionHandler implements EncounterTransactionHandler {

    @Override
    public void forRead(Encounter encounter, EncounterTransaction encounterTransaction) {
        Context.getService(BacteriologyService.class).updateEncounterTransaction(encounter,encounterTransaction);
    }

    @Override
    public void forSave(Encounter encounter, EncounterTransaction encounterTransaction) {
        Context.getService(BacteriologyService.class).updateEncounter(encounter, encounterTransaction);
    }
}
