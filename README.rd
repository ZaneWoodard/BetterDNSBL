BetterDNSBL was made with the goal of being able to blacklist vast swaths of IPv4 addresses at once.

It accomplishes this by resolving IP addresses to ASNs(or organizations), and then checking the orgnization against a blacklist.

Previously, ARIN and other RIRs were queried to produce such data.
Currently, BetterDNSBL is being migrated to utilize a public, downloadable MaxMind ASN database.
Database can be downloaded at: https://dev.maxmind.com/geoip/legacy/geolite/