package simcity.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import simcity.DRestaurant.DWaiterRole;
import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterNormalRole;
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
	public Map<String, List<Location>> buildings = new HashMap<String, List<Location>>();
	private List<Location> restaurants = new ArrayList<Location>();
	private List<Location> banks = new ArrayList<Location>();
	private List<Location> markets = new ArrayList<Location>();
	private InputStream scenario;
	
	private List<Person> people = new ArrayList<Person>();
	
	public SimCityPanel(SimCityGui gui) {
		this.gui = gui;
		
		// populate buildings map 
		PersonAgent p1 = null;
		BankManagerRole bm = new BankManagerRole(p1);
		p1 = new PersonAgent("bankmanager", bm);
		banks.add(new Bank("Bank1", bm));
		
		PersonAgent p2 = null;
		MarketManagerRole mm =  new MarketManagerRole(p2);
		p2 = new PersonAgent("marketmanager", mm);
		markets.add(new Market("Market1", mm));

		PersonAgent p3 = null;
		KHostRole kh = new KHostRole(p3);
		p3 = new PersonAgent("khost", kh);
		restaurants.add(new Restaurant("KRestaurant", kh, "normal"));

		PersonAgent p4 = null;
		BHostRole bh = new BHostRole(p4);
		p4 = new PersonAgent("bhost", bh);
		restaurants.add(new Restaurant("BRestaurant", bh, "narmal"));

		PersonAgent p5 = null;
		Drew_HostRole drewh =  new Drew_HostRole(p5);
		p5 = new PersonAgent("drew_host", drewh);
		restaurants.add(new Restaurant("DrewRestaurant", drewh, "normal"));

		PersonAgent p6 = null;
		DHostRole dh = new DHostRole(p6);
		p6 = new PersonAgent("dhost", dh);
		restaurants.add(new Restaurant("DRestaurant", dh, "normal"));

		PersonAgent p7 = null;
		LHostRole lh =  new LHostRole(p7);
		p7 = new PersonAgent("lhost", lh);
		restaurants.add(new Restaurant("LRestaurant", lh, "normal"));

		PersonAgent p8 = null;
		THostRole th = new THostRole(p8);
		p8 = new PersonAgent("thost", th);
		restaurants.add(new Restaurant("TRestaurant", th, "normal"));

		buildings.put("Bank", banks);
		buildings.put("Market", markets);
		buildings.put("Restaurant", restaurants);
		
		
		try {
			scenario = new FileInputStream("config"+File.separator+"config1.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
//		DWaiterRole w = null;
//		PersonAgent waiter = new PersonAgent("waiter", w);
//		w = new DWaiterRole(waiter);
		
//		PersonAgent cust = new PersonAgent("cust", null);
//		cust.setPanel(this);
//		
//		p6.startThread();
//		waiter.startThread();
//		cust.startThread();
	}
	
	// customer factory
	// how to use factory in person
	//		Role r = p.customerFactory("DRestaurant", this);
	//		r.isActive = true;
	//r		oles.add(r);
	public Role customerFactory(String place, PersonAgent p) {
		Role c = null;
		if (place.equals("Bank")) {
			System.out.println("making bank customer");
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
			System.out.println("made drestaurantcust");
		}
		else if (place.equals("LRestaurant")) {
			c = new LCustomerRole(p);
		}
		else if (place.equals("TRestaurant")) {
			c = new TCustomerRole(p);
		}
		return c;
	}
	
	  public void addPerson(String job, String name) {

    		PersonAgent p = null;
    		Role personRole= new DWaiterRole(p);
    		p = new PersonAgent(name, personRole);
    		System.out.println(p.getJob());
    		PersonGui g = new PersonGui(p, gui);
    		p.setGui(g);
    		gui.simCityAnimationPanel.addGui(g);
    		p.startThread();
    		people.add(p);
//    		System.err.println("added person in restpanel");
	    	
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
