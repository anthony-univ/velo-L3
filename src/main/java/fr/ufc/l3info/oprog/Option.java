package fr.ufc.l3info.oprog;

import java.util.Locale;

public abstract class Option implements IVelo{

    private IVelo _originale;
    private double tarifOption;

    private String nameOption;

    public Option(IVelo o, double t,String n) {
        _originale = o;
        tarifOption = t;
        nameOption = n;
    }

    public String toString(){
        String str = _originale.toString();
        int indexTirer = str.indexOf('-');
        return str.substring(0,indexTirer-1)+", "+nameOption+str.substring(indexTirer-1);
    }

    @Override
    public double tarif() {
        return _originale.tarif() + tarifOption;
    }


    @Override
    public double kilometrage() {return _originale.kilometrage();}

    @Override
    public double prochaineRevision() {return _originale.prochaineRevision();}

    @Override
    public void parcourir(double km) {_originale.parcourir(km);}


    @Override
    public int decrocher() {return _originale.decrocher();}

    @Override
    public int arrimer() {return _originale.arrimer();}

    @Override
    public void abimer() {_originale.abimer();}

    @Override
    public boolean estAbime() {return _originale.estAbime();}


    @Override
    public int reviser() {return _originale.reviser();}

    @Override
    public int reparer() {return _originale.reparer();}
}
