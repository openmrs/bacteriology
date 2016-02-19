package org.openmrs.module.bacteriology.api.encounter.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Specimens extends ArrayList<Specimen> {
    public Specimens() {
        super();
    }

    public Specimens(Collection<Specimen> specimens) {
        super(specimens);
    }

    public Specimens sortByDateCollected(){
        Specimens sortedCopy = (Specimens) this.clone();
        Collections.sort(sortedCopy, new Comparator<Specimen>() {
            @Override
            public int compare(Specimen specimen1, Specimen specimen2) {
                return specimen2.getDateCollected().compareTo(specimen1.getDateCollected());
            }
        });
        return sortedCopy;
    }
}
