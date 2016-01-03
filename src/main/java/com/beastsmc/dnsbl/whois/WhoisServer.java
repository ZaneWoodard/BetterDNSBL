package com.beastsmc.dnsbl.whois;

public enum WhoisServer {
	
	AFRINIC("whois.afrinic.net"),
	APNIC("whois.apnic.net"),
	ARIN("whois.arin.net"),
	LACNIC("whois.lacnic.net"),
	RIPE("whois.ripe.net");
	
	
	private final String ip;
	
	private WhoisServer(String ip) {
		this.ip = ip;
	}
	
	public static WhoisServer getRIRFromString(String name) {
		switch (name) {
			case "AFRINIC": return AFRINIC;
			case "APNIC": return APNIC;
			case "ARIN": return ARIN;
			case "LACNIC": return LACNIC;
			case "RIPE": return RIPE;
			default: return ARIN; //Shouldn't happen. But if it does ARIN probably has it.
		}
	}
}