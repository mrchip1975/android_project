package it.learnathome.indovinalaparola.data;

import java.io.Serializable;

public class RemoteRecord implements Serializable {
    private int id_record,attempts;
    private String player;
    private String elapsed_time;
    private String word;
    private String day;

    public String getWord() {
        return word;
    }

    public RemoteRecord setWord(String word) {
        this.word = word;
        return this;
    }



    public int getId_record() {
        return id_record;
    }

    public RemoteRecord setId_record(int id_record) {
        this.id_record = id_record;
        return this;
    }

    public int getAttempts() {
        return attempts;
    }

    public RemoteRecord setAttempts(int attempts) {
        this.attempts = attempts;
        return this;
    }

    public String getPlayer() {
        return player;
    }

    public RemoteRecord setPlayer(String player) {
        this.player = player;
        return this;
    }

    public String getElapsed_time() {
        return elapsed_time;
    }

    public RemoteRecord setElapsed_time(String elapsed_time) {
        this.elapsed_time = elapsed_time;
        return this;
    }

    public String getDay() {
        return day;
    }

    public RemoteRecord setDay(String day) {
        this.day = day;
        return this;
    }
}
