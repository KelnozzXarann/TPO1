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
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {
    private String country;
    public Map<String, Locale> countries;
    private Currency curr;
    private Map<String,String> WeatherMap;
    private String web;

    public Service(String country) {
        this.country=country;
        countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("EN", iso);
            countries.put(l.getDisplayCountry(), l);
        }
        curr=Currency.getInstance(countries.get(this.country));
    }

    public void setCountry(String country) {
        this.country = country;
        curr=Currency.getInstance(countries.get(this.country));
    }

    public String getWeather(String town) {

        String url="http://api.openweathermap.org/data/2.5/weather";
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("appid","9fcc3785df79775aa886fb879bede0a7");
        map.put("q",town);
        String json=connect(createQueryString(url,map));
        WeatherMap= new HashMap<>();
        WeatherMap.put("main",null);
        WeatherMap.put("description",null);
        WeatherMap.put("temp",null);
        WeatherMap.put("pressure",null);
        WeatherMap.put("humidity",null);
        WeatherMap.put("temp_min",null);
        WeatherMap.put("temp_max",null);
        WeatherMap.put("sunrise",null);
        WeatherMap.put("sunset",null);
        WeatherMap.forEach((k,v)->WeatherMap.replace(k,getJsonValue(json,k)));

        return json;
    }

    private String getJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\""+key+"\":\"?((?:\\d+\\.?\\d*)|(?:\\w+\\s?\\w*))");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getNBPValue(String json) {
        Pattern pattern = Pattern.compile("<td class=\"bgt\\d right\">(\\d+) ("+curr.getCurrencyCode()+")<\\/td>\\s+<td class=\"bgt\\d right\">(\\d+,?\\d*)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1)+" "+ matcher.group(2)+": "+matcher.group(3);
        }
        return "Nie znaleziono";
    }
    public Double getRateFor(String currency) {

        String url="http://data.fixer.io/api/latest";
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("access_key","9df2e27eb0a520ec531b377196c8826c");
        map.put("symbols",curr.getCurrencyCode()+","+currency);
        String json=connect(createQueryString(url,map));
        HashMap<String,Double> result=new HashMap<String, Double>();
        result.put(curr.getCurrencyCode(),null);
        result.put(currency,null);
        result.forEach((k,v)->result.replace(k,Double.parseDouble(getJsonValue(json,k))));
        double rate=result.get(currency)/result.get(curr.getCurrencyCode());
        System.out.println("1 "+curr.getCurrencyCode()+" = "+rate+" "+currency);

        return rate;
    }

    public Double getNBPRate() {

        if (web != null && !web.isEmpty()) {
        }else {
            web=connect("http://www.nbp.pl/kursy/kursya.html");
            web+=connect("http://www.nbp.pl/kursy/kursyb.html");
        }
        System.out.println(getNBPValue(web));
        return null;
    }
    public String getNBPLabel() {
        if (web != null && !web.isEmpty()) {
        }else {
            web=connect("http://www.nbp.pl/kursy/kursya.html");
            web+=connect("http://www.nbp.pl/kursy/kursyb.html");
        }
        return getNBPValue(web);
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

    private String createQueryString(String url, Map<String,String> params){
        StringBuilder result=new StringBuilder();
        result.append(url).append("?");
        params.forEach((key,value) -> result.append(key+"="+value+"&"));
        result.deleteCharAt(result.length()-1);
        System.out.println(result);
        return result.toString();
    }

    public Map<String, Locale> getCountries() {
        return countries;
    }

    public Currency getCurr() {
        return curr;
    }

    public Map<String, String> getWeatherMap() {
        return WeatherMap;
    }
}
