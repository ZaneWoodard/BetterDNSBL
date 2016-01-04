package com.beastsmc.dnsbl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

public class LoginListener implements Listener {
	private final BetterDNSBL plugin;
    private final String DISALLOW_MESSAGE = "IP banned for being a proxy! Mistake? Contact beastsmc@gmail.com";
	public LoginListener(BetterDNSBL main) {
		this.plugin = main;
	}

	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
	public void filterLogins(AsyncPlayerPreLoginEvent e) {
		if(e.getLoginResult()==Result.ALLOWED) {
			if(plugin.usernameBypass.contains(e.getName())) return;


			String ip = e.getAddress().getHostAddress();
            Boolean cachedAllow = plugin.ipCache.get(ip);

            if(cachedAllow!=null) {
                if(!cachedAllow) {
                    e.disallow(Result.KICK_OTHER, DISALLOW_MESSAGE);
                }
            } else {
                String org = plugin.asnLookup.lookup(ip);
                if(org==null) {
                    plugin.getLogger().warning("No ASN found for " + ip + ", skipping validation");
                } else if (plugin.isOrgBanned(org)) {
                    e.disallow(Result.KICK_OTHER, DISALLOW_MESSAGE);
                    plugin.ipCache.put(ip, false);
                } else {
                    plugin.ipCache.put(ip, true);
                }
            }
		}
	}
}
