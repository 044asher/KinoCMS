package com.CMS.kinoCMS.models.DTO.Mappers;

import com.CMS.kinoCMS.models.Cinema;
import com.CMS.kinoCMS.models.DTO.CinemaUpdateDto;

public class CinemaMapper {
    public static CinemaUpdateDto toCinemaUpdateDto(Cinema cinema) {
        return new CinemaUpdateDto(cinema.getAddress(), cinema.getXCoordinate(), cinema.getYCoordinate());
    }
}
