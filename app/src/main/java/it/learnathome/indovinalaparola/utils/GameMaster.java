package it.learnathome.indovinalaparola.utils;

import android.content.Context;

import java.util.Random;
import java.util.StringJoiner;

public class GameMaster {
    private static String secretWord;
    private static Random selector = new Random();
    private GameMaster() {}
    public static String startGame(Context ctx) {
        GameMaster.secretWord = WordSelector.getWord(ctx);
        return shuffleWord(GameMaster.secretWord);
    }
    public static String buildCryptedWord() {
        String cryptedText="";
        for (int i = 0; i < GameMaster.secretWord.length(); i++) {
            cryptedText+="-";
        }
        return cryptedText;
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
    public static String checkAttempt(String gamerAttempt) {
        StringJoiner buffer = new StringJoiner("");
        for (int i = 0; i < GameMaster.secretWord.length(); i++) {
            buffer.add(i<gamerAttempt.length() && gamerAttempt.charAt(i)==GameMaster.secretWord.charAt(i)?
               String.valueOf(GameMaster.secretWord.charAt(i)):"-");
        }
        return buffer.toString();
    }
}
