package com.CMS.kinoCMS.admin.models.DTO.Mappers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.DTO.CinemaUpdateDto;

public class CinemaMapper {
    public static CinemaUpdateDto toCinemaUpdateDto(Cinema cinema) {
        CinemaUpdateDto dto = new CinemaUpdateDto(cinema.getAddress(), cinema.getXCoordinate(), cinema.getYCoordinate());
        return dto;
    }
}
