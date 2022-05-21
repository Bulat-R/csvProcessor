package com.example.csvprocessor.controller;

import com.example.csvprocessor.service.ScvService;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/csv")
@RequiredArgsConstructor
public class ScvController {

    private final ScvService service;

    @PostMapping(consumes = "text/csv")
    public void save(@RequestParam("filename") String fileName, HttpServletRequest request) throws Exception {
        service.save(fileName, request.getReader());
    }

    @GetMapping
    public void get(@RequestParam("jsonkey") String jsonKey, HttpServletResponse response) throws Exception {
        service.get(jsonKey, response.getWriter());
    }

    @ExceptionHandler(CsvValidationException.class)
    public ResponseEntity<String> handleCsvException(CsvException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<String> handleServletException(ServletException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> allException(ServletException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
