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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sample{
        @JsonSerialize(using = CustomJsonDateSerializer.class)
        private Date dateCollected;

        private String type;

        private String identifier;

        private String existingObs;

        public String getExistingObs() {
            return existingObs;
        }

        public void setExistingObs(String existingObs) {
            this.existingObs = existingObs;
        }

        private List<EncounterTransaction.Observation> observations = new ArrayList<EncounterTransaction.Observation>();

        public List<EncounterTransaction.Observation> getObservations() {
            return observations;
        }

        public void setObservations(List<EncounterTransaction.Observation> observations) {
            this.observations = observations;
        }

        public Date getDateCollected() {
            return dateCollected;
        }

        public void setDateCollected(Date dateCollected) {
            this.dateCollected = dateCollected;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TestReport {

        private String accessionNumber;

        @JsonSerialize(using = CustomJsonDateSerializer.class)
        private Date dateCollected;

        @JsonSerialize(using = CustomJsonDateSerializer.class)
        private Date dateStarted;

        @JsonSerialize(using = CustomJsonDateSerializer.class)
        private Date resultDate;

        @JsonSerialize(using = CustomJsonDateSerializer.class)
        private Date dateOrdered;

        private String existingObs;

        private List<EncounterTransaction.Concept> reportType = new ArrayList<EncounterTransaction.Concept>();

        private List<EncounterTransaction.Observation> results = new ArrayList<EncounterTransaction.Observation>();

        public String getExistingObs() {
            return existingObs;
        }

        public void setExistingObs(String existingObs) {
            this.existingObs = existingObs;
        }

        public Date getDateCollected() {
            return dateCollected;
        }

        public void setDateCollected(Date dateCollected) {
            this.dateCollected = dateCollected;
        }

        public String getAccessionNumber() {
            return accessionNumber;
        }

        public void setAccessionNumber(String accessionNumber) {
            this.accessionNumber = accessionNumber;
        }

        public Date getDateStarted() {
            return dateStarted;
        }

        public void setDateStarted(Date dateStarted) {
            this.dateStarted = dateStarted;
        }

        public List<EncounterTransaction.Observation> getResults() {
            return results;
        }

        public void setResults(List<EncounterTransaction.Observation> results) {
            this.results = results;
        }

        public List<EncounterTransaction.Concept> getReportType() {
            return reportType;
        }

        public void setReportType(List<EncounterTransaction.Concept> reportType) {
            this.reportType = reportType;
        }

        public Date getResultDate() {
            return resultDate;
        }

        public void setResultDate(Date resultDate) {
            this.resultDate = resultDate;
        }

        public Date getDateOrdered() {
            return dateOrdered;
        }

        public void setDateOrdered(Date dateOrdered) {
            this.dateOrdered = dateOrdered;
        }

    }
}

