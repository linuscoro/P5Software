public class SignePuntuacio extends Element {
    private String simbol;

    public SignePuntuacio(String simbol) {
        this.simbol = simbol;
    }

    @Override
    public String mostrar() {
        return simbol;
    }
}