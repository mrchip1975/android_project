package it.learnathome.indovinalaparola.utils;

import android.content.Context;

import java.util.Random;

import it.learnathome.indovinalaparola.R;

 class WordSelector {
    private final static Random selector = new Random();
    private WordSelector(){}
    public static String getWord(Context ctx) {
        String[] words = ctx.getResources().getStringArray(R.array.secret_words);
        return words[selector.nextInt(words.length)];
    }
}
