package com.CMS.kinoCMS.models.DTO.Mappers;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.DTO.CinemaUpdateDto;

public class CinemaMapper {
    public static CinemaUpdateDto toCinemaUpdateDto(Cinema cinema) {
        CinemaUpdateDto dto = new CinemaUpdateDto();
        dto.setAddress(cinema.getAddress());
        dto.setXCoordinate(cinema.getXCoordinate());
        dto.setYCoordinate(cinema.getYCoordinate());
        return dto;
    }
}
