package com.beastsmc.dnsbl;

import java.util.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterDNSBL extends JavaPlugin {
	public HashMap<String, Boolean> ipCache;
	public List<String> bannedISPs;
	public HashSet<String> usernameBypass;
	public ASNLookup asnLookup;

	public void onEnable() {
		ipCache = new HashMap<String, Boolean>();
		bannedISPs = new ArrayList<>();
		usernameBypass = new HashSet<String>();
		loadData();
		getServer().getPluginManager().registerEvents(new LoginListener(this), this);
		getCommand("dnsbl").setExecutor(this);
	}
	
	private void loadData() {
		saveDefaultConfig();
		for(String name : getConfig().getStringList("username-bypass")) {
			usernameBypass.add(name);
		}
		for(String isp : getConfig().getStringList("banned-isps")) {
			bannedISPs.add(isp);
		}

		asnLookup = new ASNLookup(this.getDataFolder());
	}

	
	public boolean onCommand(CommandSender sender, Command cmd, String strCmd, String[] args) {
		if(args.length>0) {
			if(args[0].equalsIgnoreCase("reload")) {
				reloadConfig();
				loadData();
				sender.sendMessage("Config reloaded!");
				return true;
			}
		}
		return false;
	}

    public boolean isOrgBanned(String org) {
        /* List defined in config.yml may contain partial names
         * We must check to see if org contains ANY entries from black list
         */
        boolean bad = false;
        for(String needle : bannedISPs) {
            if(org.contains(needle)) {
                bad = true;
                break;
            }
        }

        return bad;
    }
}
