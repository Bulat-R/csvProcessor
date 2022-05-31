package com.example.csvprocessor.service;

import com.example.csvprocessor.dao.ScvMapper;
import com.example.csvprocessor.dto.SelectQueryDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScvService {

    private final ScvMapper mapper;

    @Transactional(rollbackFor = {IOException.class, CsvException.class})
    public void save(String fileName, Reader reader) throws IOException, CsvValidationException {
        //TODO если требуется ограничение уникальности файлов -> создать для файлов отдельную таблицу, проверять при запросе
        try (CSVReader csvReader = new CSVReader(reader)) {
            String[] headers = csvReader.readNext();
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                if (values.length != headers.length) {
                    throw new CsvValidationException("Csv format error on line " + csvReader.getLinesRead());
                }
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < headers.length; i++) {
                    jsonObject.put(headers[i], values[i]);
                }
                mapper.saveScv(fileName, (int) csvReader.getLinesRead(), jsonObject.toString());
            }
        }
    }

    public void get(String jsonKey, Writer writer) throws IOException {
        List<SelectQueryDto> result = mapper.findByJsonKey(jsonKey);
        if (result == null || result.isEmpty()) {
            return;
        }
        String[] headers = {"fileName", "rowNumber", "jsonValue"};
        List<String[]> strings = result.stream()
                .map(SelectQueryDto::toStringArray)
                .collect(Collectors.toCollection(LinkedList::new));
        strings.add(0, headers);
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            csvWriter.writeAll(strings);
        }
    }
}

