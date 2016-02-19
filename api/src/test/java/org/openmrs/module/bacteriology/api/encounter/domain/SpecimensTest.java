package org.openmrs.module.bacteriology.api.encounter.domain;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SpecimensTest {

    @Test
    public void shouldSortSpecimensBasedOnDateCollected() throws Exception{
        Specimen specimen1 = new Specimen(){{
            this.setDateCollected(new Date(10000000));
        }};
        Specimen specimen2 = new Specimen(){{
            this.setDateCollected(new Date(40000000));
        }};
        Specimen specimen3 = new Specimen(){{
            this.setDateCollected(new Date(20000000));
        }};

        List<Specimen> unSortedSpecimenList = Arrays.asList(specimen1, specimen2, specimen3);
        List<Specimen> expectedSortedSpecimenList = Arrays.asList(specimen2, specimen3, specimen1);

        Specimens unSortedSpecimens = new Specimens(unSortedSpecimenList);
        Specimens sortedSpecimens = unSortedSpecimens.sortByDateCollected();
        Specimens expectedSortedSpecimens = new Specimens(expectedSortedSpecimenList);

        assertEquals(expectedSortedSpecimens,sortedSpecimens);
    }

    @Test
    public void shouldReturnNewSpecimensWhenSortedSpecimensDiffersFromUnsortedSpecimens() throws Exception {
        Specimen specimen1 = new Specimen();
        Specimen specimen2 = new Specimen();
        Collection<Specimen> unSortedSpecimenList = Arrays.asList(specimen1, specimen2);

        specimen1.setDateCollected(new Date(10000000));
        specimen2.setDateCollected(new Date(20000000));

        Specimens unSortedSpecimens = new Specimens(unSortedSpecimenList);
        Specimens sortedSpecimens = unSortedSpecimens.sortByDateCollected();
        assertNotEquals(unSortedSpecimens, sortedSpecimens);
    }

    @Test
    public void shouldReturnTheSpecimensWhenSortedSpecimensIsSameAsUnsortedSpecimens() throws Exception {
        Specimen specimen1 = new Specimen();
        Specimen specimen2 = new Specimen();
        Collection<Specimen> unSortedSpecimenList = Arrays.asList(specimen1, specimen2);

        specimen1.setDateCollected(new Date(40000000));
        specimen2.setDateCollected(new Date(20000000));

        Specimens unSortedSpecimens = new Specimens(unSortedSpecimenList);
        Specimens sortedSpecimens = unSortedSpecimens.sortByDateCollected();
        assertEquals(unSortedSpecimens, sortedSpecimens);
    }
}