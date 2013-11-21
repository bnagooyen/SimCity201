package simcity.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.Role;
import simcity.PersonAgent;
import simcity.BRestaurant.BCustomerRole;
import simcity.Bank.BankCustomerRole;
import simcity.Drew_restaurant.CustomerRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.Market.MarketCustomerRole;
import simcity.TTRestaurant.TCustomerRole;
import simcity.interfaces.BankManager;
import simcity.interfaces.Host;
import simcity.interfaces.MarketManager;
import simcity.interfaces.Person;

public class SimCityPanel {

	private SimCityGui gui;
	public Map<String, List<Location>> buildings = new HashMap<String, List<Location>>();
	
	
	public SimCityPanel(SimCityGui gui) {
		this.gui = gui;
		
		
	}
	
	// customer factory
	public Role customerFactory(String place, PersonAgent p) {
		Role c = null;
		if (place.equals("Bank")) {
			c = new BankCustomerRole(p);
		}
		else if (place.equals("Market")) {
			c = new MarketCustomerRole(p);
		}
		else if (place.equals("KRestaurant")) {
			c = new KCustomerRole(p);
		}
		else if (place.equals("BCustomerRole")) {
			c = new BCustomerRole(p);
		}
		else if (place.equals("DrewCustomerRole")) {
			c = new CustomerRole(p);
		}
		else if (place.equals("TCustomerRole")) {
			c = new TCustomerRole(p);
		}
		return c;
	}
	
	// Location classes
	public abstract class Location {
		// Location l
		String name;
	}
	
	public class Restaurant extends Location{
		Host host;
		String foodType;
		
		public Restaurant(String n, Host h, String type) {
			name = n;
			host = h;
			foodType = type;
		}
	}
	
	public class Bank extends Location{
		BankManager manager;
		
		public Bank(String n, BankManager m){
			name = n;
			manager = m;
		}
	}
	
	public class Market extends Location {
		MarketManager manager;
		
		public Market(String n, MarketManager m) {
			name = n;
			manager = m;
		}
	}
}
