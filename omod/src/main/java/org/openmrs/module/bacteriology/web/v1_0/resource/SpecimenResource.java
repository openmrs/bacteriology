package org.openmrs.module.bacteriology.web.v1_0.resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_8.PatientResource1_8;

import java.util.*;

@Resource(name = RestConstants.VERSION_1 + "/specimen", supportedClass = Specimen.class, supportedOpenmrsVersions = {"1.10.*", "1.11.*", "1.12.*"})
public class SpecimenResource extends DelegatingCrudResource<Specimen> {


    BacteriologyService bacteriologyService;

    @Override
    public Specimen getByUniqueId(String uuid) {
        Obs obs = Context.getObsService().getObsByUuid(uuid);
        bacteriologyService = Context.getService(BacteriologyService.class);
        return bacteriologyService.getSpecimenFromObs(obs);
    }

    @Override
    protected void delete(Specimen specimen, String s, RequestContext requestContext) throws ResponseException {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    public Specimen newDelegate() {
        return new Specimen();
    }

    @Override
    public Specimen save(Specimen specimen) {
        return Context.getService(BacteriologyService.class).saveSpecimen(specimen);
    }

    @Override
    public void purge(Specimen specimen, RequestContext requestContext) throws ResponseException {

    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("dateCollected");
        description.addProperty("uuid");
        description.addProperty("report");
        description.addProperty("existingObs");
        description.addProperty("sample");
        description.addProperty("type");
        description.addProperty("typeFreeText");
        description.addProperty("identifier");
        return description;
        }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
        if (rep instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("identifier");
            description.addProperty("uuid");
            description.addProperty("existingObs");
            description.addProperty("dateCollected");
            description.addProperty("sample");
            description.addProperty("type");
            description.addProperty("report");
            description.addProperty("typeFreeText");
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            return description;
        } else if (rep instanceof FullRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("identifier");
            description.addProperty("uuid");
            description.addProperty("existingObs");
            description.addProperty("sample");
            description.addProperty("type");
            description.addProperty("dateCollected");
            description.addProperty("typeFreeText");
            description.addProperty("report");
            description.addSelfLink();
            return description;

        } else if (rep instanceof RefRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("identifier");
            description.addProperty("uuid");
            description.addProperty("existingObs");
            description.addProperty("sample");
            description.addProperty("type");
            description.addProperty("report");
            description.addProperty("typeFreeText");
            description.addSelfLink();
            return description;

        }
        return null;
    }

    @PropertyGetter("report")
    public SimpleObject getReport(Specimen specimen) {
        return new ObjectMapper().convertValue(specimen.getReport(), SimpleObject.class);
    }

    @PropertySetter("report")
    public static void setReport(Specimen specimen, Object value) {
        Object reportObject = new ObjectMapper().convertValue(value, new TypeReference<Specimen.TestReport>() {});
        specimen.setReport((Specimen.TestReport) reportObject);
    }

    @PropertySetter("sample")
    public static void setSample(Specimen specimen, Object value) {
        Object sampleObject = new ObjectMapper().convertValue(value, new TypeReference<Specimen.Sample>() {});
        specimen.setSample((Specimen.Sample) sampleObject);
    }

    @PropertySetter("dateCollected")
    public static void setDateCollected(Specimen specimen, Object value) {
        Object sampleObject = new ObjectMapper().convertValue(value, new TypeReference<Date>() {});
        specimen.setDateCollected((Date) sampleObject);
    }

    @PropertySetter("type")
    public static void setType(Specimen specimen, Object value) {
        Object typeObject = new ObjectMapper().convertValue(value, new TypeReference<EncounterTransaction.Concept>() {});
        specimen.setType((EncounterTransaction.Concept) typeObject);
    }

    @Override
    protected PageableResult doSearch(RequestContext context) {
        String conceptName = context.getParameter("name");
        String patientUuid=context.getParameter("patientUuid");
        bacteriologyService=Context.getService(BacteriologyService.class);
        Concept concept = Context.getService(ConceptService.class).getConceptByName(conceptName);
        if (patientUuid != null) {
            Patient patient = ((PatientResource1_8) Context.getService(RestService.class).getResourceBySupportedClass(
                    Patient.class)).getByUniqueId(patientUuid);
            if (patient != null && concept != null){
                if (concept != null) {
                    List<Obs> obsList = Context.getObsService().getObservationsByPersonAndConcept(patient,concept);
                    List<Specimen> specimenList=new ArrayList<Specimen>();
                    for(Obs obs:obsList) {
                        Specimen specimen = bacteriologyService.getSpecimenFromObs(obs);
                        specimenList.add(specimen);
                    }
                    sortSpecimensByDateCollected(specimenList);
                    return new NeedsPaging<Specimen>(specimenList, context);
                }
            }
        }
        return new EmptySearchResult();
    }

    private void sortSpecimensByDateCollected(List<Specimen> specimenList) {
        Collections.sort(specimenList, new Comparator<Specimen>() {
            @Override
            public int compare(Specimen specimen1, Specimen specimen2) {
                return specimen2.getDateCollected().compareTo(specimen1.getDateCollected());
            }
        });
    }
}
