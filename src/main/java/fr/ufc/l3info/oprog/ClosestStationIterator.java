package fr.ufc.l3info.oprog;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClosestStationIterator implements Iterator<Station> {

    private Station current;
    private Set<Station> stations;

    public ClosestStationIterator(Set<Station> stations, Station s) {
            if(s == null) {
                this.current = null;
                this.stations = new HashSet<>();
            }else {
                this.current = s;
                this.stations = new HashSet<>(stations);
            }
    }

    public boolean hasNext() {
        return this.stations.size() !=0;
    }

    public Station next() {
        return getNext();
    }

    private Station getNext() {
        Station stationBefore = this.current;

        this.stations.remove(this.current);

        Station stationPlusProcheAvolDoiseau = null;
        double min = 0;

        for(Station s : this.stations) {

            double distance = s.distance(this.current);

            if(stationPlusProcheAvolDoiseau == null || distance < min){
                min = distance;
                stationPlusProcheAvolDoiseau = s;
            }
        }

        this.current = stationPlusProcheAvolDoiseau;

        return stationBefore;
    }

    public void remove() {

    }
}
