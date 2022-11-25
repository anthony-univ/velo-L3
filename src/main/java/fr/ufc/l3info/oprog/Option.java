package fr.ufc.l3info.oprog;

public abstract class Option implements IVelo {

    private IVelo _original;
    private double tarifOption;

    private String nameOption;

    protected Option(IVelo v, double t, String n) {
        this._original = v;
        this.tarifOption = t;
        this.nameOption = n;
    }

    @Override
    public String toString() {
        String s = this._original.toString();
        int index = s.indexOf("-");
        return s.substring(0, index-1) + ", " + this.nameOption + s.substring(index-1);
    }

    @Override
    public double tarif() { return _original.tarif() + tarifOption;
    }

    @Override
    public double kilometrage() {
        return _original.kilometrage();
    }

    @Override
    public double prochaineRevision() {
        return _original.prochaineRevision();
    }

    @Override
    public void parcourir(double km) {
        _original.parcourir(km);
    }

    @Override
    public int decrocher() {
        return _original.decrocher();
    }

    @Override
    public int arrimer() {
        return _original.arrimer();
    }

    @Override
    public void abimer() {
        _original.abimer();
    }

    @Override
    public boolean estAbime() {
        return _original.estAbime();
    }

    @Override
    public int reviser() {
        return _original.reviser();
    }

    @Override
    public int reparer() {
        return _original.reparer();
    }
}
