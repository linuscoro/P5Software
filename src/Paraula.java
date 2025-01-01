public class Paraula extends Element {
    private String contingut;
    private int tipus; // 1: Normal, 2: Especial

    public Paraula(String contingut, int tipus) {
        this.contingut = contingut;
        this.tipus = tipus;
    }

    @Override
    public String mostrar() {
        return tipus == 1 ? contingut : "[Especial] " + contingut;
    }
}