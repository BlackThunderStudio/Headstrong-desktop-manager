package com.headstrongpro.desktop.model;

import java.util.ArrayList;

/**
 * Created by 1062085 on 18-Apr-17.
 */
public class Text extends Resource{
    public String content;

    public Text(int id, String name, String description, String url, ArrayList<Achievement> achievements, Session session, String content) {
        super(id, name, description, url, achievements, session);
        this.content = content;
    }

    public Text(String name, String description, String url, ArrayList<Achievement> achievements, Session session, String content) {
        super(name, description, url, achievements, session);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
