package it.learnathome.indovinalaparola.data;

import android.content.ContentValues;

import java.time.LocalDate;

public class Record {
    private int id,attempts;
    private String name,word;
    private LocalDate date=LocalDate.now();

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
        return values;
    }
}
