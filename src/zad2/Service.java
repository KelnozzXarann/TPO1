/**
 *
 *  @author Krupa Karol S14512
 *
 */

package zad2;

//Zadanie: klienci usług sieciowych
//        Napisać aplikację, udostępniającą GUI, w którym po podanu miasta i nazwy kraju pokazywane są:
//
//        Informacje o aktualnej pogodzie w tym mieście.
//        Informacje o kursie wymiany walutu kraju wobec podanej przez uzytkownika waluty.
//        Informacje o kursie NBP złotego wobec tej waluty podanego kraju.
//        Strona wiki z opisem miasta.
//
//        W p. 1 użyć serwisu api.openweathermap.org,
//        w p. 2 - serwisu api.fixer.io,
//        w p. 3 - informacji ze stron NBP: http://www.nbp.pl/kursy/kursya.html i http://www.nbp.pl/kursy/kursyb.html.
//        W p. 4 użyć klasy WebEngine z JavaFX dla wbudowania przeglądarki w aplikację Swingową.
//
//        Program winien zawierać klasę Service z konstruktorem Service(String kraj) i metodami::
//
//        String getWeather(String miasto) - zwraca informację o pogodzie w podanym mieście danego kraju w formacie JSON
//        (to ma być pełna informacja uzyskana z serwisu openweather - po prostu tekst w formacie JSON)),,
//        Double getRateFor(String kod_waluty) - zwraca kurs waluty danego kraju wobec waluty podanej jako argument,
//        Double getNBPRate() - zwraca kurs złotego wobec waluty danego kraju
//
//        Następujące przykładowa klasa  pokazuje możliwe użycie tych metod:
//
//public class Main {
//    public static void main(String[] args) {
//        Service s = new Service("Poland");
//        String weatherJson = s.getWeather("Warsaw");
//        Double rate1 = s.getRateFor("USD");
//        Double rate2 = s.getNBPRate();
//        // ...
//        // część uruchamiająca GUI
//    }
//}
//
//Uwaga 1: zdefiniowanie pokazanych metod w sposób niezalezny od GUI jest obowiązkowe.
//
//Uwaga 2:  W katalogu projektu (np. w podkatalogu lib) nalezy umiescic wykorzystywane JARy (w przeciwnym razie program nie przejdzie kompilacji)
// i skonfigurowac Build Path tak, by wskazania na te JARy byly w Build Path zawarte.


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Service {
    String country;

    public Service(String country) {
        this.country=country;
    }

    public String getWeather(String town) {
        String key="appid=9fcc3785df79775aa886fb879bede0a7";
        String json=connect("http://api.openweathermap.org/data/2.5/weather?q=Warsaw&APPID=9fcc3785df79775aa886fb879bede0a7");

        return null;
    }

    public Double getRateFor(String currency) {

        return null;
    }

    public Double getNBPRate() {
        return null;
    }
    private String connect(String conString){
        URL url = null;
        StringBuffer content=null;
        try {
            url = new URL(conString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println("Content: "+content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
