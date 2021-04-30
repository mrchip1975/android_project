package it.learnathome.indovinalaparola.utils.game;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class Helper{
    public static final int FIFTY_FIFTY_HELP_COST = 30;
    public static final int REVERSE_HELP_COST = 45;
    public static final int POLLING_HELP_COST = 25;
    private final static Random gen = new Random();
    private String[] suggests;
    public String reverse(String word) {
        return new StringBuilder(word).reverse().toString();
    }
    public String fiftyfifty(String word) {
        int counter = word.length()/2,index=0;
        List<Integer> alreadyUsed = new ArrayList<>();
        StringBuilder buffer = new StringBuilder(word.replaceAll("\\.","-"));
        while(counter>0) {
            index = gen.nextInt(word.length());
            if(!alreadyUsed.contains(index)) {
                buffer.replace(index,index+1,String.valueOf(word.charAt(index)));
                counter--;
            }
        }
        return buffer.toString();
    }
    public Map<String,Float> polling(String myCurrentAttempt,String word) {
        //paperon de paperoni
        //paper-- d- pap-----
        //paper% d% pap%
        //paper__ d_ pap_____
        myCurrentAttempt = myCurrentAttempt.replaceAll("(\\-)+","%");
        Map<String,Float> percent = new HashMap<>();
        try {
            Suggester suggester = new Suggester(myCurrentAttempt);
            suggester.start();
            suggester.join();
            percent = buildValues(this.suggests,word);
        } catch (Exception ex) {

        }
        return percent;
    }
    private HashMap<String,Float> buildValues(String[] words,String secret) {
        HashMap<String,Float> values = new HashMap<>();
        float counter=0;
        for (String word:words) {
            for (int i = 0; i < Math.min(word.length(),secret.length()); i++) {
                if(word.charAt(i)==secret.charAt(i)) {
                    counter++;
                }
            }
            values.put(word,counter/secret.length());
            counter=0;
        }
        return values;
    }
    private class Suggester extends Thread {
        private final static String SCRIPT_URL = "https://corsipca.altervista.org/indovina_la_parola/fiftyfifty.php";
        private String myWord;
        public Suggester(String myWord) {
            this.myWord = myWord;
        }
        @Override
        public void run() {
            try {
                URL url = new URL(SCRIPT_URL);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();
                try(PrintWriter out = new PrintWriter(connection.getOutputStream())) {
                    out.println("criteria="+this.myWord);
                    out.flush();
                }
                if(connection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                    try(Scanner in = new Scanner(connection.getInputStream())) {
                        Gson parser = new Gson();
                        suggests = parser.fromJson(in.nextLine(),String[].class);
                    }
                 } else throw new IOException("Errore download suggerimenti");
            } catch (IOException ex) {
                Log.e("Suggester",ex.getMessage());
            }
        }
    }
 }
