public class MissatgePrivat {
    private Usuari emissor;
    private Usuari receptor;
    private String contingut;

    public MissatgePrivat(Usuari emissor, Usuari receptor, String contingut) {
        this.emissor = emissor;
        this.receptor = receptor;
        this.contingut = contingut;
    }

    public Usuari getEmissor() {
        return emissor;
    }

    public String getContingut() {
        return contingut;
    }
}
