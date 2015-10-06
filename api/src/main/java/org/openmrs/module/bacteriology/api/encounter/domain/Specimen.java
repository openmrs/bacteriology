package org.openmrs.module.bacteriology.api.encounter.domain;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.utils.CustomJsonDateSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specimen {
    private Sample sample;

    private List<TestReport> reports;

    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date dateCollected;

    private EncounterTransaction.Concept type;//TODO: change this to uuid.

    private String identifier;

    private String existingObs;

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public List<TestReport> getReports() {
        return reports;
    }

    public void setReports(List<TestReport> reports) {
        this.reports = reports;
    }

    public Date getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    public EncounterTransaction.Concept getType() {
        return type;
    }

    public void setType(EncounterTransaction.Concept type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getExistingObs() {
        return existingObs;
    }

    public void setExistingObs(String existingObs) {
        this.existingObs = existingObs;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sample{
        private EncounterTransaction.Observation additionalAttributes;

        public EncounterTransaction.Observation getAdditionalAttributes() {
            return additionalAttributes;
        }

        public void setAdditionalAttributes(EncounterTransaction.Observation additionalAttributes) {
            this.additionalAttributes = additionalAttributes;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TestReport {
        private List<EncounterTransaction.Observation> results = new ArrayList<EncounterTransaction.Observation>();

        public List<EncounterTransaction.Observation> getResults() {
            return results;
        }

        public void setResults(List<EncounterTransaction.Observation> results) {
            this.results = results;
        }
    }
}

