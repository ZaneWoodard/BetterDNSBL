package com.BeastsMC.dnsbl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.BeastsMC.dnsbl.whois.WhoisServer;

public class BetterDNSBL extends JavaPlugin {
	public HashMap<String, Boolean> ipCache;
	public HashSet<String> bannedISPs;
	public HashSet<String> usernameBypass;
	public HashMap<String, WhoisServer> prefixToRIR;
	public void onEnable() {
		ipCache = new HashMap<String, Boolean>();
		bannedISPs = new HashSet<String>();
		usernameBypass = new HashSet<String>();
		loadData();
		loadRIRData();
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
	}
	
	private void loadRIRData() {
		prefixToRIR = new HashMap<String, WhoisServer>();
		InputStream in = this.getResource("RIRs.txt");
		Scanner scan  = new Scanner(in);
		while(scan.hasNext()) {
			String[] entry = scan.nextLine().split(",");
			WhoisServer rir = WhoisServer.getRIRFromString(entry[1]);
			prefixToRIR.put(entry[0], rir);
		}
		scan.close();
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
}
