package example.micronaut.chess.model.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Player {
    WHITE("w"),
    BLACK("b");

    private final String color;

    Player(String color) {
        this.color = color;
    }

    @JsonValue
    public String toString() {
        return color;
    }
}