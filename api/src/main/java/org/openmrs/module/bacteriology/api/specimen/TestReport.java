package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.Concept;
import org.openmrs.Obs;

import java.util.Date;

public class TestReport {
    private Obs existingObs;

    private String accessionNumber;

    private Date dateCollected;

    private Date dateStarted;

    private Date resultDate;

    private Date dateOrdered;

    private Obs results;

    private Concept reportType;

    public Concept getReportType() {
        return reportType;
    }

    public void setReportType(Concept reportType) {
        this.reportType = reportType;
    }

    public Obs getExistingObs() {
        return existingObs;
    }

    public void setExistingObs(Obs existingObs) {
        this.existingObs = existingObs;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public Date getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
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

    public Obs getResults() {
        return results;
    }

    public void setResults(Obs results) {
        this.results = results;
    }
}
