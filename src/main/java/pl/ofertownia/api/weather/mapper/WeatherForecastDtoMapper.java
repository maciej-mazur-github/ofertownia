package pl.ofertownia.api.weather.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.ofertownia.api.weather.*;
import pl.ofertownia.api.weather.dtofield.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherForecastDtoMapper {
    private static final String BASE_ICON_URL = "https://openweathermap.org/img/wn/";
    private final ResponseWeatherToViewWeatherMapper responseWeatherToViewWeatherMapper = new ResponseWeatherToViewWeatherMapper();

    public CurrentWeatherViewDto mapCurrentWeatherResponseDtoToCurrentWeatherViewDto(CurrentWeatherResponseDto responseDto) {
        ViewWeather weather = mapWeatherArray(responseDto.getWeather());
        ViewMain viewMain = mapResponseMainToViewMain(responseDto.getMain());
        ViewWind viewWind = mapResponseWindToViewWind(responseDto.getWind());
        BigDecimal visibility = responseDto.getVisibility().divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_DOWN);
        LocalDateTime dt = null;
        DayOfWeek dayOfWeek = null;
        if (responseDto.getDt() != null) {
            dt = mapDateToLocaleDateTime(responseDto.getDt());
            dayOfWeek = DayOfWeek.getFromString(dt.getDayOfWeek().toString());
        }
        ViewSys sys = mapResponseSysToViewSys(responseDto.getSys());
        return CurrentWeatherViewDto.builder()
                .weather(weather)
                .main(viewMain)
                .visibility(visibility)
                .wind(viewWind)
                .dt(dt)
                .dayOfWeek(dayOfWeek)
                .sys(sys)
                .name(responseDto.getName())
                .build();
    }

    public ForecastViewDto mapForecastResponseDtoToForecastViewDto(ForecastResponseDto responseDto) {
        List<CurrentWeatherViewDto> list = Arrays.stream(responseDto.getList())
                .map(this::mapCurrentWeatherResponseDtoToCurrentWeatherViewDto)
                .collect(Collectors.toList());
        ForecastViewDto.City city = new ForecastViewDto.City(
                mapDateToLocaleDateTime(responseDto.getCity().getSunrise()),
                mapDateToLocaleDateTime(responseDto.getCity().getSunset())
        );
        return ForecastViewDto.builder().
                list(list)
                .city(city)
                .build();
    }

    private ViewSys mapResponseSysToViewSys(ResponseSys sys) {
        LocalDateTime sunrise = null;
        LocalDateTime sunset = null;
        if (sys.getSunrise() != null) {
            sunrise = mapDateToLocaleDateTime(sys.getSunrise());
        }
        if (sys.getSunset() != null) {
            sunset = mapDateToLocaleDateTime(sys.getSunset());
        }
        return new ViewSys(sunrise, sunset);
    }

    private LocalDateTime mapDateToLocaleDateTime(Long dt) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.systemDefault());
    }

    private ViewWind mapResponseWindToViewWind(ResponseWind wind) {
        return ViewWind.builder()
                .speed(wind.getSpeed())
                .deg(wind.getDeg())
                .gust(wind.getGust())
                .build();
    }

    private ViewMain mapResponseMainToViewMain(ResponseMain main) {
        return new ViewMain(
                main.getTemp().round(new MathContext(2, RoundingMode.HALF_DOWN)),
                main.getFeels_like().round(new MathContext(2, RoundingMode.HALF_DOWN)),
                main.getTemp_min().round(new MathContext(2, RoundingMode.HALF_DOWN)),
                main.getTemp_max().round(new MathContext(2, RoundingMode.HALF_DOWN)),
                main.getPressure(),
                main.getHumidity()
        );
    }

    private ViewWeather mapWeatherArray(ResponseWeather[] weather) {
        List<ViewWeather> list = Arrays.stream(weather)
                .map(responseWeatherToViewWeatherMapper::map)
                .collect(Collectors.toList());
        String joinedDescription = list.stream()
                .map(ViewWeather::getDescription)
                .collect(Collectors.joining(", "));
        ViewWeather viewWeather = list.get(0);
        viewWeather.setDescription(StringUtils.capitalize(joinedDescription));
        return viewWeather;
    }

    private class ResponseWeatherToViewWeatherMapper {
        public ViewWeather map(ResponseWeather responseWeather) {
            String iconUrl = ServletUriComponentsBuilder
                    .fromHttpUrl(BASE_ICON_URL)
                    .path("%s@2x.png".formatted(responseWeather.getIcon()))
                    .toUriString();
            return new ViewWeather(responseWeather.getDescription(), iconUrl);
        }
    }
}
