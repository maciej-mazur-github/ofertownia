package pl.ofertownia.api.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pl.ofertownia.api.weather.exception.CityNotFoundException;
import pl.ofertownia.api.weather.exception.ServerConnectionException;
import pl.ofertownia.api.weather.mapper.WeatherForecastDtoMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class WeatherService {

    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q={cityName},pl&units=metric&lang=pl&appid={apiKey}";
    private static final String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast?q={cityName},pl&appid={apiKey}&lang=pl&units=metric";

    private final WeatherForecastDtoMapper mapper;

    @Value("${openWeather.apiKey}")
    private String apiKey;

    private RestTemplate restTemplate = new RestTemplate();

    WeatherService(WeatherForecastDtoMapper mapper) {
        this.mapper = mapper;
    }

    public CurrentWeatherViewDto getCurrentWeather(String cityName) {
        try {
            CurrentWeatherResponseDto responseDto = restTemplate.getForObject(
                    CURRENT_WEATHER_URL,
                    CurrentWeatherResponseDto.class,
                    cityName,
                    apiKey);
            return mapper.mapCurrentWeatherResponseDtoToCurrentWeatherViewDto(responseDto);
        } catch (HttpClientErrorException e) {
            throw new CityNotFoundException("Nie znaleziono polskiego miasta o nazwie \"" + cityName + "\"");
        } catch (ResourceAccessException e) {
            throw new ServerConnectionException("Wystąpił problem z połączeniem z serwerem.");
        }
    }

    private ForecastViewDto getFullForecast(String cityName) {
        try {
            ForecastResponseDto responseDto = restTemplate.getForObject(
                    FORECAST_URL,
                    ForecastResponseDto.class,
                    cityName,
                    apiKey
            );
            return mapper.mapForecastResponseDtoToForecastViewDto(responseDto);
        } catch (HttpClientErrorException e) {
            throw new CityNotFoundException("Nie znaleziono polskiego miasta o nazwie \"" + cityName + "\"");
        } catch (ResourceAccessException e) {
            throw new ServerConnectionException("Wystąpił problem z połączniem z serwerem.");
        }
    }

    private Map<String, List<CurrentWeatherViewDto.WeatherTempIconUrlTime>> get5DayForecast(ForecastViewDto fullForecastViewDto) {
         return fullForecastViewDto.getList().stream()
                .filter(weather -> weather.getDt().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()))
                .map(this::getWeatherTempIconUrlTime)
                .collect(Collectors.groupingBy(weather ->
                        {
                            LocalDateTime dateTime = weather.time();
                            String weekDayName = DayOfWeek.getFromString(dateTime.getDayOfWeek().toString()).getTranslation();
                            String dayOfMonth = String.valueOf(dateTime.getDayOfMonth());
                            String abbrMonth = dateTime.getMonth()
                                    .getDisplayName(TextStyle.SHORT, new Locale("pl", "Poland"));
                            return "%s, %s %s".formatted(weekDayName, dayOfMonth, abbrMonth);
                        },
                        () -> new LinkedHashMap<>(),
                        Collectors.toList()));
    }

    private CurrentWeatherViewDto.WeatherTempIconUrlTime getWeatherTempIconUrlTime(CurrentWeatherViewDto weather) {
        LocalDateTime time = weather.getDt();
        DayOfWeek dayOfWeek = DayOfWeek.getFromString(time.getDayOfWeek().toString());
        BigDecimal temp = weather.getMain().getTemp();
        String iconUrl = weather.getWeather().getIconUrl();
        return new CurrentWeatherViewDto.WeatherTempIconUrlTime(temp, iconUrl, time, dayOfWeek);
    }

    private List<CurrentWeatherViewDto.WeatherTempIconUrlTime> get24hForecast(ForecastViewDto fullForecastViewDto) {
        return fullForecastViewDto.getList().stream()
                .map(this::getWeatherTempIconUrlTime)
                .limit(9)
                .collect(Collectors.toList());
    }

    public FullForecast get24HoursAnd5DaysForecast(String cityName) {
        ForecastViewDto fullForecastViewDto = getFullForecast(cityName);
        return new FullForecast(
                get24hForecast(fullForecastViewDto),
                get5DayForecast(fullForecastViewDto)
        );
    }
}
