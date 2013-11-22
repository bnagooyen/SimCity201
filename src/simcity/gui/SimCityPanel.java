package simcity.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import agent.Role;
import simcity.PersonAgent;
import simcity.BRestaurant.BCustomerRole;
import simcity.BRestaurant.BHostRole;
import simcity.Bank.BankCustomerRole;
import simcity.Bank.BankManagerRole;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DHostRole;
import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LHostRole;
import simcity.Market.MarketCustomerRole;
import simcity.Market.MarketManagerRole;
import simcity.TTRestaurant.TCustomerRole;
import simcity.TTRestaurant.THostRole;
import simcity.interfaces.BankManager;
import simcity.interfaces.Host;
import simcity.interfaces.MarketManager;
import simcity.interfaces.Person;

public class SimCityPanel extends JPanel{

	private SimCityGui gui;
	private Map<String, List<Location>> buildings = new HashMap<String, List<Location>>();
	private List<Location> restaurants = new ArrayList<Location>();
	private List<Location> banks = new ArrayList<Location>();
	private List<Location> markets = new ArrayList<Location>();
	
	public SimCityPanel(SimCityGui gui) {
		this.gui = gui;
		
		// populate buildings map 
		BankManagerRole bm = null;
		PersonAgent p1 = new PersonAgent("bankmanager", bm);
		bm = new BankManagerRole(p1);
		banks.add(new Bank("Bank1", bm));
		System.out.println("added bank");
		
		MarketManagerRole mm = null;
		PersonAgent p2 = new PersonAgent("marketmanager", mm);
		mm = new MarketManagerRole(p2);
		markets.add(new Market("Market1", mm));
		System.out.println("added market");

		KHostRole kh = null;
		PersonAgent p3 = new PersonAgent("khost", kh);
		kh = new KHostRole(p3);
		restaurants.add(new Restaurant("KRestaurant", kh, "normal"));
		System.out.println("added krestaurant");

		BHostRole bh = null;
		PersonAgent p4 = new PersonAgent("bhost", bh);
		bh = new BHostRole(p4);
		restaurants.add(new Restaurant("BRestaurant", bh, "narmal"));
		System.out.println("added brestaurant");

		Drew_HostRole drewh = null;
		PersonAgent p5 = new PersonAgent("drew_host", drewh);
		drewh = new Drew_HostRole(p5);
		restaurants.add(new Restaurant("DrewRestaurant", drewh, "normal"));
		System.out.println("added drew_restaurant");

		DHostRole dh = null;
		PersonAgent p6 = new PersonAgent("dhost", dh);
		dh = new DHostRole(p6);
		restaurants.add(new Restaurant("DRestaurant", dh, "normal"));
		System.out.println("added drestaurant");

		LHostRole lh = null;
		PersonAgent p7 = new PersonAgent("lhost", lh);
		lh = new LHostRole(p7);
		restaurants.add(new Restaurant("LRestaurant", lh, "normal"));
		System.out.println("added lrestaurant");

		THostRole th = null;
		PersonAgent p8 = new PersonAgent("thost", th);
		th = new THostRole(p8);
		restaurants.add(new Restaurant("TRestaurant", th, "normal"));
		System.out.println("added trestaurant");

		buildings.put("Bank", banks);
		buildings.put("Market", markets);
		buildings.put("Restaurant", restaurants);
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
		else if (place.equals("BRestaurant")) {
			c = new BCustomerRole(p);
		}
		else if (place.equals("Drew_restaurant")) {
			c = new Drew_CustomerRole(p);
		}
		else if (place.equals("DRestaurant")) {
			c = new DCustomerRole(p);
		}
		else if (place.equals("LRestaurant")) {
			c = new LCustomerRole(p);
		}
		else if (place.equals("TRestaurant")) {
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
