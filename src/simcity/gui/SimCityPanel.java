package simcity.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JPanel;

import agent.Role;
import simcity.PersonAgent;
import simcity.BRestaurant.BCashierRole;
import simcity.BRestaurant.BCookRole;
import simcity.BRestaurant.BCustomerRole;
import simcity.BRestaurant.BHostRole;
import simcity.BRestaurant.BWaiterRole;
import simcity.Bank.BankCustomerRole;
import simcity.Bank.BankLoanOfficerRole;
import simcity.Bank.BankManagerRole;
import simcity.Bank.BankRobberRole;
import simcity.Bank.BankTellerRole;
import simcity.DRestaurant.DCashierRole;
import simcity.DRestaurant.DCookRole;
import simcity.DRestaurant.DCustomerRole;
import simcity.DRestaurant.DHostRole;
import simcity.DRestaurant.DWaiterRole;
import simcity.Drew_restaurant.Drew_CashierRole;
import simcity.Drew_restaurant.Drew_CookRole;
import simcity.Drew_restaurant.Drew_CustomerRole;
import simcity.Drew_restaurant.Drew_HostRole;
import simcity.Drew_restaurant.Drew_WaiterRole;
import simcity.KRestaurant.KCashierRole;
import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterNormalRole;
import simcity.KRestaurant.KWaiterRole;
import simcity.KRestaurant.KWaiterSharedDataRole;
import simcity.LRestaurant.LCashierRole;
import simcity.LRestaurant.LCookRole;
import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LHostRole;
import simcity.LRestaurant.LWaiterRole;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MarketCashierRole;
import simcity.Market.MarketCustomerRole;
import simcity.Market.MarketManagerRole;
import simcity.TTRestaurant.TCashierRole;
import simcity.TTRestaurant.TCookRole;
import simcity.TTRestaurant.TCustomerRole;
import simcity.TTRestaurant.THostRole;
import simcity.TTRestaurant.TWaiterRole;
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
	
	private List<Person> people = new ArrayList<Person>();
	
	static Scanner in;
	
	public SimCityPanel(SimCityGui gui) {
		this.gui = gui;
		
//		// populate buildings map 
//		PersonAgent p1 = new PersonAgent("bankmanager");
//		BankManagerRole bm = new BankManagerRole(p1);
//		p1.SetJob(bm);
//		banks.add(new Bank("Bank1", bm));
//		
//		PersonAgent p2 = new PersonAgent("marketmanager");
//		MarketManagerRole mm =  new MarketManagerRole(p2);
//		p2.SetJob(mm);
//		markets.add(new Market("Market1", mm));
//
//		PersonAgent p3 = null;
//		KHostRole kh = new KHostRole(p3);
//		p3 = new PersonAgent("khost");
//		restaurants.add(new Restaurant("KRestaurant", kh, "normal"));
//
//		PersonAgent p4 = null;
//		BHostRole bh = new BHostRole(p4);
//		p4 = new PersonAgent("bhost");
//		restaurants.add(new Restaurant("BRestaurant", bh, "narmal"));
//
//		PersonAgent p5 = null;
//		Drew_HostRole drewh =  new Drew_HostRole(p5);
//		p5 = new PersonAgent("drew_host");
//		restaurants.add(new Restaurant("DrewRestaurant", drewh, "normal"));
//
//		PersonAgent p6 = null;
//		DHostRole dh = new DHostRole(p6);
//		p6 = new PersonAgent("dhost");
//		restaurants.add(new Restaurant("DRestaurant", dh, "normal"));
//
//		PersonAgent p7 = null;
//		LHostRole lh =  new LHostRole(p7);
//		p7 = new PersonAgent("lhost", lh);
//		restaurants.add(new Restaurant("LRestaurant", lh, "normal"));
//
//		PersonAgent p8 = null;
//		THostRole th = new THostRole(p8);
//		p8 = new PersonAgent("thost", th);
//		restaurants.add(new Restaurant("TRestaurant", th, "normal"));
//
//		buildings.put("Bank", banks);
//		buildings.put("Market", markets);
//		buildings.put("Restaurant", restaurants);
//		
		
		try {
			in  = new Scanner(new FileReader("config"+File.separator+"config1.txt"));
			System.out.println(in.next());
			int numItems = in.nextInt();
			//clear input template
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			
			for(int i=0; i<numItems; i++) {
//				System.out.println(in.next());
//				System.out.println(in.nextDouble());
//				System.out.println(in.next());
//				System.out.println(in.nextBoolean());
				Person p = new PersonAgent(in.next());
				p.SetMoney(in.nextDouble());
			}
			in.close();
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
	
	public Role jobFactory(String job, PersonAgent p) {
		Role j = null;
		// bank 
		if (job.equals("BankLoanOfficer")) {
			j = new BankLoanOfficerRole(p);
		}
		else if (job.equals("BankManager")) {
			j = new BankManagerRole(p);
		}
		else if (job.equals("BankRobber")) {
			j = new BankRobberRole(p);
		}
		else if (job.equals("BankTeller")) {
			j = new BankTellerRole(p);
		}
		
		// market
		else if (job.equals("InventoryBoy")) {
			j = new InventoryBoyRole(p);
		}
		else if (job.equals("MarketCashier")) {
			j = new MarketCashierRole(p);
		}
		else if (job.equals("MarketManager")) {
			j = new MarketManagerRole(p);
		}
	
		// BRestaurant
		else if (job.equals("BCashier")) {
			j = new BCashierRole(p);
		}
		else if (job.equals("BCook")) {
			j = new BCookRole(p);
		}
		else if (job.equals("BHost")) {
			j = new BHostRole(p);
		}
		else if (job.equals("BWaiter")) {
			j = new BWaiterRole(p);
		}
		
		// DRestaurant
		else if (job.equals("DCashier")) {
			j = new DCashierRole(p);
		}
		else if (job.equals("DCook")) {
			j = new DCookRole(p);
		}
		else if (job.equals("DHost")) {
			j = new DHostRole(p);
		}
		else if (job.equals("DWaiter")) {
			j = new DWaiterRole(p);
		}
		
		// Drew_Restaurant
		else if (job.equals("DrewCashier")) {
			j = new Drew_CashierRole(p);
		}
		else if (job.equals("DrewCook")) {
			j = new Drew_CookRole(p);
		}
		else if (job.equals("DrewHost")) {
			j = new Drew_HostRole(p);
		}
		else if (job.equals("DrewWaiter")) {
			j = new Drew_WaiterRole(p);
		}
		
		// KRestaurant
		else if (job.equals("KCashier")) {
			j = new KCashierRole(p);
		}
		else if (job.equals("KCook")) {
			j = new KCookRole(p);
		}
		else if (job.equals("KHost")) {
			j = new KHostRole(p);
		}
		else if (job.equals("KWaiterNormal")) {
			j = new KWaiterNormalRole(p);
		}
		else if (job.equals("KWaiterSharedData")) {
			j = new KWaiterSharedDataRole(p);
		}
		
		// LRestaurant
		else if (job.equals("LCashier")) {
			j = new LCashierRole(p);
		}
		else if (job.equals("LCook")) {
			j = new LCookRole(p);
		}
		else if (job.equals("LHost")) {
			j = new LHostRole(p);
		}
		else if (job.equals("LWaiter")) {
			j = new LWaiterRole(p);
		}
		
		// TTRestaurant
		else if (job.equals("TCashier")) {
			j = new TCashierRole(p);
		}
		else if (job.equals("TCook")) {
			j = new TCookRole(p);
		}
		else if (job.equals("THost")) {
			j = new THostRole(p);
		}
		else if (job.equals("TWaiter")) {
			j = new TWaiterRole(p);
		}
		return j;
	}
	
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

    		PersonAgent p = new PersonAgent(name);
    		Role personRole= new DWaiterRole(p);
    		p.SetJob(personRole);
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
