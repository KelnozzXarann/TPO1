package zad2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

public class gui {
    private JComboBox<String> cbCountry;
    private JPanel mainPanel;
    private JTextField tfCurrOrig;
    private JTextField tfCurrChosen;
    private JTextField inputTown;
    private JLabel CurrOrig;
    private JLabel CurrChosen;
    private JLabel nbpVal;
    private JComboBox<String> cbCurrency;
    private JButton checkTheWeatherButton;
    private JButton openWikiButton;
    private JTextArea taWeather;
    private double rate;

    public gui(Service s) {
        JFrame jFrame=new JFrame("App");
        jFrame.setContentPane(mainPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        s.getCountries().forEach((k,v)->cbCountry.addItem(k));
        Currency.getAvailableCurrencies().forEach(k->cbCurrency.addItem(k.getCurrencyCode()));
        cbCountry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.setCountry(cbCountry.getSelectedItem().toString());
                CurrOrig.setText(s.getCurr().getCurrencyCode()!=null?s.getCurr().getCurrencyCode():"None");
                nbpVal.setText(s.getNBPLabel());
            }
        });
        cbCurrency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrChosen.setText(cbCurrency.getSelectedItem().toString());
            }
        });
        openWikiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputTown.getText()!=null){
                    Web.setUrl(inputTown.getText());
                    Web.start();
                }
            }
        });
        checkTheWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputTown.getText()!=null){
                    s.getWeather(inputTown.getText());
                    StringBuilder text=new StringBuilder();
                    s.getWeatherMap().forEach((k,v)->text.append(k).append(": ").append(v).append("\r\n"));
                    taWeather.setText(text.toString());
                }
            }
        });
        CurrOrig.setText(s.getCurr().getCurrencyCode());
        CurrChosen.setText(cbCurrency.getSelectedItem().toString());

    }
}
