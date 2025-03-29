package com.example.miniprojet_genie_logiciel.entities;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum Priority {
    MUST_HAVE("Must Have", 4),
    SHOULD_HAVE("Should Have", 3),
    COULD_HAVE("Could Have", 2),
    WONT_HAVE("Won't Have", 1);

    // Getters
    private final String label;
    private final int weight;

    // Constructor
    Priority(String label, int weight) {
        this.label = label;
        this.weight = weight;
    }

    // Optionnel: Convertir une String en enum
    public static Priority fromLabel(String label) {
        return Arrays.stream(Priority.values())
                .filter(p -> p.getLabel().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Priorité invalide : " + label));
    }
}