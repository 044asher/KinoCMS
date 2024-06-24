package com.CMS.kinoCMS.admin.models.Banners;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table
@AllArgsConstructor
@NoArgsConstructor
public class BannersAndSliders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String background;

    @OneToMany(mappedBy = "bannersAndSliders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BannerImage> images = new ArrayList<>();

    public void addImage(BannerImage image) {
        images.add(image);
        image.setBannersAndSliders(this);
    }

    public void removeImage(BannerImage image) {
        images.remove(image);
        image.setBannersAndSliders(null);
    }
}
