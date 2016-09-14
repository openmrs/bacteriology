package org.openmrs.module.bacteriology.api.encounter.postprocessor;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.EncounterService;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.ObservationMapper;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

public class BacteriologyTransactionHandlerIT extends BaseModuleContextSensitiveTest {

    @Autowired
    private BacteriologyTransactionHandler bacteriologyTransactionHandler;

    @Autowired
    private EncounterService encounterService;

    @Autowired
    private ObservationMapper obsMapper;

    @Before
    public void setUp() throws Exception {
        executeDataSet("baseBacteriologyData.xml");
        executeDataSet("existingSpecimenObs.xml");
    }

    @Test
    public void shouldNotReturnVoidedObs() {
        Encounter encounterByUuid = encounterService.getEncounterByUuid("y403fafb-e5e4-42d0-9d11-4f52e89d12st");
        EncounterTransaction encounterTransaction = new EncounterTransaction();
        bacteriologyTransactionHandler.forRead(encounterByUuid, encounterTransaction);

        Object mdrtbSpecimen = encounterTransaction.getExtensions().get("mdrtbSpecimen");

        assertNotNull("Should not be null", mdrtbSpecimen);
        ArrayList mdrtbSpecimens = (ArrayList) mdrtbSpecimen;
        assertEquals(1, mdrtbSpecimens.size());
        assertEquals("e26cea2c-1b9f-4afe-b211-f3ef6c88afaa", ((Specimen) mdrtbSpecimens.get(0)).getUuid());
    }

    @Test
    public void shouldNotContainSpecimenObsInETObs() {
        Encounter encounterByUuid = encounterService.getEncounterByUuid("y403fafb-e5e4-42d0-9d11-4f52e89d12st");

        EncounterTransaction encounterTransaction = new EncounterTransaction();
       for(Obs obs: encounterByUuid.getObsAtTopLevel(false)) {
        EncounterTransaction.Observation observation= obsMapper.map(obs);
           encounterTransaction.addObservation(observation);
       }

        bacteriologyTransactionHandler.forRead(encounterByUuid, encounterTransaction);

        List<EncounterTransaction.Observation> ETObsList = encounterTransaction.getObservations();

        List<String> ETObsUuidList= new ArrayList();
        for(EncounterTransaction.Observation etObs:ETObsList)
            ETObsUuidList.add(etObs.getUuid());

        assertFalse("Specimen Observations should not be a part of Encounter Transaction Observations",
                ETObsUuidList.contains("e26cea2c-1b9f-4afe-b211-f3ef6c88afaa"));
        assertFalse("Specimen Observations should not be a part of Encounter Transaction Observations",
                ETObsUuidList.contains("e26cea2c-1b9f-4afe-b2zo-flaf6c88afaa"));

    }

    @Test
    public void shouldAddNewSpecimen() throws Exception {
        Encounter encounterBeforeSavingSpecimen = encounterService.getEncounterByUuid("y403fafb-e5e4-42d0-9d11-4f52e89d12th");

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(getBaseExtension(null));
        bacteriologyTransactionHandler.forSave(encounterBeforeSavingSpecimen, encounterTransaction);

        assertNotNull(encounterBeforeSavingSpecimen.getAllObs());
        assertEquals("baef67ac-b24e-4b04-ac83-c3b994132b85",
                encounterBeforeSavingSpecimen.getAllObs().iterator().next().getConcept().getUuid());
        assertFalse(encounterBeforeSavingSpecimen.getAllObs().iterator().next().getVoided());

        Obs specimenObs = encounterBeforeSavingSpecimen.getAllObs().iterator().next();
        Obs typeFreeTextObs = findChildObs(specimenObs, "SPECIMEN SAMPLE SOURCE FREE TEXT");
        assertNotNull(typeFreeTextObs);
        assertEquals("someSampleFreeText", typeFreeTextObs.getValueText());
    }

    private Obs findChildObs(Obs parentObs, String conceptName){
        Iterator<Obs> iterator = parentObs.getGroupMembers().iterator();

        while(iterator.hasNext()){
            Obs obs = iterator.next();
            if(obs.getConcept().getName().getName().equals(conceptName)){
                return obs;
            }
        }
        return null;
    }

    @Test
    public void shouldEditExistingSpecimen() throws Exception {
        Encounter encounterBeforeSavingSpecimen = encounterService.getEncounterByUuid("y403fafb-e5e4-42d0-9d11-4f52e89d12st");

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(getBaseExtension("e26cea2c-1b9f-4afe-b211-f3ef6c88afaa"));
        bacteriologyTransactionHandler.forSave(encounterBeforeSavingSpecimen, encounterTransaction);

        assertNotNull(encounterBeforeSavingSpecimen.getAllObs(false));
        assertEquals("baef67ac-b24e-4b04-ac83-c3b994132b85",
                encounterBeforeSavingSpecimen.getObsAtTopLevel(false).iterator().next().getConcept().getUuid());
        assertFalse(encounterBeforeSavingSpecimen.getAllObs().iterator().next().getVoided());

        Obs member = findChildObs(encounterBeforeSavingSpecimen.getObsAtTopLevel(false).iterator().next(), "SPECIMEN ID");
        assertNotNull(member);
        assertEquals("1234", member.getValueText());

        Obs typeFreeTextObs = findChildObs(encounterBeforeSavingSpecimen.getObsAtTopLevel(false).iterator().next(), "SPECIMEN SAMPLE SOURCE FREE TEXT");
        assertNotNull(typeFreeTextObs);
        assertEquals("someSampleFreeText", typeFreeTextObs.getValueText());

    }

    @Test
    public void shouldDeleteExistingSpecimen() throws Exception {
        Encounter encounterBeforeSavingSpecimen = encounterService.getEncounterByUuid("y403fafb-e5e4-42d0-9d11-4f52e89d12st");

        EncounterTransaction encounterTransaction = new EncounterTransaction();
        encounterTransaction.setExtensions(getVoidedBaseExtension("e26cea2c-1b9f-4afe-b211-f3ef6c88afaa"));
        bacteriologyTransactionHandler.forSave(encounterBeforeSavingSpecimen, encounterTransaction);

        assertEquals(0, encounterBeforeSavingSpecimen.getObsAtTopLevel(false).size());
    }

    private Map<String, Object> getVoidedBaseExtension(String existingObsUuid) {
        Map<String, Object> baseExtension = getBaseExtension(existingObsUuid);
        Object mdrtbSpecimen = baseExtension.get("mdrtbSpecimen");
        Specimen specimen = (Specimen)((ArrayList) mdrtbSpecimen).get(0);
        specimen.setVoided(true);
        return baseExtension;
    }

    private Map<String, Object> getBaseExtension(String existingObsUuid) {
        HashMap<String, Object> extension = new HashMap<String, Object>();
        Specimen specimen = new Specimen();
        specimen.setExistingObs(existingObsUuid);
        specimen.setDateCollected(new Date());
        specimen.setIdentifier("1234");
        specimen.setTypeFreeText("someSampleFreeText");
        EncounterTransaction.Concept type = new EncounterTransaction.Concept();
        type.setUuid("c607c80f-1ea9-4da3-bb88-6276ce8868dd");
        type.setName("WEIGHT (KG)");
        specimen.setType(type);
        ArrayList<Specimen> specimens = new ArrayList<Specimen>();
        specimens.add(specimen);
        extension.put("mdrtbSpecimen", specimens);
        return extension;
    }
}
