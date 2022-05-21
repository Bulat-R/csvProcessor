package com.example.csvprocessor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectQueryDto {
    private String fileName;
    private int rowNumber;
    private String jsonValue;

    public String[] toStringArray() {
        return new String[]{fileName, String.valueOf(rowNumber), jsonValue};
    }
}
