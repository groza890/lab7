package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Map<Integer, Carte> cartiMap = new HashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        incarcaCartiDinFisier();

        Scanner scanner = new Scanner(System.in);
        boolean rulare = true;

        while (rulare) {
            System.out.println("\n--- Meniu ---");
            System.out.println("1. Afișează colecția de cărți");
            System.out.println("2. Șterge o carte după ID");
            System.out.println("3. Adaugă o carte nouă");
            System.out.println("4. Salvează colecția în fișier");
            System.out.println("5. Afișează cărțile unui autor specific");
            System.out.println("6. Afișează cărțile unui autor ordonate după titlu");
            System.out.println("7. Afișează cea mai veche carte a unui autor");
            System.out.println("0. Ieșire");
            System.out.print("Alege o opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine(); // Consumă linia rămasă

            switch (optiune) {
                case 1 -> afiseazaCarti();
                case 2 -> stergeCarte(scanner);
                case 3 -> adaugaCarte(scanner);
                case 4 -> salveazaInFisier();
                case 5 -> afiseazaCartiAutor(scanner);
                case 6 -> afiseazaCartiAutorOrdonate(scanner);
                case 7 -> afiseazaCeaMaiVecheCarte(scanner);
                case 0 -> {
                    rulare = false;
                    System.out.println("Ieșire din program.");
                }
                default -> System.out.println("Opțiune invalidă. Te rog să încerci din nou.");
            }
        }

        scanner.close();
    }

    private static void incarcaCartiDinFisier() {
        try {
            // Folosim calea relativă pentru fișierul JSON
            File inputFile = new File(Main.class.getClassLoader().getResource("carti.json").toURI());
            if (inputFile.exists()) {
                MapType mapType = TypeFactory.defaultInstance().constructMapType(HashMap.class, Integer.class, Carte.class);
                cartiMap = mapper.readValue(inputFile, mapType);

                if (cartiMap.isEmpty()) {
                    System.out.println("Fișierul `carti.json` nu conține date sau nu a fost încărcat corect.");
                } else {
                    System.out.println("Datele din `carti.json` au fost încărcate cu succes.");
                }
            } else {
                System.out.println("Fișierul `carti.json` nu a fost găsit în `resources`.");
            }
        } catch (IOException | NullPointerException | URISyntaxException e) {
            System.out.println("Eroare la încărcarea fișierului: " + e.getMessage());
        }
    }


    private static void afiseazaCarti() {
        if (cartiMap.isEmpty()) {
            System.out.println("Colecția de cărți este goală.");
        } else {
            System.out.println("Colecția de cărți:");
            cartiMap.forEach((id, carte) -> System.out.println("ID: " + id + ", Carte: " + carte));
        }
    }


    private static void stergeCarte(Scanner scanner) {
        System.out.print("Introdu ID-ul cărții de șters: ");
        int id = scanner.nextInt();
        if (cartiMap.remove(id) != null) {
            System.out.println("Cartea cu ID-ul " + id + " a fost ștearsă.");
        } else {
            System.out.println("Cartea cu ID-ul " + id + " nu există.");
        }
    }

    private static void adaugaCarte(Scanner scanner) {
        System.out.print("Introdu ID-ul cărții noi: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumă linia rămasă
        System.out.print("Introdu titlul cărții: ");
        String titlu = scanner.nextLine();
        System.out.print("Introdu autorul cărții: ");
        String autor = scanner.nextLine();
        System.out.print("Introdu anul apariției: ");
        int anAparitie = scanner.nextInt();
        cartiMap.putIfAbsent(id, new Carte(titlu, autor, anAparitie));
        System.out.println("Cartea a fost adăugată.");
    }

    private static void salveazaInFisier() {
        try {
            mapper.writeValue(new File("src/main/resources/carti.json"), cartiMap);
            System.out.println("Modificările au fost salvate în fișier.");
        } catch (IOException e) {
            System.out.println("Eroare la salvarea fișierului: " + e.getMessage());
        }
    }

    private static void afiseazaCartiAutor(Scanner scanner) {
        System.out.print("Introdu numele autorului: ");
        String autor = scanner.nextLine();
        Set<Carte> cartiAutor = cartiMap.values().stream()
                .filter(carte -> autor.equals(carte.autorul()))
                .collect(Collectors.toSet());
        if (cartiAutor.isEmpty()) {
            System.out.println("Nu există cărți ale acestui autor.");
        } else {
            cartiAutor.forEach(System.out::println);
        }
    }

    private static void afiseazaCartiAutorOrdonate(Scanner scanner) {
        System.out.print("Introdu numele autorului: ");
        String autor = scanner.nextLine();
        Set<Carte> cartiAutor = cartiMap.values().stream()
                .filter(carte -> autor.equals(carte.autorul()))
                .sorted(Comparator.comparing(Carte::titlul))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (cartiAutor.isEmpty()) {
            System.out.println("Nu există cărți ale acestui autor.");
        } else {
            System.out.println("Cărțile autorului " + autor + " ordonate după titlu:");
            cartiAutor.forEach(System.out::println);
        }
    }

    private static void afiseazaCeaMaiVecheCarte(Scanner scanner) {
        System.out.print("Introdu numele autorului: ");
        String autor = scanner.nextLine();
        cartiMap.values().stream()
                .filter(carte -> autor.equals(carte.autorul()))
                .min(Comparator.comparingInt(Carte::anul))
                .ifPresentOrElse(
                        carte -> System.out.println("Cea mai veche carte a autorului " + autor + ": " + carte),
                        () -> System.out.println("Nu există cărți ale acestui autor.")
                );
    }
}
