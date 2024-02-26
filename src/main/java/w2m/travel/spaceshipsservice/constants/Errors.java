package w2m.travel.spaceshipsservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Errors {
    ERROR_BAD_REQUEST("400100", "Error -> the fields cannot be null or empty: %s"),

    ERROR_DATABASE_SPACESHIP_NOT_FOUND("404100", "Error -> spaceship not found with id: [%s]"),
    ERROR_DATABASE_SPACESHIPS_NOT_CONTAINING_NAME("404101", "Error -> there are no spaceships containing the name: [%s]"),
    ERROR_DATABASE_SPACESHIPS_NOT_FOUND("404102", "Error -> there are no spaceships"),
    ERROR_DATABASE_SERIESS_NOT_FOUND("404103", "Error -> there are no series"),

    ERROR_GENERAL("900000", "Error General -> [%s] %s");

    private final String code;
    private final String message;

}