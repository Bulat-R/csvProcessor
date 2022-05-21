package com.example.csvprocessor.dao;

import com.example.csvprocessor.dto.SelectQueryDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScvMapper {

    @Insert("INSERT INTO scv_data (file_name, row_number, json_data) VALUES (#{fileName}, #{rowNumber}, #{scvString}::jsonb)")
    void saveScv(@Param("fileName") String fileName, @Param("rowNumber") int rowNumber, @Param("scvString") String scvString);

    @Select("SELECT file_name as fileName, row_number as rowNumber, json_value as jsonValue FROM find_by_json_key(#{jsonKey})")
    List<SelectQueryDto> findByJsonKey(@Param("jsonKey") String jsonKey);
}
