package backend.tangsquad.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        // Handle null or empty list
        return attribute == null || attribute.isEmpty() ? "" :
                attribute.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        // Handle null or empty data
        return dbData == null || dbData.isEmpty() ? Collections.emptyList() :
                Arrays.stream(dbData.split(",")).map(Long::valueOf).collect(Collectors.toList());
    }
}
