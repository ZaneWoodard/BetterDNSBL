package com.beastsmc.dnsbl;

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
                String org = plugin.asnLookup.lookup(ip);
                if(plugin.isOrgBanned(org)) {
                    e.disallow(Result.KICK_OTHER, "IP banned for being a proxy! Mistake? Contact beastsmc@gmail.com");
                    plugin.ipCache.put(ip, false);
                } else {
                    plugin.ipCache.put(ip, true);
                }
			}
		}
	}
}
