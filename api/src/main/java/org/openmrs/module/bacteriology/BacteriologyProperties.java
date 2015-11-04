package org.openmrs.module.bacteriology;

import org.openmrs.module.bacteriology.api.specimen.SpecimenMetadataDescriptor;
import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.springframework.stereotype.Component;

@Component("bacteriologyProperties")
public class BacteriologyProperties extends ModuleProperties{

    //TODO: make specimenmetadatadescriptor as a component.
    public SpecimenMetadataDescriptor getSpecimenMetadata() {
        return new SpecimenMetadataDescriptor(conceptService);
    }

}
