package com.BeastsMC.dnsbl;

import org.apache.commons.net.whois.WhoisClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class LoginListener implements Listener {
	private final BetterDNSBL plugin;
	public LoginListener(BetterDNSBL main) {
		this.plugin = main;
	}
	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
	public void filterLogins(AsyncPlayerPreLoginEvent e) {
		if(e.getLoginResult()==Result.ALLOWED) {
			if(plugin.usernameBypass.contains(e.getName())) return;
			String ip = e.getAddress().getHostAddress();
			if(plugin.ipCache.containsKey(ip)) {
				boolean cachedAllow = plugin.ipCache.get(ip);
				if(!cachedAllow) {
					e.disallow(Result.KICK_OTHER, "IP banned for being a proxy! Mistake? Contact beastsmc@gmail.com");
				} else {
					return;
				}
			} else {
				WhoisClient whois = new WhoisClient();
				try {
					whois.connect("whois.arin.net");
					String[] data = whois.query(ip).split("\n");
					for(String line : data) {
						if(line.startsWith("OrgName")) {
							String isp = line.replaceFirst("OrgName: *", "");
							if(plugin.bannedISPs.contains(isp)) {
								e.disallow(Result.KICK_OTHER, "IP banned for being a proxy! Mistake? Contact beastsmc@gmail.com");
								plugin.ipCache.put(ip, false);
							} else {
								plugin.ipCache.put(ip, true);
							}
						}
					}
				} catch (Exception e1) {
					//If any exception happens, just let the player connect
					return;
				}
			}
		}
	}
}
