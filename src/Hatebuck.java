// Hatebuck.java
import java.util.*;

public class Hatebuck {
    private static List<Usuari> usuaris = new ArrayList<>();

    public static void main(String[] args) {
        // Crear usuaris per a les proves
        Usuari usuari1 = new Usuari("1", "Alice", "password123");
        Usuari usuari2 = new Usuari("2", "Bob", "securepass");
        usuari2.afegirUsuariAcceptat("1"); // Alice pot enviar missatges a Bob

        usuaris.add(usuari1);
        usuaris.add(usuari2);

        // Afegir textos de prova
        usuari1.afegirText(new Text("1", "Alice", "Text inicial d'Alice", false));
        usuari1.afegirText(new Text("2", "Alice", "Segon text d'Alice", false));
        usuari2.afegirText(new Text("3", "Bob", "Text inicial de Bob", false));

        Scanner scanner = new Scanner(System.in);
        Usuari usuariAutenticat = null;

        // Autenticació
        while (usuariAutenticat == null) {
            System.out.print("Introdueix el teu nom d'usuari: ");
            String nom = scanner.nextLine();
            System.out.print("Introdueix la contrasenya: ");
            String contrasenya = scanner.nextLine();

            usuariAutenticat = autenticarUsuari(nom, contrasenya);

            if (usuariAutenticat == null) {
                System.out.println("Usuari o contrasenya incorrectes.");
                System.out.print("Vols tornar a intentar? (S/N): ");
                String resposta = scanner.nextLine().toUpperCase();
                if (resposta.equals("N")) {
                    System.out.println("Finalitzant el programa...");
                    return;
                }
            }
        }

        mostrarMenu(usuariAutenticat, scanner);
    }

    private static Usuari autenticarUsuari(String nom, String contrasenya) {
        for (Usuari usuari : usuaris) {
            if (usuari.autenticar(contrasenya) && usuari.getNom().equals(nom)) {
                return usuari;
            }
        }
        return null;
    }

    private static void mostrarMenu(Usuari usuari, Scanner scanner) {
        int opcio;
        do {
            System.out.println("\nMenú principal:");
            System.out.println("1. Enviar missatge privat");
            System.out.println("2. Modificar text");
            System.out.println("3. Canviar relació");
            System.out.println("0. Sortir");
            System.out.print("Escull una opció: ");
            opcio = scanner.nextInt();
            scanner.nextLine(); // Consumir línia

            switch (opcio) {
                case 1:
                    System.out.print("Introdueix l'usuari receptor: ");
                    String receptorNom = scanner.nextLine();
                    Usuari receptor = trobarUsuari(receptorNom);
                    if (receptor != null) {
                        System.out.println("Entrant el missatge paraula a paraula...");
                        List<Element> missatge = llegirMissatge(scanner);
                        if (usuari.potEnviarMissatge(receptor)) {
                            receptor.rebreMissatge(new MissatgePrivat(usuari, receptor, construirMissatge(missatge)));
                        } else {
                            System.out.println("El receptor no accepta missatges del teu usuari.");
                        }
                    } else {
                        System.out.println("Usuari no trobat.");
                    }
                    break;
                case 2:
                    System.out.println("Textos disponibles per modificar:");
                    mostrarTextosModificables(usuari);
                    System.out.print("Introdueix l'ID del text a modificar: ");
                    String idText = scanner.nextLine();
                    if (usuari.potModificarText(idText)) {
                        System.out.print("Introdueix el nou contingut: ");
                        String nouContingut = scanner.nextLine();
                        usuari.modificarText(idText, nouContingut);
                        System.out.println("Text modificat correctament.");
                    } else {
                        System.out.println("No tens permís per modificar aquest text.");
                    }
                    break;
                case 3:
                    System.out.print("Introdueix l'usuari amb qui vols establir relació: ");
                    String usuariRelacio = scanner.nextLine();
                    System.out.println("Selecciona el tipus de relació:");
                    System.out.println("1. Amic");
                    System.out.println("2. Conegut");
                    System.out.println("3. Saludat");
                    System.out.print("Opció: ");
                    int tipusRelacioOpcio = scanner.nextInt();
                    scanner.nextLine();
                    String tipusRelacio;
                    switch (tipusRelacioOpcio) {
                        case 1: tipusRelacio = "Amic";
                        case 2: tipusRelacio = "Conegut";
                        case 3: tipusRelacio = "Saludat";
                        default : {
                            System.out.println("Opció no vàlida. Assignant relació com a 'Saludat'.");
                            tipusRelacio = "Saludat";
                        }
                    }
                    usuari.establirRelacio(usuariRelacio, tipusRelacio);
                    System.out.println("Relació establerta amb l'usuari.");
                    break;
                case 0:
                    System.out.println("Sortint del programa...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
        } while (opcio != 0);
    }

    private static void mostrarTextosModificables(Usuari usuari) {
        List<Text> textos = usuari.getTextos();
        for (Text text : textos) {
            System.out.println("ID: " + text.getId() + " - Contingut: " + text.getContingut());
        }
    }

    private static List<Element> llegirMissatge(Scanner scanner) {
        List<Element> elements = new ArrayList<>();
        String opcio;

        do {
            System.out.println("\nVols entrar una paraula (P) o un símbol (S)? (E per acabar): ");
            opcio = scanner.nextLine().toUpperCase();

            switch (opcio) {
                case "P":
                    System.out.print("Introdueix la paraula: ");
                    String paraula = scanner.nextLine();
                    System.out.println("Selecciona el tipus de paraula: ");
                    System.out.println("1. Normal");
                    System.out.println("2. Paraula especial");
                    System.out.print("Opció: ");
                    int tipusParaula = scanner.nextInt();
                    scanner.nextLine(); // Consumir línia
                    elements.add(new Paraula(paraula, tipusParaula));
                    break;
                case "S":
                    System.out.print("Introdueix el símbol: ");
                    String simbol = scanner.nextLine();
                    elements.add(new SignePuntuacio(simbol));
                    break;
                case "E":
                    System.out.println("Finalitzant l'entrada del missatge...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
        } while (!opcio.equals("E"));

        return elements;
    }

    private static String construirMissatge(List<Element> elements) {
        StringBuilder missatge = new StringBuilder();
        for (Element element : elements) {
            missatge.append(element.mostrar()).append(" ");
        }
        return missatge.toString().trim();
    }

    private static Usuari trobarUsuari(String nom) {
        for (Usuari usuari : usuaris) {
            if (usuari.getNom().equals(nom)) {
                return usuari;
            }
        }
        return null;
    }
}
