package example.micronaut.chess.api.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

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

    public static Player getValue(String colorValue){
        return Stream.of(values()).filter((Player value) -> value.getColor().equals(colorValue)).findFirst().orElse(Player.WHITE);
    }
}