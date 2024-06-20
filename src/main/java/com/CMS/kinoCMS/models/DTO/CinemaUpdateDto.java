package com.CMS.kinoCMS.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CinemaUpdateDto {
    private String address;
    private double xCoordinate;
    private double yCoordinate;
    private MultipartFile logoName;
}
