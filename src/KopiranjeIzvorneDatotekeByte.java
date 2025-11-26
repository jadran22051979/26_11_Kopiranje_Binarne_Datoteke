import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class KopiranjeIzvorneDatotekeByte {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            //  Unos putanje
            System.out.print("Unesite putanju do originalne datoteke: ");
            String originalnaPutanja = scanner.nextLine();

            System.out.print("Unesite putanju i naziv kopije datoteke: ");
            String kopiranaPutanja = scanner.nextLine();

            File originalnaDatoteka = new File(originalnaPutanja);
            File kopiranaDatoteka = new File(kopiranaPutanja);
            File kopiranaDirektorij = kopiranaDatoteka.getParentFile();

            //  Provjera postoji li originalna datoteka
            if (!originalnaDatoteka.exists()) {
                System.out.println("Greška: originalna datoteka ne postoji.");
                return;
            }
            //  Provjera imamo li pravo čitanja za originalnu datoteku
            if (!originalnaDatoteka.canRead()) {
                System.out.println("GREŠKA: Nemate pravo čitanja originalne datoteke.");
                return;
            }
            //Provjera da li možemo pisati u odredišni direktorij
            if (kopiranaDirektorij != null && !kopiranaDirektorij.canWrite()) {
                System.out.println("GREŠKA: Nemate pravo pisanja u odredišni direktorij.");
                return;
            }
            //Provjera da li nam odredišni direktorij postoji
            if (kopiranaDirektorij != null && !kopiranaDirektorij.exists()) {
                System.out.println("GREŠKA: Odredišni direktorij ne postoji.");
                return;
            }

            //  Kopiranje datoteke
            boolean uspješnoKopiranje = kopirajDatoteku(originalnaDatoteka, kopiranaDatoteka);

            if (!uspješnoKopiranje) {
                System.out.println("Došlo je do pogreške pri kopiranju.");
                return;
            }

            System.out.println("Kopija je uspješno napravljena: " + kopiranaDatoteka.getAbsolutePath());
            // Otvaranje kopirane datoteke u File exploreru
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(kopiranaDatoteka);
                    System.out.println("Kopirana datoteka je otvorena.");
                } else {
                    System.out.println("Desktop API nije podržan na ovom sustavu.");
                }
            } catch (IOException e) {
                System.out.println("Ne mogu otvoriti kopiranu datoteku u File Exploreru: " + e.getMessage());
            }
            //  Brisanje datoteke
            System.out.print("Želite li obrisati kopiranu datoteku? (da/ne): ");
            String odgovorBrisanje = scanner.nextLine();

            if (odgovorBrisanje.equalsIgnoreCase("da")) {
                if (kopiranaDatoteka.delete()) {
                    System.out.println("Datoteka uspješno obrisana.");
                } else {
                    System.out.println("Datoteku nije moguće obrisati.");
                }
            }

        } finally {
            scanner.close();
        }
    }

    public static boolean kopirajDatoteku(File ulazna, File izlazna) {
        try (FileInputStream fis = new FileInputStream(ulazna);
             FileOutputStream fos = new FileOutputStream(izlazna)) {

            int c;
            while ((c = fis.read()) != -1) {
                fos.write(c); // zapisujemo byte-po-byte
            }
            return true;

        } catch (IOException e) {
            System.out.println("Greška pri kopiranju: " + e.getMessage());
            return false;
        }
    }
}
