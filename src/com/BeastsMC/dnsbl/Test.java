package com.BeastsMC.dnsbl;

import org.apache.commons.net.whois.WhoisClient;

public class Test {
	public static void main(String[] args) {
		WhoisClient whois = new WhoisClient();
//		String ip = "83.87.220.206"; //Jort IP
		String ip = "201.32.135.53"; //Lock IP
//		String ip = "192.99.14.221"; //Creative IP
		try {
			//LOOKUP RiRs
//NetType:        Allocated to APNIC
//			whois.connect("whois.ripe.net");
//			whois.connect("whois.afrinic.net");
			whois.connect("whois.lacnic.net");
//			whois.connect("whois.apnic.net");
//			whois.connect("whois.arin.net");
			System.out.println(whois.query(ip));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
