package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClosestStationIteratorTest {

    final String vraiPath = "./target/classes/data/";

    private Ville v = new Ville();

    @Test
    public void ClosetStationIteratorSisNull() {
        Set<Station> ss = new HashSet<>();
        ClosestStationIterator c = new ClosestStationIterator(ss,null);

        Assert.assertEquals(false,c.hasNext());
    }

    @Test
    public void ClosetStationIteratorStationsIsNull() {
        Station s = new Station("test",47,6,10);
        ClosestStationIterator c = new ClosestStationIterator(null,s);

        Assert.assertEquals(false,c.hasNext());
    }

    @Test
    public void hasNext() {
        Set<Station> ss = new HashSet<>();
        Station s = new Station("test",47,6,10);
        ss.add(s);
        ClosestStationIterator c = new ClosestStationIterator(ss,s);

        Assert.assertEquals(true,c.hasNext());
    }

    @Test
    public void hasNextFirstNotINset() {
        Set<Station> ss = new HashSet<>();
        Station s = new Station("test",47,6,10);
        Station sss = new Station("teset",47,6,10);
        ss.add(sss);
        ClosestStationIterator c = new ClosestStationIterator(ss,s);

        Assert.assertEquals(false,c.hasNext());
    }


    @Test
    public void next(){
        Station s1 = new Station("Gare Viotte",47.246501551427329,6.022715427111734,10);
        Station s2 = new Station("Saint Jacques",47.234970856296655,6.020961226259578,12);
        Station s3 = new Station("Granvelle",47.235310381596307,6.025353690286829,10);
        Station s4 = new Station("Victor Hugo",47.234841756122286,6.029500146346702,8);
        Station s5 = new Station("Mairie",47.236701239866434,6.022315779424154,10);
        Station s6 = new Station("Jean Cornet",47.236109146208591,6.029768734531883,12);

        Set<Station> ss = new HashSet<>();

        ss.add(s1);
        ss.add(s2);
        ss.add(s3);
        ss.add(s4);
        ss.add(s5);
        ss.add(s6);

        ClosestStationIterator c = new ClosestStationIterator(ss,s1);

        Assert.assertEquals(s1.getNom(),c.next().getNom());
        Assert.assertEquals(s5.getNom(),c.next().getNom());
        Assert.assertEquals(s2.getNom(),c.next().getNom());
        Assert.assertEquals(s3.getNom(),c.next().getNom());
        Assert.assertEquals(s4.getNom(),c.next().getNom());
        Assert.assertEquals(s6.getNom(),c.next().getNom());

    }



}
