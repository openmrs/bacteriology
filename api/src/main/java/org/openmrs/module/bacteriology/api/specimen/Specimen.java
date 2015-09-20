package org.openmrs.module.bacteriology.api.specimen;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openmrs.Concept;
import org.openmrs.module.emrapi.encounter.domain.EncounterTransaction;
import org.openmrs.module.emrapi.utils.CustomJsonDateSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specimen {
    private String identifier;

    private String type;

    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date dateCollected;

    private String appearance;

    private String comments;

    private List<Smear> smears;

    private List<Culture> cultures;

    private List<DST> dsts;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Smear> getSmears() {
        return smears;
    }

    public void setSmears(List<Smear> smears) {
        this.smears = smears;
    }

    public List<Culture> getCultures() {
        return cultures;
    }

    public void setCultures(List<Culture> cultures) {
        this.cultures = cultures;
    }

    public List<DST> getDsts() {
        return dsts;
    }

    public void setDsts(List<DST> dsts) {
        this.dsts = dsts;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Smear extends Bacteriology {
        private Integer bacilli;
        private String method;

        public Integer getBacilli() {
            return bacilli;
        }

        public void setBacilli(Integer bacilli) {
            this.bacilli = bacilli;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DST {
        private Concept method;
        private Boolean direct;
        private Concept organismType;
        private String organismTypeNonCoded;
        private Integer coloniesInControl;

        public Concept getMethod() {
            return method;
        }

        public void setMethod(Concept method) {
            this.method = method;
        }

        public Boolean getDirect() {
            return direct;
        }

        public void setDirect(Boolean direct) {
            this.direct = direct;
        }

        public Concept getOrganismType() {
            return organismType;
        }

        public void setOrganismType(Concept organismType) {
            this.organismType = organismType;
        }

        public String getOrganismTypeNonCoded() {
            return organismTypeNonCoded;
        }

        public void setOrganismTypeNonCoded(String organismTypeNonCoded) {
            this.organismTypeNonCoded = organismTypeNonCoded;
        }

        public Integer getColoniesInControl() {
            return coloniesInControl;
        }

        public void setColoniesInControl(Integer coloniesInControl) {
            this.coloniesInControl = coloniesInControl;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Culture {
        private Integer colonies;
        private EncounterTransaction.Concept method;
        private EncounterTransaction.Concept organismType;
        private String organismTypeNonCoded;
        private Integer daysToPositivity;

        public Integer getColonies() {
            return colonies;
        }

        public void setColonies(Integer colonies) {
            this.colonies = colonies;
        }

        public EncounterTransaction.Concept getMethod() {
            return method;
        }

        public void setMethod(EncounterTransaction.Concept method) {
            this.method = method;
        }

        public EncounterTransaction.Concept getOrganismType() {
            return organismType;
        }

        public void setOrganismType(EncounterTransaction.Concept organismType) {
            this.organismType = organismType;
        }

        public String getOrganismTypeNonCoded() {
            return organismTypeNonCoded;
        }

        public void setOrganismTypeNonCoded(String organismTypeNonCoded) {
            this.organismTypeNonCoded = organismTypeNonCoded;
        }

        public Integer getDaysToPositivity() {
            return daysToPositivity;
        }

        public void setDaysToPositivity(Integer daysToPositivity) {
            this.daysToPositivity = daysToPositivity;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bacteriology {
        private EncounterTransaction.Concept result;
        private Date dateCollected;
        private String accessionNumber;
        private Date dateStarted;
        private Date resultDate;
        private Date dateOrdered;
        private String labLocationUuid;
        private String comment;
        //This is for storing the results which will be obsTemplate configured as per the result configuration.
        private List<EncounterTransaction.Observation> observations = new ArrayList<EncounterTransaction.Observation>();

        public EncounterTransaction.Concept getResult() {
            return result;
        }

        public void setResult(EncounterTransaction.Concept result) {
            this.result = result;
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

        public String getLabLocationUuid() {
            return labLocationUuid;
        }

        public void setLabLocationUuid(String labLocationUuid) {
            this.labLocationUuid = labLocationUuid;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}

