package pl.ofertownia.api.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DayOfWeek {
    MONDAY("Poniedziałek"),
    TUESDAY("Wtorek"),
    WEDNESDAY("Środa"),
    THURSDAY("Czwartek"),
    FRIDAY("Piątek"),
    SATURDAY("Sobota"),
    SUNDAY("Niedziela");

    private String translation;

    public static DayOfWeek getFromString(String dayName) {
        for (DayOfWeek day : values()) {
            if (day.name().equalsIgnoreCase(dayName)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Wystąpił błąd ekstrakcji nazwy dnia tygodnia z daty");
    }
}
