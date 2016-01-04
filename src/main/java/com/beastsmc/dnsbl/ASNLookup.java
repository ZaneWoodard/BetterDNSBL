package com.beastsmc.dnsbl;

import com.maxmind.geoip.LookupService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

public class ASNLookup {
    private LookupService asnl;
    private final String DATABASE_DOWNLOAD_LINK = "https://download.maxmind.com/download/geoip/database/asnum/GeoIPASNum.dat.gz";
    public ASNLookup(File geoipFolder) {
        try {
            File dbFile = new File(geoipFolder, "GeoIPASNum.dat");
            if(dbFile.exists()) {
                this.asnl = new LookupService(
                        dbFile,
                        LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE
                );
            } else {
                try {
                    downloadDatabase(dbFile);
                    this.asnl = new LookupService(
                            dbFile,
                            LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE
                    );
                } catch(MalformedURLException e) {
                    e.printStackTrace();
                };
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }

    /**
     * Look what organization an IP belongs to.
     * Benchmarked to ~100,000 queries/sec
     * @param ip The IPv4 address to search for
     * @return string - the name of the organization ip belongs to
     */
    public String lookup(String ip) {
        if(asnl==null) {
            return null;
        }
        return asnl.getOrg(ip);
    }


    /**
     * Downloads and unzips the binary Maxmind GeoLite Organization database
     * Source:
     */
    public void downloadDatabase(File destination) throws MalformedURLException {
        URL website = new URL(DATABASE_DOWNLOAD_LINK);
        try (InputStream in = new GZIPInputStream(website.openStream())) {
            Files.copy(in, destination.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
