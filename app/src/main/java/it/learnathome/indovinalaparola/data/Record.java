package it.learnathome.indovinalaparola.data;

import android.content.ContentValues;

import java.time.LocalDate;
import java.time.LocalTime;

public class Record {
    private final static String TEMPLATE = "Nome Giocatore:%s - Parola: %s%nTentativi:%d%n" +
            "Data:%s - Tempo Impiegato:%s";
    private int id,attempts;
    private String name,word;
    private LocalDate date=LocalDate.now();
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public Record setTime(LocalTime time) {
        this.time = time;
        return this;
    }

    public int getId() {
        return id;
    }

    public Record setId(int id) {
        this.id = id;
        return this;
    }

    public int getAttempt() {
        return attempts;
    }

    public Record setAttempts(int attempts) {
        this.attempts = attempts;
        return this;
    }

    public String getName() {
        return name;
    }

    public Record setName(String name) {
        this.name = name;
        return this;
    }

    public String getWord() {
        return word;
    }

    public Record setWord(String word) {
        this.word = word;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Record setDate(LocalDate date) {
        this.date = date;
        return this;
    }
    public ContentValues convertToContent() {
        ContentValues values = new ContentValues();
        values.put("name",this.name);
        values.put("word",this.word);
        values.put("attempts",this.attempts);
        values.put("date",this.date.toEpochDay());
        values.put("time",this.date.toEpochDay());
        return values;
    }
    @Override
    public String toString() {
        return String.format(TEMPLATE,this.name,this.word,this.attempts,this.date.toString(),this.time.toString());
    }

}
