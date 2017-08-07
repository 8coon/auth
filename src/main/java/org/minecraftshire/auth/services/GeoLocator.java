package org.minecraftshire.auth.services;


import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import org.minecraftshire.auth.Server;
import org.minecraftshire.auth.utils.logging.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


@Service
public class GeoLocator {

    private static DatabaseReader reader = null;
    public static final String LOCATION_UNKNOWN = "(Неизвестно)";


    public static DatabaseReader getDatabase() {
        if (reader == null) {
            try {
                List<String> locales = new ArrayList<>();
                locales.add("RU");

                reader = new DatabaseReader
                        .Builder(
                            new File(Server.getPath() + "/assets/geo-db/CurrentVersion/GeoLite2-City.mmdb")
                        ).locales(locales).build();
            } catch (IOException e) {
                Logger.getLogger().severe(e);

                System.exit(-1);
                return null;
            }
        }

        return reader;
    }


    public GeoLocator() {
    }


    public String lookupAddress(String ip) {
        InetAddress address;
        CityResponse response;

        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            return GeoLocator.LOCATION_UNKNOWN;
        }

        try {
            response = GeoLocator.getDatabase().city(address);
            if (response == null) throw new NullPointerException();
        } catch (IOException | NullPointerException e) {
            Logger.getLogger().severe(e);
            return GeoLocator.LOCATION_UNKNOWN;
        } catch (GeoIp2Exception e2) {
            return GeoLocator.LOCATION_UNKNOWN;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(response.getLocation().getLatitude());
        sb.append(" ");
        sb.append(response.getLocation().getLongitude());
        sb.append("; ");
        sb.append(response.getCountry().getIsoCode());
        sb.append("; ");
        sb.append(response.getPostal().getCode());
        sb.append(", ");
        sb.append(response.getCity().getName());
        sb.append(", ");
        sb.append(response.getCountry().getName());

        return sb.toString();
    }

}
