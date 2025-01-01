public class Text {
    private String id;
    private String autor;
    private String contingut;
    private boolean privat;

    public Text(String id, String autor, String contingut, boolean privat) {
            this.id = id;
            this.autor = autor;
            this.contingut = contingut;
            this.privat = privat;
        }

        public String getId() {
            return id;
        }

        public String getContingut() {
            return contingut;
        }

        public void setContingut(String nouContingut) {
            this.contingut = nouContingut;
        }
}
