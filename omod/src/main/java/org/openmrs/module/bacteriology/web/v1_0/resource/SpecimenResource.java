package org.openmrs.module.bacteriology.web.v1_0.resource;

import org.hibernate.SessionFactory;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.ObsDAO;
import org.openmrs.module.bacteriology.api.BacteriologyService;
import org.openmrs.module.bacteriology.api.encounter.domain.Specimen;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/specimen", supportedClass = Specimen.class, supportedOpenmrsVersions = {"1.10.*", "1.11.*"})
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
        return null;
    }

    @Override
    public Specimen save(Specimen specimen) {
        return null;
    }

    @Override
    public void purge(Specimen specimen, RequestContext requestContext) throws ResponseException {

    }
    
    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
        if (rep instanceof DefaultRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("identifier");
            description.addProperty("uuid");
            description.addProperty("existingObs", Representation.REF);
            description.addProperty("type");
            description.addProperty("report");
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
            return description;
        } else if (rep instanceof FullRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("identifier");
            description.addProperty("uuid");
            description.addProperty("existingObs", Representation.REF);
            description.addProperty("type");
            description.addSelfLink();
            return description;

        }
        return null;
    }

    @PropertyGetter("report")
    public Object getReport(Specimen specimen) {
        List ret = new ArrayList();
        EncounterTransaction.Observation observation = specimen.getReport().getResults();
        List<EncounterTransaction.Observation> tests = observation.getGroupMembers();
        ret.add(tests);
        return ret;
    }

}
