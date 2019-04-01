package ru.javawebinar.topjava.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalTime;

public class StringToLocalTime implements Converter<String, LocalTime> {

    @Override
    public LocalTime convert(@Nullable String source) {
        return StringUtils.isEmpty(source) ? null : LocalTime.parse(source);
    }
}
