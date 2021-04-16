package it.learnathome.indovinalaparola.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
    public static String updateShuffledText(String shuffledText) {
        if(shuffledText.length()>GameMaster.secretWord.length()) {
            List<Character> chars;
            do {
                chars = new ArrayList<>();
                for (char ch:shuffledText.toCharArray()) {
                    chars.add(Character.valueOf(ch));
                }
                chars.remove(Character.valueOf(shuffledText.charAt(selector.nextInt(shuffledText.length()))));
                if(isValid(chars))
                    break;
            } while(true);

            return chars.toString().replace("[","").replace("]","").replaceAll(", ","");
        } else return shuffledText;
    }
    private static boolean isValid(List<Character> availableChars) {
        List<Character> copy = new ArrayList<>(availableChars);
        for (char ch: GameMaster.secretWord.toCharArray()) {
            if(!copy.remove(Character.valueOf(ch)))
                return false;
        }
        return true;
    }
    public static boolean youWin(String playerAttempt) {
        return GameMaster.secretWord.equals(playerAttempt);
    }
    public static String getSecretWord() {
        return secretWord;
    }
}
