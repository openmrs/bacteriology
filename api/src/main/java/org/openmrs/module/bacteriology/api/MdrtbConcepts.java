package org.openmrs.module.bacteriology.api;

/**
 * This class defines all of the Concept Mappings that are required/used by this module
 * Note that mappings are defined as arrays in case we want to rename the mappings and temporarily support multiple mappings for a single concept
 */
public class MdrtbConcepts {

    public static final String SPECIMEN_DATE_COLLECTED = "SAMPLE_DATE_COLLECTED";
    public final static String SPECIMEN_CONSTRUCT = "TUBERCULOSIS SPECIMEN CONSTRUCT";
    public static final String SPUTUM_CONCEPT_CODE = "SPUTUM";
    public static final String URINE_CONCEPT_CODE = "URINE";
    public static final String SAMPLE_SOURCE_CODE = "TUBERCULOSIS SAMPLE SOURCE";

    // Vitals
    public final static String [] WEIGHT = {"WEIGHT"};
    public final static String [] PULSE = {"PULSE"};
    public final static String [] TEMPERATURE = {"TEMPERATURE"};
    public final static String [] RESPIRATORY_RATE = {"RESPIRATORY RATE"};
    public final static String [] SYSTOLIC_BLOOD_PRESSURE = {"SYSTOLIC BLOOD PRESSURE"};

    // MDR-TB Drugs
    public final static String [] TUBERCULOSIS_DRUGS = {"TUBERCULOSIS DRUGS"};
    public final static String [] ISONIAZID = {"ISONIAZID"};
    public final static String [] RIFAMPICIN = {"RIFAMPICIN"};
    public final static String [] CAPREOMYCIN = {"CAPREOMYCIN"};
    public final static String [] KANAMYCIN = {"KANAMYCIN"};
    public final static String [] AMIKACIN = {"AMIKACIN"};
    public final static String [] CLOFAZIMINE = {"CLOFAZIMINE"};
    public final static String [] CYCLOSERINE = {"CYCLOSERINE"};
    public final static String [] ETHIONAMIDE = {"ETHIONAMIDE"};
    public final static String [] PROTHIONAMIDE = {"PROTHIONAMIDE"};
    public final static String [] GATIFLOXACIN = {"GATIFLOXACIN"};
    public final static String [] OFLOXACIN = {"OFLOXACIN"};
    public final static String [] P_AMINOSALICYLIC_ACID = {"P-AMINOSALICYLIC ACID "};
    public final static String [] TERIZIDONE = {"TERIZIDONE"};
    public final static String [] VIOMYCIN = {"VIOMYCIN"};
    public final static String [] CLARITHROMYCIN = {"CLARITHROMYCIN"};
    public final static String [] RIFABUTINE = {"RIFABUTINE"};
    public final static String [] STREPTOMYCIN = {"STREPTOMYCIN"};
    public final static String [] PYRAZINAMIDE = {"PYRAZINAMIDE"};
    public final static String [] CIPROFLOXACIN = {"CIPROFLOXACIN"};
    public final static String [] ETHAMBUTOL = {"ETHAMBUTOL"};
    public final static String [] LEVOFLOXACIN = {"LEVOFLOXACIN"};
    public final static String [] PYRIDOXINE = {"PYRIDOXINE"};
    public final static String [] MOXIFLOXACIN = {"MOXIFLOXACIN"};
    public final static String [] AMOXICILLIN_AND_LAVULANIC_ACID = {"AMOXICILLIN AND CLAVULANIC ACID"};
    public final static String [] THIOACETAZONE = {"THIOACETAZONE"};

    public final static String [] QUINOLONES = {"QUINOLONES"};

    // Drug-Related concepts
    public final static String [] CURRENT_MULTI_DRUG_RESISTANT_TUBERCULOSIS_TREATMENT_TYPE = {"CURRENT MULTI-DRUG RESISTANT TUBERCULOSIS TREATMENT TYPE"};
    public final static String [] REASON_TUBERCULOSIS_TREATMENT_CHANGED_OR_STOPPED = {"REASON TUBERCULOSIS TREATMENT CHANGED OR STOPPED"};
    public final static String [] STANDARDIZED = {"STANDARDIZED"};
    public final static String [] EMPIRIC = {"EMPIRIC"};
    public final static String [] INDIVIDUALIZED = {"INDIVIDUALIZED"};

    // Smear, Culture, and DSTs
    public final static String [] BACILLI = {"BACILLI"};
    public final static String [] COLONIES = {"COLONIES"};
    public final static String [] CULTURE_CONSTRUCT = {"TUBERCULOSIS CULTURE CONSTRUCT"};
    public final static String [] CULTURE_METHOD = {"TUBERCULOSIS CULTURE METHOD"};
    public final static String [] CULTURE_RESULT = {"TUBERCULOSIS CULTURE RESULT"};
    public final static String [] DIRECT_INDIRECT = {"DIRECT/INDIRECT"};
    public final static String [] DST_CONSTRUCT = {"TUBERCULOSIS DRUG SENSITIVITY TEST CONSTRUCT"};
    public final static String [] DST_METHOD = {"TUBERCULOSIS DRUG SENSITIVITY TEST METHOD"};
    public final static String [] DST_RESULT = {"TUBERCULOSIS DRUG SENSITIVITY TEST RESULT"};
    public final static String [] COLONIES_IN_CONTROL = {"COLONIES IN CONTROL"};
    public final static String [] CONCENTRATION = {"CONCENTRATION"};
    public final static String [] RESISTANT_TO_TB_DRUG = {"RESISTANT TO TUBERCULOSIS DRUG"};
    public final static String [] INTERMEDIATE_TO_TB_DRUG = {"INTERMEDIATE TO TUBERCULOSIS DRUG"};
    public final static String [] SUSCEPTIBLE_TO_TB_DRUG = {"SUSCEPTIBLE TO TUBERCULOSIS DRUG"};
    public final static String [] OTHER_MYCOBACTERIA_NON_CODED = {"OTHER MYCOBACTERIA NON-CODED"};
    public final static String [] SCANTY = {"SCANTY"};
    public final static String [] SPUTUM = {"SPUTUM"};
    public final static String [] SPUTUM_COLLECTION_DATE = {"SPUTUM COLLECTION DATE"};
    public final static String [] SAMPLE_SOURCE = {"TUBERCULOSIS SAMPLE SOURCE"};

    public final static String [] SMEAR_CONSTRUCT = {"TUBERCULOSIS SMEAR MICROSCOPY CONSTRUCT"};
    public final static String [] SMEAR_METHOD = {"TUBERCULOSIS SMEAR MICROSCOPY METHOD"};
    public final static String [] SMEAR_RESULT = {"TUBERCULOSIS SMEAR RESULT"};
    public final static String [] TEST_DATE_ORDERED = {"TUBERCULOSIS TEST DATE ORDERED"};
    public final static String [] TEST_DATE_RECEIVED = {"TUBERCULOSIS TEST DATE RECEIVED"};
    public final static String [] TEST_RESULT_DATE = {"TUBERCULOSIS TEST RESULT DATE"};
    public final static String [] TEST_START_DATE = {"TUBERCULOSIS TEST START DATE"};
    public final static String [] TYPE_OF_ORGANISM = {"TYPE OF ORGANISM"};
    public final static String [] TYPE_OF_ORGANISM_NON_CODED = {"TYPE OF ORGANISM NON-CODED"};
    public final static String [] SPECIMEN_ID = {"TUBERCULOSIS SPECIMEN ID"};
    public static final String SPECIMEN_ID_CODE = "TUBERCULOSIS SPECIMEN ID";


    public final static String [] SPECIMEN_APPEARANCE = {"APPEARANCE OF SPECIMEN"};
    public static final String SPECIMEN_APPEARANCE_CODE = "APPEARANCE OF SPECIMEN";
    public final static String [] SPECIMEN_COMMENTS =  {"TUBERCULOSIS SPECIMEN COMMENTS"};
    public static final String SPECIMEN_COMMENTS_CODE = "TUBERCULOSIS SPECIMEN COMMENTS";
    public final static String [] WAITING_FOR_TEST_RESULTS = {"WAITING FOR TEST RESULTS"};
    public final static String [] DST_CONTAMINATED = {"DST CONTAMINATED"};
    public final static String [] SCANNED_LAB_REPORT = {"SCANNED LAB REPORT"};
    public final static String [] DAYS_TO_POSITIVITY = {"DAYS TO POSITIVITY"};

    // Lab Results
    public final static String [] STRONGLY_POSITIVE = {"STRONGLY POSITIVE"};
    public final static String [] MODERATELY_POSITIVE = {"MODERATELY POSITIVE"};
    public final static String [] WEAKLY_POSITIVE = {"WEAKLY POSITIVE"};
    public final static String [] POSITIVE = {"POSITIVE"};
    public final static String [] NEGATIVE = {"NEGATIVE"};
    public final static String [] CONTAMINATED = {"CONTAMINATED"};
    public final static String [] UNSATISFACTORY_SAMPLE = {"UNSATISFACTORY SAMPLE"};

    // MDR-TB Classification
    public final static String [] CAT_4_CLASSIFICATION_PREVIOUS_DRUG_USE = {"CATEGORY 4 TUBERCULOSIS CLASSIFICATION ACCORDING TO PREVIOUS DRUG USE"};
    public final static String [] NEW = {"NEW"};
    public final static String [] PREVIOUSLY_TREATED_FIRST_LINE_DRUGS_ONLY = {"PREVIOUSLY TREATED WITH FIRST LINE DRUGS ONLY"};
    public final static String [] PREVIOUSLY_TREATED_SECOND_LINE_DRUGS = {"PREVIOUSLY TREATED WITH SECOND LINE DRUGS"};

    public final static String [] CAT_4_CLASSIFICATION_PREVIOUS_TX = {"CATEGORY 4 TUBERCULOSIS CLASSIFICATION ACCORDING TO RESULT OF PREVIOUS TREATMENT"};
    public final static String [] TREATMENT_AFTER_FAILURE_OF_FIRST_TREATMENT = {"TREATMENT AFTER FAILURE OF FIRST TREATMENT MDR-TB PATIENT"};
    public final static String [] TREATMENT_AFTER_FAILURE_OF_FIRST_RETREATMENT = {"TREATMENT AFTER FAILURE OF RE-TREATMENT MDR-TB PATIENT"};
    public final static String [] OTHER = {"OTHER"};
    public final static String [] RELAPSE = {"RELAPSE"};
    public final static String [] TRANSFER = {"TRANSFER"};

    // TODO: are these still used?
    public final static String [] MDR_TB = {"MULTI-DRUG RESISTANT TUBERCULOSIS"};
    public final static String [] XDR_TB = {"EXTENSIVE DRUG RESISTANT TUBERCULOSIS"};
    public final static String [] SUSPECTED_MDR_TB = {"SUSPECTED MULTI-DRUG TUBERCULOSIS"};
    public final static String [] TB = {"TUBERCULOSIS"};

    // Treatment Outcome
    public final static String [] MDR_TB_TX_OUTCOME = {"MULTI-DRUG RESISTANT TUBERCULOSIS TREATMENT OUTCOME"};
    public final static String [] CURED = {"CURED"};
    public final static String [] DEFAULTED = {"DEFAULTED"};
    public final static String [] DIED = {"DIED"};
    public final static String [] FAILED = {"FAILED"};
    public final static String [] TREATMENT_COMPLETE = {"TREATMENT COMPLETE"};
    public final static String [] PATIENT_TRANSFERRED_OUT = {"PATIENT TRANSFERRED OUT"};
    public final static String [] STILL_ON_TREATMENT = {"STILL ON TREATMENT"};

    // TB Type
    public final static String [] PULMONARY_TB = {"PULMONARY TUBERCULOSIS"};
    public final static String [] EXTRA_PULMONARY_TB = {"EXTRA-PULMONARY TUBERCULOSIS"};
    public final static String [] ANATOMICAL_SITE_OF_TB = {"ANATOMICAL SITE OF TUBERCULOSIS"};

    // Antiretrovirals (for HIV status section and HIV regimens)
    public final static String [] ANTIRETROVIRALS = {"ANTIRETROVIRAL DRUGS"};
    public final static String [] REASON_HIV_TX_STOPPED = {"REASON ANTIRETROVIRALS CHANGED OR STOPPED"};

    // HIV Co-infection
    public final static String [] COINFECTED_ARVS = {"COINFECTED AND ON ANTIRETROVIRALS"};
    public final static String [] CD4_COUNT = {"CD4 COUNT"};
    public final static String [] RESULT_OF_HIV_TEST = {"RESULT OF HIV TEST"};

    // Hospitalization states
    public final static String [] HOSPITALIZATION_WORKFLOW = {"HOSPITALIZATION WORKFLOW"};
    public final static String [] HOSPITALIZED = {"HOSPITALIZED"};
    public final static String [] AMBULATORY = {"AMBULATORY"};   // legacy, has been retired

    // Other
    public final static String [] UNKNOWN = {"UNKNOWN"};
    public final static String [] CLINICIAN_NOTES = {"CLINICIAN NOTES"};
    public final static String [] RETURN_VISIT_DATE = {"RETURN VISIT DATE"};
    public final static String [] TELEPHONE_NUMBER = {"TELEPHONE NUMBER"};
    public final static String [] NONE = {"NONE"};


    // Contacts (potentially legacy?)
    public final static String [] PATIENT_CONTACT_IS_KNOWN_PRIOR_OR_CURRENT_MDR_TB_CASE = {"PATIENT CONTACT IS A KNOWN PRIOR OR CURRENT MDR-TB CASE"};
    public final static String [] PATIENT_CONTACT_TB_TEST_RESULT = {"PATIENT CONTACT TUBERCULOSIS TEST RESULT"};
    public final static String [] SIMPLE_TB_TEST_RESULT = {"SIMPLE TUBERCULOSIS TEST RESULT"};
    public final static String [] SIMPLE_TB_TEST_TYPE = {"SIMPLE TUBERCULOSIS TEST TYPE"};
    public final static String [] TREATMENT_SUPPORTER_CURRENTLY_ACTIVE = {"TREATMENT SUPPORTER IS CURRENTLY ACTIVE"};


    // Adverse Effects (potentially legacy?)
    public final static String [] ADVERSE_EFFECT = {"ADVERSE EFFECT"};
    public final static String [] ADVERSE_EFFECT_ACTION_TAKEN = {"ADVERSE EFFECT ACTION TAKEN, NON-CODED"};
    public final static String [] ADVERSE_EFFECT_CONSTRUCT = {"ADVERSE EFFECT CONSTRUCT"};
    public final static String [] ADVERSE_EFFECT_DATE = {"ADVERSE EFFECT DATE"};
    public final static String [] ADVERSE_EFFECT_MEDICATION = {"ADVERSE EFFECT MEDICATION"};
    public final static String [] ADVERSE_EFFECT_MEDICATION_NON_CODED = {"ADVERSE EFFECT MEDICATION NON-CODED"};
    public final static String [] ADVERSE_EFFECT_NON_CODED = {"ADVERSE EFFECT, NON-CODED"};


    // Legacy (only used by migration controller)
    public final static String [] CULTURE_STATUS = {"MULTI-DRUG RESISTANT TUBERCULOSIS CULTURE STATUS"};

}