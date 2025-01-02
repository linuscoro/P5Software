// Hatebuck.java
import java.util.*;

public class Hatebuck {
    private static List<Usuari> usuaris = new ArrayList<>();

    public static void main(String[] args) {
        // Crear usuaris per a les proves
        Usuari usuari1 = new Usuari("1", "Hamza", "Hamza123");
        Usuari usuari2 = new Usuari("2", "Marceli", "Marceli123");
        Usuari usuari3 = new Usuari("3", "Alice", "Alice123");
        Usuari usuari4 = new Usuari("4", "Bob", "Bob123");
        Usuari usuari5 = new Usuari("5", "Clara", "Clara123");
        Usuari usuari6 = new Usuari("6", "David", "David123");

        // Relacions inicials
        usuari1.afegirUsuariAcceptat("2");
        usuari1.establirRelacio("2", "Amic"); // Hamza és amic de Marceli

        usuari1.afegirUsuariAcceptat("3");
        usuari1.establirRelacio("3", "Conegut"); // Hamza és conegut d'Alice

        usuari2.afegirUsuariAcceptat("1");
        usuari2.establirRelacio("1", "Conegut"); // Hamza és conegut de Marceli

        usuari3.afegirUsuariAcceptat("1");
        usuari3.establirRelacio("1", "Amic"); // Hamza és amic d'Alice

        usuari4.afegirUsuariAcceptat("3");
        usuari4.establirRelacio("3", "Conegut"); // Alice és coneguda de Bob

        usuari5.afegirUsuariAcceptat("4");
        usuari5.establirRelacio("4", "Amic"); // Bob és amic de Clara

        usuari6.afegirUsuariAcceptat("5");
        usuari6.establirRelacio("5", "Saludat"); // Clara és saludada de David

        usuaris.add(usuari1);
        usuaris.add(usuari2);
        usuaris.add(usuari3);
        usuaris.add(usuari4);
        usuaris.add(usuari5);
        usuaris.add(usuari6);

        // Afegir textos de prova
        usuari1.afegirText(new Text("1", "Hamza", "Text inicial de Hamza", false));
        usuari1.afegirText(new Text("2", "Hamza", "Segon text de Hamza", false));
        usuari2.afegirText(new Text("3", "Marceli", "Text inicial de Marceli", false));
        usuari3.afegirText(new Text("4", "Alice", "Text inicial d'Alice", false));
        usuari3.afegirText(new Text("5", "Alice", "Segon text d'Alice", false));
        usuari4.afegirText(new Text("6", "Bob", "Text inicial de Bob", false));
        usuari4.afegirText(new Text("7", "Bob", "Segon text de Bob", false));
        usuari5.afegirText(new Text("8", "Clara", "Text inicial de Clara", false));
        usuari5.afegirText(new Text("9", "Clara", "Segon text de Clara", false));
        usuari6.afegirText(new Text("10", "David", "Text inicial de David", false));
        usuari6.afegirText(new Text("11", "David", "Segon text de David", false));

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
                        List<Element> missatge = llegirElements(scanner);
                        if (usuari.potEnviarMissatge(receptor)) {
                            receptor.rebreMissatge(new MissatgePrivat(usuari, receptor, construirMissatge(missatge)));
                            System.out.println("Missatge enviat a " + receptorNom + ": " + construirMissatge(missatge));
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
                        System.out.println("Entrant el nou contingut paraula a paraula...");
                        List<Element> nouContingut = llegirElements(scanner);
                        usuari.modificarText(idText, construirMissatge(nouContingut));
                        System.out.println("Text modificat correctament. Nou contingut:");
                        mostrarTextosModificables(usuari);
                    } else {
                        System.out.println("No tens permís per modificar aquest text.");
                    }
                    break;
                case 3:
                    System.out.println("Llistat de persones amb les que tens relació:");
                    Map<String, Relacio> relacionsMap = usuari.getRelacionsMap();
                    if (relacionsMap.isEmpty()) {
                        System.out.println("No tens cap relació per modificar.");
                        break;
                    }
                    for (Map.Entry<String, Relacio> entry : relacionsMap.entrySet()) {
                        Usuari relUsuari = trobarUsuariPerId(entry.getKey());
                        if (relUsuari != null) {
                            System.out.println("Usuari: " + relUsuari.getNom() + " - Relació: " + entry.getValue().getTipusRelacio());
                        }
                    }
                    System.out.print("Introdueix el nom de la persona amb qui vols modificar la relació: ");
                    String nomRelacio = scanner.nextLine();
                    Usuari usuariRelacio = trobarUsuari(nomRelacio);
                    if (usuariRelacio == null || !relacionsMap.containsKey(usuariRelacio.getId())) {
                        System.out.println("Selecció no vàlida o relació no existent.");
                        break;
                    }
                    System.out.println("Selecciona el tipus de relació:");
                    System.out.println("1. Amic");
                    System.out.println("2. Conegut");
                    System.out.println("3. Saludat");
                    System.out.print("Opció: ");
                    int tipusRelacioOpcio = scanner.nextInt();
                    scanner.nextLine();
                    String tipusRelacio;
                    switch (tipusRelacioOpcio) {
                        case 1:
                            tipusRelacio = "Amic";
                            break;
                        case 2:
                            tipusRelacio = "Conegut";
                            break;
                        case 3:
                            tipusRelacio = "Saludat";
                            break;
                        default:
                            System.out.println("Opció no vàlida. Assignant relació com a 'Saludat'.");
                            tipusRelacio = "Saludat";
                            break;
                    }
                    usuari.establirRelacio(usuariRelacio.getId(), tipusRelacio);
                    System.out.println("Relació establerta amb " + nomRelacio + " com a " + tipusRelacio);
                    System.out.println("Estat actual de les relacions:");
                    for (Map.Entry<String, Relacio> entry : relacionsMap.entrySet()) {
                        Usuari relUsuari = trobarUsuariPerId(entry.getKey());
                        if (relUsuari != null) {
                            System.out.println("Usuari: " + relUsuari.getNom() + " - Relació: " + entry.getValue().getTipusRelacio());
                        }
                    }
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

    private static List<Element> llegirElements(Scanner scanner) {
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
                    System.out.println("Finalitzant l'entrada...");
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

    private static Usuari trobarUsuariPerId(String id) {
        for (Usuari usuari : usuaris) {
            if (usuari.getId().equals(id)) {
                return usuari;
            }
        }
        return null;
    }
}
