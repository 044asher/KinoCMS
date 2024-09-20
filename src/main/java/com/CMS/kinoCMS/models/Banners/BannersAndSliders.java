package com.CMS.kinoCMS.models.Banners;

import com.CMS.kinoCMS.models.Pages.Contact;
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

    @OneToMany(mappedBy = "bannersAndSliders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BannerNewsActions> newsImages = new ArrayList<>();


    public void removeImage(BannerImage image) {
        images.remove(image);
        image.setBannersAndSliders(null);
    }

    public void addBannerImage(BannerImage image) {
        images.add(image);
        image.setBannersAndSliders(this);
    }

    public void addNewsImage(BannerNewsActions newsImage) {
        newsImages.add(newsImage);
        newsImage.setBannersAndSliders(this);
    }

    public Contact getFirst() {
        return null;
    }

    public void removeImage(BannerNewsActions newsImage) {
        newsImages.remove(newsImage);
        newsImage.setBannersAndSliders(null);
    }
}
