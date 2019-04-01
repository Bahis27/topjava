package ru.javawebinar.topjava.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class StringToLocalDate implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(@Nullable String source) {
        return StringUtils.isEmpty(source) ? null : LocalDate.parse(source);
    }
}
