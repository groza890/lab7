package problema2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import problema2.InstrumentMuzical;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class main {
    private static Set<InstrumentMuzical> instrumenteSet = new HashSet<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        // Creăm câteva instrumente pentru a popula colecția
        adaugaInstrumente();

        boolean rulare = true;

        while (rulare) {
            System.out.println("\n--- Meniu ---");
            System.out.println("1. Afișează colecția de instrumente");
            System.out.println("2. Salvează colecția în fișier");
            System.out.println("3. Încarcă colecția din fișier");
            System.out.println("4. Verifică duplicate în colecție");
            System.out.println("5. Șterge instrumentele cu preț > 3000 RON");
            System.out.println("6. Afișează chitările");
            System.out.println("7. Afișează tobele");
            System.out.println("8. Afișează chitara cu cele mai multe corzi");
            System.out.println("9. Afișează tobele acustice");
            System.out.println("0. Ieșire");
            System.out.print("Alege o opțiune: ");

            int optiune = new java.util.Scanner(System.in).nextInt();

            switch (optiune) {
                case 1:
                    afiseazaInstrumente();
                    break;
                case 2:
                    salveazaInFisier();
                    break;
                case 3:
                    incarcaDinFisier();
                    break;
                case 4:
                    verificaDuplicate();
                    break;
                case 5:
                    stergeInstrumentePeste3000();
                    break;
                case 6:
                    afiseazaChitarele();
                    break;
                case 7:
                    afiseazaTobele();
                    break;
                case 8:
                    afiseazaChitaraCuCeleMaiMulteCorzi();
                    break;
                case 9:
                    afiseazaTobeleAcustice();
                    break;
                case 0:
                    rulare = false;
                    System.out.println("Ieșire din program.");
                    break;
                default:
                    System.out.println("Opțiune invalidă.");
            }
        }
    }

    private static void adaugaInstrumente() {
        instrumenteSet.add(new Chitara("Fender", 2500, TipChitara.ELECTRICA, 6));
        instrumenteSet.add(new Chitara("Gibson", 3500, TipChitara.ACUSTICA, 6));
        instrumenteSet.add(new Chitara("Yamaha", 1500, TipChitara.CLASICA, 6));

        instrumenteSet.add(new SetTobe("Pearl", 4000, TipTobe.ELECTRONICE, 5, 3));
        instrumenteSet.add(new SetTobe("Ludwig", 3200, TipTobe.ACUSTICE, 6, 4));
        instrumenteSet.add(new SetTobe("Tama", 2200, TipTobe.ACUSTICE, 4, 2));
    }

    private static void afiseazaInstrumente() {
        if (instrumenteSet.isEmpty()) {
            System.out.println("Colecția de instrumente este goală.");
        } else {
            instrumenteSet.forEach(System.out::println);
        }
    }

    private static void salveazaInFisier() {
        try {
            mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator()); // Activăm tipizarea polimorfă
            mapper.writeValue(new File("src/main/resources/instrumente.json"), instrumenteSet);
            System.out.println("Colecția a fost salvată în fișier.");
        } catch (IOException e) {
            System.out.println("Eroare la salvarea fișierului: " + e.getMessage());
        }
    }

    private static void incarcaDinFisier() {
        try {
            File inputFile = new File("instrumente.json");
            if (inputFile.exists()) {
                instrumenteSet = mapper.readValue(inputFile, TypeFactory.defaultInstance().constructCollectionType(Set.class, InstrumentMuzical.class));
                System.out.println("Colecția a fost încărcată din fișier.");
            } else {
                System.out.println("Fișierul `instrumente.json` nu a fost găsit.");
            }
        } catch (IOException e) {
            System.out.println("Eroare la încărcarea fișierului: " + e.getMessage());
        }
    }

    private static void verificaDuplicate() {
        Chitara chitaraNoua = new Chitara("Fender", 2500, TipChitara.ELECTRICA, 6);
        if (instrumenteSet.contains(chitaraNoua)) {
            System.out.println("Instrumentul deja există în colecție.");
        } else {
            instrumenteSet.add(chitaraNoua);
            System.out.println("Instrumentul a fost adăugat.");
        }
    }

    private static void stergeInstrumentePeste3000() {
        instrumenteSet.removeIf(instrument -> instrument.pret > 3000);
        System.out.println("Instrumentele cu prețul mai mare de 3000 RON au fost șterse.");
    }

    private static void afiseazaChitarele() {
        instrumenteSet.stream()
                .filter(instrument -> instrument instanceof Chitara)
                .forEach(System.out::println);
    }

    private static void afiseazaTobele() {
        instrumenteSet.stream()
                .filter(instrument -> instrument instanceof SetTobe)
                .forEach(System.out::println);
    }

    private static void afiseazaChitaraCuCeleMaiMulteCorzi() {
        instrumenteSet.stream()
                .filter(instrument -> instrument instanceof Chitara)
                .map(instrument -> (Chitara) instrument)
                .max((c1, c2) -> Integer.compare(c1.nr_corzi, c2.nr_corzi))
                .ifPresent(System.out::println);
    }

    private static void afiseazaTobeleAcustice() {
        instrumenteSet.stream()
                .filter(instrument -> instrument instanceof SetTobe)
                .map(instrument -> (SetTobe) instrument)
                .filter(tobe -> tobe.tip_tobe == TipTobe.ACUSTICE)
                .sorted((t1, t2) -> Integer.compare(t1.nr_tobe, t2.nr_tobe))
                .forEach(System.out::println);
    }
}
