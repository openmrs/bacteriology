package org.openmrs.module.bacteriology.api.encounter;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.openmrs.Concept;
import org.openmrs.ConceptComplex;
import org.openmrs.ConceptDatatype;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.OrderService;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.encounter.exception.ConceptNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

import static org.openmrs.module.emrapi.utils.GeneralUtils.getCurrentDateIfNull;

//TODO: this is a refactoring of EncounterObservationServiceHelper.  This code will go back to emrapi

@Component
public class EncounterObservationMapper {

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private ObsService obsService;

    @Autowired
    private OrderService orderService;

    public void updateObservation(Encounter encounter, Obs parentObs, Set<Obs> existingObservations, EncounterTransaction.Observation observationData) throws ParseException {
        Obs observation = getMatchingObservation(existingObservations, observationData.getUuid());
        if (observation == null) {
            observation = newObservation(encounter, observationData);
            if (parentObs == null) {
                encounter.addObs(observation);
            }
            else parentObs.addGroupMember(observation);
        }
        if (observationData.getVoided()) {
            observation.setVoided(true);
            observation.setVoidReason(observationData.getVoidReason());
        } else {
            mapObservationProperties(observationData, observation);
        }

        for (EncounterTransaction.Observation member : observationData.getGroupMembers()) {
            updateObservation(encounter, observation, observation.getGroupMembers(), member);
        }
    }

    private Obs newObservation(Encounter encounter, EncounterTransaction.Observation observationData) {
        Obs observation;
        observation = new Obs();
        if(!StringUtils.isBlank(observationData.getUuid())){
            observation.setUuid(observationData.getUuid());
        }
        Date observationDateTime = getCurrentDateIfNull(observationData.getObservationDateTime());
        Concept concept = conceptService.getConceptByUuid(observationData.getConceptUuid());
        if (concept == null) {
            throw new ConceptNotFoundException("Observation concept does not exist" + observationData.getConceptUuid());
        }
        observation.setPerson(encounter.getPatient());
        observation.setEncounter(encounter);
        observation.setConcept(concept);
        observation.setObsDatetime(observationDateTime);
        return observation;
    }

    private void mapObservationProperties(EncounterTransaction.Observation observationData, Obs observation) throws ParseException {
        observation.setComment(observationData.getComment());
        if (observationData.getValue() != null) {
            if (observation.getConcept().getDatatype().isCoded()) {
                String uuid = getUuidOfCodedObservationValue(observationData.getValue());
                Concept conceptByUuid = conceptService.getConceptByUuid(uuid);
                if (conceptByUuid == null) {
                    Drug drug = conceptService.getDrugByUuid(uuid);
                    observation.setValueDrug(drug);
                    observation.setValueCoded(drug.getConcept());
                } else {
                    observation.setValueCoded(conceptByUuid);
                }
            } else if (observation.getConcept().isComplex()) {
                observation.setValueComplex(observationData.getValue().toString());
                Concept conceptComplex = observation.getConcept();
                if (conceptComplex instanceof HibernateProxy) {
                    Hibernate.initialize(conceptComplex);
                    conceptComplex = (ConceptComplex) ((HibernateProxy) conceptComplex).getHibernateLazyInitializer().getImplementation();
                }
                obsService.getHandler(((ConceptComplex) conceptComplex).getHandler()).saveObs(observation);
            } else if (!observation.getConcept().getDatatype().getUuid().equals(ConceptDatatype.N_A_UUID)) {
                observation.setValueAsString(observationData.getValue().toString());
            }
        }
        if(observationData.getOrderUuid() != null && !observationData.getOrderUuid().isEmpty()){
            observation.setOrder(getOrderByUuid(observationData.getOrderUuid()));
        }
        observation.setObsDatetime(getCurrentDateIfNull(observationData.getObservationDateTime()));
    }

    private String getUuidOfCodedObservationValue(Object codeObsVal) {
        if (codeObsVal instanceof LinkedHashMap) return (String) ((LinkedHashMap) codeObsVal).get("uuid");
        return (String) codeObsVal;
    }

    private Obs getMatchingObservation(Set<Obs> existingObservations, String observationUuid) {
        if (existingObservations == null) return null;
        for (Obs obs : existingObservations) {
            if (StringUtils.equals(obs.getUuid(), observationUuid)) return obs;
        }
        return null;
    }

    private Order getOrderByUuid(String orderUuid){
        return orderService.getOrderByUuid(orderUuid);
    }

}
