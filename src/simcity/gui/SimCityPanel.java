package simcity.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.interfaces.BankManager;
import simcity.interfaces.Host;
import simcity.interfaces.MarketManager;

public class SimCityPanel {

	private SimCityGui gui;
	public Map<String, List<Location>> buildings = new HashMap<String, List<Location>>();
	
	
	public SimCityPanel(SimCityGui gui) {
		this.gui = gui;
		
		
	}
	
	// Location classes
	public abstract class Location {
		// Location l
		String name;
		String customerRole;
	}
	
	public class Restaurant extends Location{
		Host host;
		String foodType;
		
		public Restaurant(String n, String c, Host h, String type) {
			name = n;
			customerRole = c;
			host = h;
			foodType = type;
		}
	}
	
	public class Bank extends Location{
		BankManager manager;
		
		public Bank(String n, String c, BankManager m){
			name = n;
			customerRole = c;
			manager = m;
		}
	}
	
	public class Market extends Location {
		MarketManager manager;
		
		public Market(String n, String c, MarketManager m) {
			name = n;
			customerRole = c;
			manager = m;
		}
	}
}
