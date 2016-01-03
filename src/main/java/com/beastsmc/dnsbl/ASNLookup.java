package com.beastsmc.dnsbl;

import com.maxmind.geoip.LookupService;

import java.io.File;
import java.io.IOException;

public class ASNLookup {
    private LookupService asnl;
    public ASNLookup(File geoipFolder) {
        try {
            this.asnl = new LookupService(
                    new File(geoipFolder, "GeoIPASNum.dat"),
                    LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE
            );
        } catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }

    /**
     * Look what organization an ISP belongs to.
     * Benchmarked to ~100,000 queries/sec
     * @param ip
     * @return
     */
    public String lookup(String ip) {
        return asnl.getOrg(ip);
    }
}
