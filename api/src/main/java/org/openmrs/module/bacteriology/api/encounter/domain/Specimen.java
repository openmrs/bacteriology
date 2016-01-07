package org.openmrs.module.bacteriology.api.encounter.domain;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.utils.CustomJsonDateSerializer;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specimen {
    private Sample sample;

    private TestReport report;

    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date dateCollected;

    private EncounterTransaction.Concept type;

    private String typeFreeText;

    private String identifier;

    private String existingObs;

    private String uuid;

    private boolean voided;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
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

    public TestReport getReport() {
        return report;
    }

    public void setReport(TestReport report) {
        this.report = report;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public boolean isVoided() {
        return voided;
    }


    public String getTypeFreeText() {
        return typeFreeText;
    }

    public void setTypeFreeText(String typeFreeText) {
        this.typeFreeText = typeFreeText;
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
        EncounterTransaction.Observation results;

        public EncounterTransaction.Observation getResults() {
            return results;
        }

        public void setResults(EncounterTransaction.Observation results) {
            this.results = results;
        }
    }
}

