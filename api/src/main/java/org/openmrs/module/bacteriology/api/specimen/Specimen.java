package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.Concept;
import org.openmrs.Obs;

import java.util.Date;
import java.util.List;

public class Specimen {
    private String uuid;
    private Obs existingObs;
    private Date dateCollected;
    private String id;
    private Concept type;
    private Obs additionalAttributes;
    private Obs reports;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Concept getType() {
        return type;
    }

    public void setType(Concept type) {
        this.type = type;
    }

    public Obs getExistingObs() {
        return existingObs;
    }

    public void setExistingObs(Obs existingObs) {
        this.existingObs = existingObs;
    }

    public Date getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    public Obs getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(Obs additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public Obs getReports() {
        return reports;
    }

    public void setReports(Obs reports) {
        this.reports = reports;
    }

}
