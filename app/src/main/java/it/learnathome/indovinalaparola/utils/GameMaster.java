package it.learnathome.indovinalaparola.utils;

import android.content.Context;

import java.util.Random;

public class GameMaster {
    private static String secretWord;
    private static Random selector = new Random();
    private GameMaster() {}
    public static String startGame(Context ctx) {
        GameMaster.secretWord = WordSelector.getWord(ctx);
        return shuffleWord(GameMaster.secretWord);
    }
    private static String shuffleWord(String word) {
        char shuffledText[] = new char[word.length()*2];
        for (char ch:word.toCharArray()) {
            shuffledText[getEmptyLocation(shuffledText)] = ch;
        }
        for (int i = 0; i < shuffledText.length; i++) {
            if(shuffledText[i]=='\0')
                shuffledText[i] = (char)('a'+selector.nextInt(26));
        }
        return new String(shuffledText);
    }
    private static int getEmptyLocation(char vector[]) {
        int position = selector.nextInt(vector.length);
        while(vector[position]!='\0') {
            position = selector.nextInt(vector.length);
        }
        return position;
    }
}
