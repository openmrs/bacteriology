package org.openmrs.module.bacteriology.api.specimen;

import org.openmrs.Obs;
import org.openmrs.module.bacteriology.api.MdrtbConcepts;

import java.util.Date;
import java.util.List;

public class Specimen {
    private Obs existingObs;
    private Date dateCollected;
    private String id;
    private SampleType type;
    private Obs additionalAttributes;
    private List<TestReport> reports;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SampleType getType() {
        return type;
    }

    public void setType(SampleType type) {
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

    public List<TestReport> getReports() {
        return reports;
    }

    public void setReports(List<TestReport> reports) {
        this.reports = reports;
    }

    public enum SampleType{
        SPUTUM(MdrtbConcepts.SPUTUM_CONCEPT_CODE),
        URINE(MdrtbConcepts.URINE_CONCEPT_CODE);

        String codeInEmrConceptSource;

        SampleType(String codeInEmrConceptSource){ this.codeInEmrConceptSource = codeInEmrConceptSource;}


        String getCodeInEmrConceptSource() {
            return codeInEmrConceptSource;
        }

        public static SampleType parseConceptReferenceCode(String code) {
            for (SampleType candidate : values()) {
                if (candidate.getCodeInEmrConceptSource().equals(code)) {
                    return candidate;
                }
            }
            return null;
        }

    }

}
