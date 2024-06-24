package com.CMS.kinoCMS.admin.models.DTO.Mappers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.DTO.CinemaUpdateDto;

public class CinemaMapper {
    public static CinemaUpdateDto toCinemaUpdateDto(Cinema cinema) {
        CinemaUpdateDto dto = new CinemaUpdateDto();
        dto.setAddress(cinema.getAddress());
        dto.setXCoordinate(cinema.getXCoordinate());
        dto.setYCoordinate(cinema.getYCoordinate());
        return dto;
    }
}
