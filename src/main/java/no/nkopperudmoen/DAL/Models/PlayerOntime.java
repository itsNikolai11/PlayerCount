package no.nkopperudmoen.DAL.Models;

import no.nkopperudmoen.UTIL.MessagePreProcessor;

public class PlayerOntime {
    private int ontime;
    private String name;

    public PlayerOntime(int ontime, String name) {
        this.ontime = ontime;
        this.name = name;
    }

    public int getOntime() {
        return ontime;
    }

    public void setOntime(int ontime) {
        this.ontime = ontime;
    }
    public String getOntimeString(){
        return MessagePreProcessor.formatTimeFromMinutes(ontime);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
