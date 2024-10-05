package com.CMS.kinoCMS.config.IpAddreses;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@Service
@Log4j2
public class GeoIPService {
    // Определяем местоположение пользователя по IP

    private DatabaseReader dbReader;


    @PostConstruct
    public void init() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resource = classLoader.getResourceAsStream("GeoLite2-City.mmdb");

        if (resource == null) {
            throw new FileNotFoundException("GeoLite2-City.mmdb not found in resources");
        }

        dbReader = new DatabaseReader.Builder(resource).build();
    }

    //Определение местоположения пользователя по БД GeoLite2
    public String getCityAndCountry(String ip) {
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            return "Localhost";
        }

        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            String city = response.getCity().getName();
            String country = response.getCountry().getName();
            return city + ", " + country;
        } catch (Exception e) {
            log.warn("Unknown location - GeoIpService - getCityAndCountry");
            return "Unknown location";
        }
    }

}
