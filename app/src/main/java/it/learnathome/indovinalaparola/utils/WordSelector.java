package it.learnathome.indovinalaparola.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import it.learnathome.indovinalaparola.R;

 class WordSelector {
    private final static Random selector = new Random();
    private String[] words;
    private static WordSelector instance = null;
    private WordSelector(){
        new WordsDownloader().start();
    }
    public static WordSelector getInstance() {
        if(instance==null) {
            instance = new WordSelector();
        }
        return instance;
    }
    public String getWord() {
        return words[selector.nextInt(words.length)];
    }
    private class WordsDownloader extends Thread {
        private final static String SCRIPT_URL = "https://corsipca.altervista.org/indovina_la_parola/download_word.php";
        @Override
        public void run() {
            try{
                URL url = new URL(SCRIPT_URL);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.connect();
                Scanner lettore = new Scanner(connection.getInputStream());
                String message = lettore.nextLine();
                Gson parser = new Gson();
                words = parser.fromJson(message,String[].class);
                Log.d("vettore", Arrays.toString(words));
            } catch(IOException ex) {
                Log.e("WordsDownloader",ex.getMessage());
            }
        }
    }
}
