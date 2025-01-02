public class Relacio {
    private String tipusRelacio;

    public Relacio(String tipusRelacio) {
        this.tipusRelacio = tipusRelacio;
    }

    public void canviarTipusRelacio(String nouTipus) {
        this.tipusRelacio = nouTipus;
    }

    public String getTipusRelacio() {
        return tipusRelacio;
    }
}
