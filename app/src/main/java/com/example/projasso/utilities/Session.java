package com.example.projasso.utilities;

import com.example.projasso.entities.Adherent;

public class Session {

    private static Adherent adherent;
    private static String id;

    public static Adherent getAdherent() {
        return adherent;
    }

    public static void setAdherent(Adherent adherent) {
        Session.adherent = adherent;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Session.id = id;
    }
}
