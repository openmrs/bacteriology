package org.openmrs.module.bacteriology.api.encounter.postprocessor;

import org.openmrs.Encounter;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.encounter.BacteriologyMapper;
import org.openmrs.module.bacteriology.api.specimen.SpecimenMetadataDescriptor;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.encounter.postprocessor.EncounterUpdatePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BacteriologyPostProcessor implements EncounterUpdatePostProcessor {

    @Autowired
    private BacteriologyService bacteriologyService;

    @Override
    public void forRead(Encounter encounter, EncounterTransaction encounterTransaction) {
        bacteriologyService.updateEncounter(encounter,encounterTransaction);
    }

    @Override
    public void forSave(Encounter encounter, EncounterTransaction encounterTransaction) {

    }
}
