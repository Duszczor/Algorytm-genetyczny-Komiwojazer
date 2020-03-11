/**
 * Created by Admin on 22.02.2018.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class OdczytPliku {

    static int LICZBA_MIEJSC;
    static int[][] TABLICA_PRZEPLYWU;
    static int[][] TABLICA_ODLEGLOSCI;
    static int LICZBA_PETLI_ALGORYTMU = 100;
    static String NAZWA_PLIKU = "20.txt";
    static int LICZBA_POPULACJI = 100;
    static int ROZMNAZANIE_PIERWSZY_PUNKT = 3;
    static int ROZMNAZANIE_DRUGI_PUNKT = 6;
    static double SZANSA_MUTACJI = 1;
    static int LICZBA_POKOLEN = 100;
    static int TOUR = 5;

    static LineChartEx2 line = new LineChartEx2();
    static XYSeries seriesNajgorsza = new XYSeries("Najgorsza");
    static XYSeries seriesSrednia = new XYSeries("Srednia");
    static XYSeries seriesNajlepsza = new XYSeries("Najlepsza");

    public static void main(String[] args) throws FileNotFoundException {
        algorytmLosowy();
        //algorytmGreedy();
        //algorytmGenetyczny();


    }

    public static void wczytywaniePliku() throws FileNotFoundException {

        File file = new File(NAZWA_PLIKU);
        Scanner in = new Scanner(file);


        LICZBA_MIEJSC = in.nextInt();
        TABLICA_ODLEGLOSCI = new int[LICZBA_MIEJSC][LICZBA_MIEJSC];
        TABLICA_PRZEPLYWU = new int[LICZBA_MIEJSC][LICZBA_MIEJSC];

        for (int i = 0; i < LICZBA_MIEJSC; i++) {
            for (int j = 0; j < LICZBA_MIEJSC; j++) {
                TABLICA_PRZEPLYWU[i][j] = in.nextInt();
            }
        }

        for (int i = 0; i < LICZBA_MIEJSC; i++) {
            for (int j = 0; j < LICZBA_MIEJSC; j++) {
                TABLICA_ODLEGLOSCI[i][j] = in.nextInt();
            }
        }

        System.out.println("Liczba miejsc = " + LICZBA_MIEJSC);

        System.out.println("Tablica Przeplywu: ");
        wyswietlanieTablicyInt2D(TABLICA_PRZEPLYWU);
        System.out.println();
        System.out.println("Tablica Odleglosci: ");
        wyswietlanieTablicyInt2D(TABLICA_ODLEGLOSCI);
    }

    public static void zamienElementListy(int index, int value, List<Integer> lista) {
        lista.remove(index);
        lista.add(index, value);
    }

    public static double sredniaWartoscFunkcjiListy(List<ParaListaWartosc> lista) {

        int suma = 0;
        for (int i = 0; i < LICZBA_POPULACJI; i++) {
            suma += lista.get(i).wartosc;
        }
        return suma / LICZBA_POPULACJI;
    }

    public static void algorytmLosowy() throws FileNotFoundException {

        wczytywaniePliku();

        List<Integer> listaWyniku = new ArrayList<Integer>();
        List<Integer> listaProb = new ArrayList<Integer>();


        uzupelnianieListyLosowymiWartosciamiBezPowtorzen(listaWyniku, LICZBA_MIEJSC); //Wylosowana bierzaca droga


        int aktualnaWartoscFunkcjiCelu = funkcjaCelu(LICZBA_MIEJSC, listaWyniku); //Liczenie funkcji celu


        for (int k = 0; k < LICZBA_PETLI_ALGORYTMU; k++) {
            uzupelnianieListyLosowymiWartosciamiBezPowtorzen(listaProb, LICZBA_MIEJSC); //Wylosowana nowa droga

            int nowaWartoscFunkcjiCelu = funkcjaCelu(LICZBA_MIEJSC, listaProb);
            seriesNajgorsza.add(k + 1, nowaWartoscFunkcjiCelu);

            if (aktualnaWartoscFunkcjiCelu > nowaWartoscFunkcjiCelu) {
                listaWyniku = listaProb;
                aktualnaWartoscFunkcjiCelu = nowaWartoscFunkcjiCelu;
            }
            seriesSrednia.add(k + 1, aktualnaWartoscFunkcjiCelu);
        }


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesSrednia);
        dataset.addSeries(seriesNajgorsza);

        XYDataset datas = dataset;
        JFreeChart chart = line.createChart(dataset, "Numer Pokolenia", "Wartosc funkcji", "Tytul");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        line.add(chartPanel);

        line.pack();
        line.setTitle("Line chart");
        line.setLocationRelativeTo(null);
        line.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(() -> {
            line.setVisible(true);
        });


        System.out.println("Dla N = " + LICZBA_MIEJSC);
        System.out.println("Lista wyniku: " + listaWyniku);
        System.out.println("Wartosc funkcji celu: " + aktualnaWartoscFunkcjiCelu);

    }

    public static int funkcjaCelu(int n, List<Integer> lista) {
        int aktualnaWartoscFunkcjiCelu = 0; //Liczenie funkcji celu
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aktualnaWartoscFunkcjiCelu += TABLICA_PRZEPLYWU[i][j] * TABLICA_ODLEGLOSCI[lista.get(i) - 1][lista.get(j) - 1];
            }
        }
        return aktualnaWartoscFunkcjiCelu;
    }

    public static void uzupelnianieListyLosowymiWartosciamiBezPowtorzen(List<Integer> lista, int zakres) {
        lista.clear();
        for (int i = 0; i < zakres; i++) {
            lista.add(i + 1);
        }
        Collections.shuffle(lista);
    }

    public static void wyswietlanieTablicyInt2D(int tab[][]) {
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab.length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void algorytmGreedy() throws FileNotFoundException {

        wczytywaniePliku();

        List<Integer> listaWyniku = new ArrayList<Integer>();
        List<Integer> listaProb = new ArrayList<Integer>();

        int aktualnaLiczbaFabryk = 2;
        int aktualnaWartoscFunkcjiCelu;
        int nowaWartoscFunkcjiCelu;
        boolean znalazl = false;

        for (int i = 0; i < aktualnaLiczbaFabryk; i++) { //Pierwszy przyklad listy dla n=2
            listaWyniku.add(i + 1);
        }
        aktualnaWartoscFunkcjiCelu = funkcjaCelu(aktualnaLiczbaFabryk, listaWyniku);


        for (int i = 0; i < LICZBA_MIEJSC; i++) {
            for (int j = 0; j < LICZBA_MIEJSC; j++) {
                if (i != j) {
                    listaProb.clear();
                    listaProb.add(i + 1);
                    listaProb.add(j + 1);
                    nowaWartoscFunkcjiCelu = funkcjaCelu(aktualnaLiczbaFabryk, listaProb);
                    if (aktualnaWartoscFunkcjiCelu > nowaWartoscFunkcjiCelu) {
                        listaWyniku.clear();
                        listaWyniku.addAll(listaProb);
                        aktualnaWartoscFunkcjiCelu = nowaWartoscFunkcjiCelu;
                        System.out.println("Iteracyjna wartosc funkcji: " + aktualnaWartoscFunkcjiCelu);
                    }
                }
            }

        }
        seriesSrednia.add(2, aktualnaWartoscFunkcjiCelu);


        for (aktualnaLiczbaFabryk = 3; aktualnaLiczbaFabryk <= LICZBA_MIEJSC; aktualnaLiczbaFabryk++) {

            for (int n = 0; n < LICZBA_MIEJSC && !znalazl; n++) { //nowa wartosc funkcji celu dla wiekszej liczby fabryk
                if (!listaWyniku.contains(n + 1)) {
                    listaWyniku.add(n + 1);
                    aktualnaWartoscFunkcjiCelu = funkcjaCelu(aktualnaLiczbaFabryk, listaWyniku);

                    znalazl = true;

                }
            }

            znalazl = false;
            for (int i = 0; i < LICZBA_MIEJSC; i++) {
                if (!listaWyniku.contains(i + 1)) {
                    listaProb.clear();
                    listaProb.addAll(listaWyniku);
                    listaProb.remove(listaProb.size() - 1);
                    listaProb.add(i + 1);
                    nowaWartoscFunkcjiCelu = funkcjaCelu(aktualnaLiczbaFabryk, listaProb);
                    if (aktualnaWartoscFunkcjiCelu > nowaWartoscFunkcjiCelu) {
                        listaWyniku.clear();
                        listaWyniku.addAll(listaProb);
                        aktualnaWartoscFunkcjiCelu = nowaWartoscFunkcjiCelu;

                    }
                }
            }
            seriesSrednia.add(aktualnaLiczbaFabryk, aktualnaWartoscFunkcjiCelu);
            System.out.println(listaWyniku);
            System.out.println(aktualnaLiczbaFabryk);
        }


        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesSrednia);

        XYDataset datas = dataset;
        JFreeChart chart = line.createChart(dataset, "Numer Pokolenia", "Wartosc funkcji", "Tytul");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        line.add(chartPanel);

        line.pack();
        line.setTitle("Line chart");
        line.setLocationRelativeTo(null);
        line.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(() -> {
            line.setVisible(true);
        });


        System.out.println("Dla N = " + LICZBA_MIEJSC);
        System.out.println("Lista wyniku: " + listaWyniku);
        System.out.println("Wynik: " + aktualnaWartoscFunkcjiCelu);
    }

    public static void naprawListe(List<Integer> lista) {
        List<Integer> brakujaceElementy = new ArrayList<Integer>();
        brakujaceElementy.clear();
        for (int i = 1; i <= LICZBA_MIEJSC; i++) {
            if (!lista.contains(i)) {
                brakujaceElementy.add(i);   // WARTOSCI ELEMENTOW BRAKUJACYCH
            }
        }


        if (brakujaceElementy.size() > 0) {
            for (int j = 0; j < LICZBA_MIEJSC; j++) {
                int liczbaWystapien = 0;
                List<Integer> listaIndeksow = new ArrayList<Integer>();
                listaIndeksow.clear();
                for (int i = 0; i < LICZBA_MIEJSC; i++) {
                    if (lista.get(i) == j + 1) {   // WARTOSC j+1
                        liczbaWystapien += 1;
                        listaIndeksow.add(i);
                    }
                }
                if (liczbaWystapien > 1) {
                    while (listaIndeksow.size() > 1) {
                        lista.remove(listaIndeksow.get(0));
                        lista.add(listaIndeksow.get(0), brakujaceElementy.get(0));
                        brakujaceElementy.remove(0);
                        listaIndeksow.remove(0);
                    }
                }
            }
        }
    }

    public static void wylosujNowaListe(List<Integer> lista) {

        uzupelnianieListyLosowymiWartosciamiBezPowtorzen(lista, LICZBA_MIEJSC);
    }

    public static void czyListaJestDobra(List<Integer> lista) {
        for (int i = 0; i < LICZBA_MIEJSC; i++) {
            if (!lista.contains(i + 1)) {
                System.out.println("BLAD !!!! BRAKUJE ELEMENTU " + (i + 1));
                System.out.println(lista);
            }
        }
    }

    public static boolean czyListaJestDobraBoolean(List<Integer> lista) {
        for (int i = 0; i < LICZBA_MIEJSC; i++) {
            if (!lista.contains(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static List<Trojka> utworzListeTrojek(List<ParaListaWartosc> lista) {

        int wartoscNajgorszegoOsobnika = 0;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).wartosc > wartoscNajgorszegoOsobnika) {
                wartoscNajgorszegoOsobnika = lista.get(i).wartosc;
            }
        }

        int sumaOsobnikow = 0;
        for (int i = 0; i < lista.size(); i++) {
            sumaOsobnikow += lista.get(i).wartosc;
        }


        List<Trojka> wynik = new ArrayList<Trojka>();
        int aktualnaGranica = 0;

        for (int i = 0; i < lista.size(); i++) {
            Trojka trojka = new Trojka();
            trojka.lista.addAll(lista.get(i).lista);
            trojka.wartoscMin = aktualnaGranica;
            trojka.wartoscMax = aktualnaGranica + Math.abs(((wartoscNajgorszegoOsobnika - lista.get(i).wartosc + 1)));
            aktualnaGranica += Math.abs((wartoscNajgorszegoOsobnika - lista.get(i).wartosc + 1) + 1);
            wynik.add(trojka);
        }

        return wynik;
    }

    public static List<ParaListaWartosc> selekcjaRuletka(List<ParaListaWartosc> lista) {
        List<Trojka> listaTrojek = utworzListeTrojek(lista);
        List<ParaListaWartosc> wynik = new ArrayList<ParaListaWartosc>();
        Random generator = new Random();

        for (int i = 0; i < listaTrojek.size(); i++) {
            listaTrojek.get(i).wyswietl();
        }

        int granicaLosowania = listaTrojek.get(listaTrojek.size() - 1).wartoscMax;

        for (int i = 0; i <= LICZBA_POPULACJI / 2; i++) {
            int wylosowanaLiczba = generator.nextInt(granicaLosowania + 1);

            boolean petla = true;
            for (int j = 0; j < listaTrojek.size() && petla; j++) {         //Losowanie populacji
                if (wylosowanaLiczba <= listaTrojek.get(j).wartoscMax && wylosowanaLiczba >= listaTrojek.get(j).wartoscMin) {
                    wynik.add(new ParaListaWartosc(listaTrojek.get(j).lista, funkcjaCelu(LICZBA_MIEJSC, listaTrojek.get(j).lista)));
                    petla = false;
                }
            }
        }

        int najmniejsza = lista.get(0).wartosc;
        int indeks = 0;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).wartosc < najmniejsza) {
                najmniejsza = lista.get(i).wartosc;
                indeks = i;
            }
        }
        wynik.remove(0);
        wynik.add(lista.get(indeks));
        return wynik;

    }

    public static List<ParaListaWartosc> selekcjaTurniejowa(List<ParaListaWartosc> lista) {

        List<ParaListaWartosc> wynik = new ArrayList<ParaListaWartosc>();

        int najmniejsza = lista.get(0).wartosc;
        int indeks = 0;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).wartosc < najmniejsza) {
                najmniejsza = lista.get(i).wartosc;
                indeks = i;
            }
        }
        wynik.add(lista.get(indeks));

        for (int i = 1; i <= LICZBA_POPULACJI / 2; i++) {
            Random generator = new Random();
            int wylosowanyIndexJeden;
            int lepszyIndeks = generator.nextInt(LICZBA_POPULACJI - i);

            for (int j = 0; j < TOUR; j++) {

                wylosowanyIndexJeden = generator.nextInt(LICZBA_POPULACJI - i);

                if (lista.get(wylosowanyIndexJeden).wartosc < lista.get(lepszyIndeks).wartosc) {
                    lepszyIndeks = wylosowanyIndexJeden;
                }
            }

            wynik.add(lista.get(lepszyIndeks));
            lista.remove(lepszyIndeks);

        }

        return wynik;
    }

    public static void algorytmGenetyczny() throws FileNotFoundException {

        wczytywaniePliku();

        List<ParaListaWartosc> listaPar = new ArrayList<ParaListaWartosc>();

        for (int i = 0; i < LICZBA_POPULACJI; i++) {        //// Uzupelnianie populacji

            List<Integer> nowaLista = new ArrayList<Integer>();
            uzupelnianieListyLosowymiWartosciamiBezPowtorzen(nowaLista, LICZBA_MIEJSC);

            boolean sprawdzanieCzyJestLista = true;
            for (int j = 0; j < listaPar.size() && sprawdzanieCzyJestLista; j++) {
                if (listaPar.get(j).czyTaSamaLista(nowaLista)) {
                    i--;
                    sprawdzanieCzyJestLista = false;
                }
            }

            if (sprawdzanieCzyJestLista) {
                listaPar.add(new ParaListaWartosc(nowaLista, funkcjaCelu(LICZBA_MIEJSC, nowaLista)));
            }
        }


        for (int q = 0; q < LICZBA_POKOLEN; q++) {


            List<ParaListaWartosc> najlepszaPolowa_Lista = new ArrayList<ParaListaWartosc>();
            najlepszaPolowa_Lista.clear();
            Random generator = new Random();


            //SELEKCJA -- metoda turniejowa

            najlepszaPolowa_Lista.addAll(selekcjaTurniejowa(listaPar));
            listaPar.clear();
            listaPar.addAll(najlepszaPolowa_Lista);


            //Rozmnazanie -- Zrob dzieci i zostaw troche najlepszych rodzicow

            List<ParaListaWartosc> lista_po_rozmnazaniu = new ArrayList<ParaListaWartosc>();
            lista_po_rozmnazaniu.addAll(najlepszaPolowa_Lista);

            for (int i = 0; lista_po_rozmnazaniu.size() < LICZBA_POPULACJI; i++) {


                List<Integer> pierwszy_rodzic = new ArrayList<Integer>();
                pierwszy_rodzic.clear();
                pierwszy_rodzic.addAll(lista_po_rozmnazaniu.get(generator.nextInt(LICZBA_POPULACJI / 2)).lista);

                List<Integer> drugi_rodzic = new ArrayList<Integer>();
                drugi_rodzic.clear();
                drugi_rodzic.addAll(lista_po_rozmnazaniu.get(generator.nextInt(LICZBA_POPULACJI / 2)).lista);

                List<Integer> pierwsze_dziecko = new ArrayList<Integer>();
                pierwsze_dziecko.clear();
                List<Integer> drugie_dziecko = new ArrayList<Integer>();
                drugie_dziecko.clear();


                pierwsze_dziecko.addAll(pierwszy_rodzic);
                drugie_dziecko.addAll(drugi_rodzic);


                //Pierwsze dziecko
                List<Integer> zmieniona_czesc_dziecka_pierwszego = new ArrayList<Integer>();
                zmieniona_czesc_dziecka_pierwszego.clear();
                List<Integer> nowa_czesc_dziecka_pierwszego = new ArrayList<Integer>();
                nowa_czesc_dziecka_pierwszego.clear();


                for (int j = ROZMNAZANIE_PIERWSZY_PUNKT; j <= ROZMNAZANIE_DRUGI_PUNKT; j++) { // UZUPELNIENIE NOWEJ I ZMIENIONEJ CZESCI
                    zmieniona_czesc_dziecka_pierwszego.add(pierwsze_dziecko.get(ROZMNAZANIE_PIERWSZY_PUNKT));
                    pierwsze_dziecko.remove(ROZMNAZANIE_PIERWSZY_PUNKT);
                    nowa_czesc_dziecka_pierwszego.add(drugi_rodzic.get(j));
                }

                for (int o = 0; o < (ROZMNAZANIE_DRUGI_PUNKT - ROZMNAZANIE_PIERWSZY_PUNKT + 1); o++) {    // USUWANIE POWTORZEN Z DO ZMIANY ( NIE USUWA )
                    if (zmieniona_czesc_dziecka_pierwszego.contains(nowa_czesc_dziecka_pierwszego.get(o))) {
                        zmieniona_czesc_dziecka_pierwszego.remove(zmieniona_czesc_dziecka_pierwszego.indexOf(nowa_czesc_dziecka_pierwszego.get(o)));
                    }
                }


                int indeksNowejListy = 0;
                for (int o = 0; o < LICZBA_MIEJSC - (ROZMNAZANIE_DRUGI_PUNKT - ROZMNAZANIE_PIERWSZY_PUNKT + 1); o++) {   // NAPRAWA POWTORZEN
                    if (nowa_czesc_dziecka_pierwszego.contains(pierwsze_dziecko.get(o))) {
                        pierwsze_dziecko.remove(o);
                        pierwsze_dziecko.add(o, zmieniona_czesc_dziecka_pierwszego.get(0));
                        zmieniona_czesc_dziecka_pierwszego.remove(0);
                        indeksNowejListy++;
                    }
                }

                for (int w = ROZMNAZANIE_PIERWSZY_PUNKT; w <= ROZMNAZANIE_DRUGI_PUNKT; w++) {  //Wstawianie
                    pierwsze_dziecko.add(w, nowa_czesc_dziecka_pierwszego.get(0));
                    nowa_czesc_dziecka_pierwszego.remove(0);
                }


                //Drugie dziecko

                List<Integer> zmieniona_czesc_dziecka_drugiego = new ArrayList<Integer>();
                zmieniona_czesc_dziecka_drugiego.clear();
                List<Integer> nowa_czesc_dziecka_drugiego = new ArrayList<Integer>();
                nowa_czesc_dziecka_drugiego.clear();


                for (int j = ROZMNAZANIE_PIERWSZY_PUNKT; j <= ROZMNAZANIE_DRUGI_PUNKT; j++) { // UZUPELNIENIE NOWEJ I ZMIENIONEJ CZESCI
                    zmieniona_czesc_dziecka_drugiego.add(drugie_dziecko.get(ROZMNAZANIE_PIERWSZY_PUNKT));
                    drugie_dziecko.remove(ROZMNAZANIE_PIERWSZY_PUNKT);
                    nowa_czesc_dziecka_drugiego.add(pierwszy_rodzic.get(j));
                }

                for (int o = 0; o < (ROZMNAZANIE_DRUGI_PUNKT - ROZMNAZANIE_PIERWSZY_PUNKT + 1); o++) {    // USUWANIE POWTORZEN Z DO ZMIANY
                    if (zmieniona_czesc_dziecka_drugiego.contains(nowa_czesc_dziecka_drugiego.get(o))) {
                        zmieniona_czesc_dziecka_drugiego.remove(zmieniona_czesc_dziecka_drugiego.indexOf(nowa_czesc_dziecka_drugiego.get(o)));
                    }
                }


                indeksNowejListy = 0;
                for (int o = 0; o < LICZBA_MIEJSC - (ROZMNAZANIE_DRUGI_PUNKT - ROZMNAZANIE_PIERWSZY_PUNKT + 1); o++) {   // NAPRAWA POWTORZEN
                    if (nowa_czesc_dziecka_drugiego.contains(drugie_dziecko.get(o))) {
                        drugie_dziecko.remove(o);
                        drugie_dziecko.add(o, zmieniona_czesc_dziecka_drugiego.get(0));
                        zmieniona_czesc_dziecka_drugiego.remove(0);
                        indeksNowejListy++;
                    }
                }

                for (int w = ROZMNAZANIE_PIERWSZY_PUNKT; w <= ROZMNAZANIE_DRUGI_PUNKT; w++) {  //Wstawianie
                    drugie_dziecko.add(w, nowa_czesc_dziecka_drugiego.get(0));
                    nowa_czesc_dziecka_drugiego.remove(0);
                }


                lista_po_rozmnazaniu.add(new ParaListaWartosc(pierwsze_dziecko, funkcjaCelu(LICZBA_MIEJSC, pierwsze_dziecko)));
                if (lista_po_rozmnazaniu.size() < LICZBA_POPULACJI) {
                    lista_po_rozmnazaniu.add(new ParaListaWartosc(drugie_dziecko, funkcjaCelu(LICZBA_MIEJSC, drugie_dziecko)));
                }
            }


            listaPar.clear();
            listaPar.addAll(lista_po_rozmnazaniu);


            for (int i = 0; i < listaPar.size(); i++) {
                if (!czyListaJestDobraBoolean(listaPar.get(i).lista)) {
                    System.out.println("PRZED MUTACJA ZLE");
                    System.out.println(listaPar.get(i).lista);
                }
            }


            //Mutacja

            int liczba_do_mutacji = (int) (SZANSA_MUTACJI * LICZBA_POPULACJI);

            int najmniejsza = listaPar.get(0).wartosc;
            int indeks = 0;
            for (int i = 0; i < listaPar.size(); i++) {
                if (listaPar.get(i).wartosc < najmniejsza) {
                    najmniejsza = listaPar.get(i).wartosc;
                    indeks = i;
                }
            }


            for (int i = 0; i < liczba_do_mutacji; i++) {
                int wylosowanyIndex = generator.nextInt(LICZBA_POPULACJI);
                if (wylosowanyIndex != indeks) {

                    List<Integer> do_mutacji = new ArrayList<Integer>();
                    do_mutacji.clear();
                    do_mutacji.addAll(lista_po_rozmnazaniu.get(wylosowanyIndex).lista);

                    int indexDoZmiany = generator.nextInt(LICZBA_MIEJSC);
                    int indexZamieniany = generator.nextInt(LICZBA_MIEJSC);

                    int zapamietanaWartoscDZ = do_mutacji.get(indexDoZmiany);
                    int zapamietanaWartoscZ = do_mutacji.get(indexZamieniany);


                    do_mutacji.remove(indexDoZmiany);
                    do_mutacji.add(indexDoZmiany, zapamietanaWartoscZ);
                    do_mutacji.remove(indexZamieniany);
                    do_mutacji.add(indexZamieniany, zapamietanaWartoscDZ);


                    lista_po_rozmnazaniu.remove(wylosowanyIndex);
                    lista_po_rozmnazaniu.add(wylosowanyIndex, new ParaListaWartosc(do_mutacji, funkcjaCelu(LICZBA_MIEJSC, do_mutacji)));
                }
            }


            listaPar.clear();
            listaPar.addAll(lista_po_rozmnazaniu);


            najmniejsza = listaPar.get(0).wartosc;
            for (int i = 0; i < listaPar.size(); i++) {
                if (listaPar.get(i).wartosc < najmniejsza) {
                    najmniejsza = listaPar.get(i).wartosc;
                }
            }

            System.out.println("ITERACJA NUMER : " + (q + 1));
            System.out.println("Najlepsza w Iteracji: " + najmniejsza);
            seriesNajlepsza.add(q + 1, najmniejsza);


            int najgorsza = listaPar.get(0).wartosc;
            for (int i = 0; i < listaPar.size(); i++) {
                if (listaPar.get(i).wartosc > najgorsza) {
                    najgorsza = listaPar.get(i).wartosc;
                }
            }
            System.out.println("Najgorsza w Iteracji: " + najgorsza);
            seriesNajgorsza.add(q + 1, najgorsza);
            System.out.println("Srednia wartosc w iteracji: " + sredniaWartoscFunkcjiListy(listaPar));
            seriesSrednia.add(q + 1, sredniaWartoscFunkcjiListy(listaPar));
            System.out.println();


        }

        System.out.println("Srednia wartosc: " + sredniaWartoscFunkcjiListy(listaPar));

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesNajlepsza);
        dataset.addSeries(seriesNajgorsza);
        dataset.addSeries(seriesSrednia);

        XYDataset datas = dataset;
        JFreeChart chart = line.createChart(dataset, "Numer Pokolenia", "Wartosc funkcji", "Tytul");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        line.add(chartPanel);

        line.pack();
        line.setTitle("Line chart");
        line.setLocationRelativeTo(null);
        line.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(() -> {
            line.setVisible(true);
        });

    }
}




