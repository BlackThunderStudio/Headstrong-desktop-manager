package com.headstrongpro.desktop.model;

import java.util.ArrayList;

/**
 * Created by 1062085 on 18-Apr-17.
 */
public class Multimedia extends Resource {
    private double duration;

    public Multimedia(int id, String name, String description, String url, ArrayList<Achievement> achievements, Session session, double duration) {
        super(id, name, description, url, achievements, session);
        this.duration = duration;
    }

    public Multimedia(String name, String description, String url, ArrayList<Achievement> achievements, Session session, double duration) {
        super(name, description, url, achievements, session);
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
