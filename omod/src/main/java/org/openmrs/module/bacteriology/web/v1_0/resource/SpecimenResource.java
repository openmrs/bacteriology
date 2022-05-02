package org.openmrs.module.bacteriology.web.v1_0.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.StringProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimens;
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

@Resource(name = RestConstants.VERSION_1 + "/specimen", supportedClass = Specimen.class, supportedOpenmrsVersions = {"1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*"})
public class SpecimenResource extends DelegatingCrudResource<Specimen> {

    @Override
    public Specimen getByUniqueId(String uuid) {
        Obs obs = Context.getObsService().getObsByUuid(uuid);
        BacteriologyService bacteriologyService = Context.getService(BacteriologyService.class);
        return bacteriologyService.getSpecimen(obs);
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
        description.addProperty("voided");
        return description;
    }

    @Override
    public Model getCREATEModel(Representation rep) {
        return new ModelImpl().property("dataCollected", new StringProperty())
                .property("uuid", new StringProperty())
                .property("report", new StringProperty())
                .property("existingObs", new StringProperty())
                .property("sample", new StringProperty())
                .property("type", new StringProperty())
                .property("typeFreeText", new StringProperty())
                .property("identifier", new StringProperty())
                .property("voided", new StringProperty());
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

    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl modelImpl = ((ModelImpl) super.getGETModel(rep));
        if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
            modelImpl.property("dataCollected", new StringProperty())
                    .property("uuid", new StringProperty())
                    .property("report", new StringProperty())
                    .property("existingObs", new StringProperty())
                    .property("sample", new StringProperty())
                    .property("type", new StringProperty())
                    .property("typeFreeText", new StringProperty())
                    .property("identifier", new StringProperty())
                    .property("voided", new StringProperty());
        }
        if (rep instanceof RefRepresentation) {
            modelImpl.property("uuid", new StringProperty())
                    .property("report", new StringProperty())
                    .property("existingObs", new StringProperty())
                    .property("sample", new StringProperty())
                    .property("type", new StringProperty())
                    .property("typeFreeText", new StringProperty())
                    .property("identifier", new StringProperty())
                    .property("voided", new StringProperty());
        }
        return modelImpl;
    }


    @PropertyGetter("report")
    public SimpleObject getReport(Specimen specimen) {
        return new ObjectMapper().convertValue(specimen.getReport(), SimpleObject.class);
    }

    @PropertySetter("report")
    public static void setReport(Specimen specimen, Object value) {
        Object reportObject = new ObjectMapper().convertValue(value, new TypeReference<Specimen.TestReport>() {
        });
        specimen.setReport((Specimen.TestReport) reportObject);
    }

    @PropertySetter("sample")
    public static void setSample(Specimen specimen, Object value) {
        Object sampleObject = new ObjectMapper().convertValue(value, new TypeReference<Specimen.Sample>() {
        });
        specimen.setSample((Specimen.Sample) sampleObject);
    }

    @PropertySetter("dateCollected")
    public static void setDateCollected(Specimen specimen, Object value) {
        Object sampleObject = new ObjectMapper().convertValue(value, new TypeReference<Date>() {
        });
        specimen.setDateCollected((Date) sampleObject);
    }

    @PropertySetter("type")
    public static void setType(Specimen specimen, Object value) {
        Object typeObject = new ObjectMapper().convertValue(value, new TypeReference<EncounterTransaction.Concept>() {
        });
        specimen.setType((EncounterTransaction.Concept) typeObject);
    }

    @PropertySetter("voided")
    public static void setVoided(Specimen specimen, Object value) {
        Object voided = new ObjectMapper().convertValue(value, new TypeReference<Boolean>() {
        });
        specimen.setVoided((Boolean) voided);
    }

    @Override
    protected PageableResult doSearch(RequestContext requestContext) {
        String conceptName = requestContext.getParameter("name");
        String patientUuid = requestContext.getParameter("patientUuid");

        BacteriologyService bacteriologyService = Context.getService(BacteriologyService.class);
        ConceptService conceptService = Context.getConceptService();
        PatientService patientService = Context.getPatientService();
        ObsService obsService = Context.getObsService();

        Concept concept = conceptService.getConceptByName(conceptName);
        Patient patient = patientService.getPatientByUuid(patientUuid);
        if (null == concept || null == patient) {
            return new EmptySearchResult();
        }

        List<Obs> obsList = obsService.getObservationsByPersonAndConcept(patient, concept);
        Specimens specimens = bacteriologyService.getSpecimens(obsList);
        Specimens sortedSpecimens = specimens.sortByDateCollected();
        return new NeedsPaging<Specimen>(sortedSpecimens, requestContext);
    }
}
