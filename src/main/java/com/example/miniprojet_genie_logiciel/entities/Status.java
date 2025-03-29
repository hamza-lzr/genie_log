package com.example.miniprojet_genie_logiciel.entities;

import java.util.Arrays;

public enum Status {
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Convertit une String en Status (utile pour les requêtes API)
    public static Status fromDisplayName(String displayName) {
        return Arrays.stream(Status.values())
                .filter(s -> s.getDisplayName().equalsIgnoreCase(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Statut invalide : " + displayName));
    }
}