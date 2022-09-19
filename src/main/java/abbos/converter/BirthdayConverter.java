package com.abbos.converter;

import com.abbos.entitiy.BirthDay;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.util.Optional;


@Converter(autoApply = true)
public class BirthdayConverter implements AttributeConverter<BirthDay,Date> {


    // from Java Type to SQL Type
    @Override
    public Date convertToDatabaseColumn(BirthDay birthDay) {
        return Optional.ofNullable(birthDay)
                .map(BirthDay::birthDate)
                .map(Date::valueOf)
                .orElse(null);
    }



    // from SQL Type to Java Type
    @Override
    public BirthDay convertToEntityAttribute(Date date) {
        return Optional.ofNullable(date)
                .map(Date::toLocalDate)
                .map(BirthDay::new)
                .orElse(null);
    }
}
