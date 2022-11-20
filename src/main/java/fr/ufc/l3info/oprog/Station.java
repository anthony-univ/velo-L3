package fr.ufc.l3info.oprog;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Station {

    private String nom = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private int capacite = 0;
    private IRegistre registre = null;
    private IVelo[] bornes;

    public Station(String nom, double latitude, double longitude, int capacite) {
        this.nom = nom;

        if(latitude >= -90.0 && latitude <= 90.0) {
            this.latitude = latitude;
        }
        if(longitude >= -90.0 && longitude <= 90.0) {
            this.longitude = longitude;
        }
        if(capacite >= 0) {
            this.capacite = capacite;
        }
        this.bornes = new IVelo[this.capacite];
    }

    public void setRegistre(IRegistre registre) {
        this.registre = registre;
    }

    public String getNom() {
        return this.nom;
    }

    public int capacite() {
        return  this.capacite;
    }

    public int nbBornesLibres() {
        int nbBornesLibres = 0;
        for (IVelo velo: this.bornes) {
            if(velo == null) {
                ++nbBornesLibres;
            }
        }
        return  nbBornesLibres;
    }

    public IVelo veloALaBorne(int b) {
        if(b < 1 || b > this.capacite) {
            return null;
        }
        return this.bornes[b-1];
    }

    public IVelo emprunterVelo(Abonne a, int b) {
        IVelo v = veloALaBorne(b);
        if (this.registre == null || a == null || v == null) {
            return null;
        }
        if(v.estAbime()) { // nouvelle fonctioanlite
            return null;
        }

        if(a.estBloque()) {
            return null;
        }

        if(b < 1 || b > this.capacite) {
            return null;
        }
        if(this.registre.nbEmpruntsEnCours(a) !=0) {
            return null;
        }
        int emprunter = this.registre.emprunter(a, v, maintenant());
        if(emprunter !=0) {
            return null;
        }

        if(v.decrocher() < 0) {
            return null;
        }

        this.bornes[b-1] = null;
        return v;
    }

    public int arrimerVelo(IVelo v, int b) {
        if(v == null || b < 1 || b > this.capacite) {
            return -1;
        }
        if(this.registre == null || this.bornes[b-1] != null) {
            return -2;
        }
        if(v.arrimer() == -1) {
            return -3;
        }

        this.bornes[b-1] = v;
        if(v.estAbime()) { // nouvelle fonctioanlite
            Abonne a = registre.emprunteur(v);
            if(a!=null) {
                a.bloquer();
            }
        }

        if(this.registre.retourner(v, maintenant()) != 0) {
            return -4;
        }

        return 0;
    }


    public void equilibrer(Set<IVelo> velos) {
        Set<IVelo> velosDeCoter = new HashSet<IVelo>();
        if(velos == null) return;

        Iterator<IVelo> value = velos.iterator();
        while (value.hasNext()) {
            IVelo veloSuivant = value.next();
            if(veloSuivant == null) {
                velos.remove(veloSuivant);
                break;
            }
        }

        value = velos.iterator();
        for (int i = 0; i < this.bornes.length; ++i) {
            if (this.bornes[i] != null && this.bornes[i].estAbime()) {
                velosDeCoter.add(this.bornes[i]); //mettre de coter le velo abime
                this.bornes[i].decrocher();
                this.bornes[i] = null; //enlver le velo abime de la borne
                while(value.hasNext()) {
                    IVelo veloSuivant = value.next();
                    if(!veloSuivant.estAbime() && veloSuivant.prochaineRevision() > 0) {
                        this.bornes[i] = veloSuivant; //mettre velo sur la borne
                        this.bornes[i].arrimer();
                        value.remove(); //enlver velo camion
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < this.bornes.length; ++i) {
            if (this.bornes[i] != null && this.bornes[i].prochaineRevision() <= 0) {
                while(value.hasNext()) {
                    IVelo veloSuivant = value.next();
                    if(!veloSuivant.estAbime() && veloSuivant.prochaineRevision() > 0) {
                        velosDeCoter.add(this.bornes[i]); //mettre de coter le velo a reviser
                        this.bornes[i].decrocher();
                        this.bornes[i] = null; //enlver le velo a reviser de la borne

                        this.bornes[i] = veloSuivant; //mettre velo sur la borne
                        this.bornes[i].arrimer();
                        value.remove(); //enlever velo camion
                        break;
                    }
                }
            }
        }

        // remplir la sation si pas assez pleine que si assez de velo en bon etat dans le camion
        for (int i = 0; i < bornes.length && capacite -nbBornesLibres() < Math.ceil(this.bornes.length/2.0) && value.hasNext();) {
            if(bornes[i] != null) {
                ++i;
                continue;
            }
            IVelo veloSuivant = value.next();
            if(!veloSuivant.estAbime() && veloSuivant.prochaineRevision() > 0) {
                this.bornes[i] = veloSuivant; //mettre velo sur la borne
                this.bornes[i].arrimer();
                value.remove(); //enlever velo camion
                ++i;
            }
        }

        //vider station si trop pleine en priviligiant les velo a reviser
        for (int i = 0; i < bornes.length && capacite -nbBornesLibres() > Math.ceil(this.bornes.length/2.0); ++i) {
            if(this.bornes[i] != null && this.bornes[i].prochaineRevision() <= 0) {
                velosDeCoter.add(this.bornes[i]); //mettre de coter le velo a reviser
                this.bornes[i].decrocher();
                this.bornes[i] = null; //enlver le velo a reviser de la borne
            }
        }


        for (int i = 0; i < bornes.length && capacite -nbBornesLibres() > Math.ceil(this.bornes.length/2.0); ++i) {
            if(this.bornes[i] != null && this.bornes[i].prochaineRevision() > 0) {
                velosDeCoter.add(this.bornes[i]); //mettre de coter le velo bon etat
                this.bornes[i].decrocher();
                this.bornes[i] = null; //enlver le velo a de la borne
            }
        }

        for (IVelo v: velosDeCoter) {
            velos.add(v);
        }
    }

    public double distance(Station s) {
        double R = 6371;
        double phi1 = this.latitude * Math.PI/180;
        double phi2 = s.latitude * Math.PI/180;
        double deltaPhi = (s.latitude - this.latitude) * Math.PI/180;
        double deltaLambda = (s.longitude - this.longitude) * Math.PI/180;
        double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
                                Math.cos(phi1) * Math.cos(phi2) *
                                Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public long maintenant() {
        return System.currentTimeMillis();
    }

}
