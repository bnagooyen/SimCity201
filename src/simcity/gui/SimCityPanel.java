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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import agent.Role;
import simcity.PersonAgent;
import simcity.PersonAgent.HomeType;
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
import simcity.LRestaurant.LWaiterNormalRole;
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
import simcity.Transportation.CarAgent;
import simcity.housing.LandlordRole;
import simcity.housing.RepairManRole;
import simcity.interfaces.BankManager;
import simcity.interfaces.Host;
import simcity.interfaces.MarketManager;
import simcity.interfaces.Person;

public class SimCityPanel extends JPanel{

	public static final int NUMAPTS = 12;
	public static final int NUMHOUSES = 15;
	public static final int NUMBANKS = 2;
	public static final int NUMMARKETS = 4;
	public int mktCounter=1;
	public int bankCounter = 1;
	public int houseNumCounter=1;
	public int ApartmentsPerLandlord;
	public int aptNumCounter=1;
	public char aptLetCounter='A';
	private SimCityGui gui;
	public Map<String, List<Location>> buildings = new HashMap<String, List<Location>>();
	public Map<String, List<Location>> busStops=new HashMap<String, List<Location>>(); //STILL BLANK
	private List<Location> restaurants = new ArrayList<Location>();
	private List<Location> banks = new ArrayList<Location>();
	private List<Location> markets = new ArrayList<Location>();
	
	private ArrayList<Person> people = new ArrayList<Person>();
	private ArrayList<MyLandlord> landlords = new ArrayList<MyLandlord>();
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
		
		
		
//		DWaiterRole w = null;
//		PersonAgent waiter = new PersonAgent("waiter", w);
//		w = new DWaiterRole(waiter);
		
//		PersonAgent cust = new PersonAgent("cust", null);
//		cust.setPanel(this);
//		
//		p6.startThread();
//		waiter.startThread();
//		cust.startThread();
		
		System.out.println(people.size());
		Timer timer;

		 class RemindTask extends TimerTask {
			 int counter= 0;
		        @Override
				public void run() {
		        	if(counter <25) {
		                 System.out.println("hour is " + counter);
		                 for(Person p: people) {
		                	 p.msgTimeUpdate(counter);
		                 }
		                 counter++;
		                 if (counter == 25) {
		                	 counter = 1;
		                 }
		        	}
		        }
		 }
	     timer = new Timer();
	     timer.schedule(new RemindTask(),
	                       0,        //initial delay
	                       1*12000);  //subsequent rate
	    
}
	   
	
	
	
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

		// house
		else if (job.equals("Landlord")) {
			j = new LandlordRole(p);
		}
		else if (job.equals("RepairMan")) {
			j = new RepairManRole(p);
		}
		
		// BRestaurant
		else if (job.equals("RestCashier2")) {
			j = new BCashierRole(p);
		}
		else if (job.equals("Cook2")) {
			j = new BCookRole(p);
		}
		else if (job.equals("Host2")) {
			j = new BHostRole(p);
		}
		else if (job.equals("Waiter2")) {
			j = new BWaiterRole(p);
		}
		
		// DRestaurant
		else if (job.equals("RestCashier3")) {
			j = new DCashierRole(p);
		}
		else if (job.equals("Cook3")) {
			j = new DCookRole(p);
		}
		else if (job.equals("Host3")) {
			j = new DHostRole(p);
		}
		else if (job.equals("Waiter3")) {
			j = new DWaiterRole(p);
		}
		
		// Drew_Restaurant
		else if (job.equals("RestCashier1")) {
			j = new Drew_CashierRole(p);
		}
		else if (job.equals("Cook1")) {
			j = new Drew_CookRole(p);
		}
		else if (job.equals("Host1")) {
			j = new Drew_HostRole(p);
		}
		else if (job.equals("Waiter1")) {
			j = new Drew_WaiterRole(p);
		}
		
		// KRestaurant
		else if (job.equals("RestCashier4")) {
			j = new KCashierRole(p);
		}
		else if (job.equals("Cook4")) {
			j = new KCookRole(p);
		}
		else if (job.equals("Host4")) {
			j = new KHostRole(p);
		}
		else if (job.equals("Waiter4")) {
			j = new KWaiterNormalRole(p);
		}
		else if (job.equals("WaiterSharedData4")) {
			j = new KWaiterSharedDataRole(p);
		}
		
		// LRestaurant
		else if (job.equals("RestCashier5")) {
			j = new LCashierRole(p);
		}
		else if (job.equals("Cook5")) {
			j = new LCookRole(p);
		}
		else if (job.equals("Host5")) {
			j = new LHostRole(p);
		}
		else if (job.equals("Waiter5")) {
			j = new LWaiterNormalRole(p);
		}
		
		// TTRestaurant
		else if (job.equals("RestCashier6")) {
			j = new TCashierRole(p);
		}
		else if (job.equals("Cook6")) {
			j = new TCookRole(p);
		}
		else if (job.equals("Host6")) {
			j = new THostRole(p);
		}
		else if (job.equals("Waiter6")) {
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
	
	public void LoadScenario(String type) {
		people.clear();
		landlords.clear();
		houseNumCounter=1;
		aptNumCounter=1;
		aptLetCounter='A';
		
		try {
			in  = new Scanner(new FileReader("config"+File.separator+type+".txt"));
//			System.out.println(in.next());
			in.next();
			int numItems = in.nextInt();
			//clear input template;
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			in.next();
			for(int i=0; i<numItems; i++) {
				PersonAgent p = new PersonAgent(in.next());
				p.setMoney(in.nextDouble());
				String job = in.next().trim();
				Role myJob = jobFactory(job,p);
				p.SetJob(myJob);
				if(job.equals("Landlord")) {
					landlords.add(new MyLandlord(p));
//					System.out.println("landlord added");
				}
				
				//add to map
				if(job.equals("BankManager") && bankCounter<=NUMBANKS) {
					banks.add(new Bank("Bank"+Integer.toString(bankCounter), myJob));
					bankCounter++;
				}
				else if(job.equals("MarketManager") && mktCounter<=NUMMARKETS) {
					markets.add(new Market("Market"+Integer.toString(mktCounter), myJob));	
					mktCounter++;
				}
				else if(job.equals("Host1")) {
					restaurants.add(new Restaurant("Restaurant1", myJob, "normal"));
				}
				else if(job.equals("Host2")) {
					restaurants.add(new Restaurant("Restaurant2", myJob, "normal"));
				}
				else if(job.equals("Host3")) {
					restaurants.add(new Restaurant("Restaurant3", myJob, "normal"));
				}
				else if(job.equals("Host4")) {
					restaurants.add(new Restaurant("Restaurant4", myJob, "normal"));
				}
				else if(job.equals("Host5")) {
					restaurants.add(new Restaurant("Restaurant5", myJob, "normal"));
				}
				else if(job.equals("Host6")) {
					restaurants.add(new Restaurant("Restaurant6", myJob, "normal"));
				}
				//for load balancing waiters when they are added on through gui
				else if(job.equals("Waiter1")) {
					for(Location r: restaurants) {
						if(r.name.equals("Restaurant1")) {
							//System.out.println(r.numEmployees);
							r.numEmployees++;
							break;
						}
					}
				}
				else if(job.equals("Waiter2")) {
					for(Location r: restaurants) {
						if(r.name.equals("Restaurant2")){
							r.numEmployees++;
							break;
						}	
					}
				}
				else if(job.equals("Waiter3")) {
					for(Location r: restaurants) {
						if(r.name.equals("Restaurant3")){
							r.numEmployees++;
							break;
						}		
					}
				}
				else if(job.equals("Waiter4")) {
					for(Location r: restaurants) {
						if(r.name.equals("Restaurant4")){
							r.numEmployees++;
							break;
						}		
					}
				}
				else if(job.equals("Waiter5")) {
					for(Location r: restaurants) {
						if(r.name.equals("Restaurant5")) {
							r.numEmployees++;
							break;
						}		
					}
				}
				else if(job.equals("Waiter6")) {
					for(Location r: restaurants) {
						if(r.name.equals("Restaurant6")) {
							r.numEmployees++;
							break;
						}
					}
				}
				
				boolean hasACar = in.nextBoolean();
				if(hasACar) {
					p.setCar(new CarAgent());
				}
				String home = in.next();
				
				if(home.equals("House") && houseNumCounter<=NUMHOUSES) {
					p.SetHome(HomeType.house);
					p.SetHouseLocation(houseNumCounter);
					houseNumCounter++;
					
				}
				else if(aptNumCounter<=aptNumCounter) {
					p.SetHome(HomeType.apartment);
					p.SetApatmentLocation(aptNumCounter, aptLetCounter);
					if(aptLetCounter=='C') {
						aptLetCounter='A';
						aptNumCounter++;
					}
					else {
						aptLetCounter++;
					}
				}
				else {
					p.SetHome(HomeType.homeless);
				}
				people.add(p);
			}
			
			ApartmentsPerLandlord = NUMAPTS/landlords.size();
			for(Person p: people) {
				if(p.GetHomeState()==HomeType.apartment) {
					landlords.get((p.getAptNum()-1)/ApartmentsPerLandlord).tenants.add(p);
				}
			}
			
			buildings.put("Bank", banks);
			buildings.put("Market", markets);
			buildings.put("Restaurant", restaurants);
			
			for (Person p: people) {
				p.msgSetBuildingDirectory(buildings);
				p.msgSetBusDirectory(busStops);
			}
//			
			for(MyLandlord l: landlords) {
//				System.out.println(l.tenants.size());
//				l.p.SetTenants(l.tenants);
			}
//			
			//TEST CODE
//			System.out.println(people.size());
//			for(Person person: people) {
//				HomeType h= person.GetHomeState();
//				if(h==HomeType.house) {
//					System.out.println("house");
//					System.out.println(person.getHouseNum());
//				}
//				if(h==HomeType.apartment) {
//					System.out.println("apartment");
//					System.out.print(person.getAptNum());
//					System.out.println(person.getAptLet());
//				}
//			}
//			int i=1;
//			for(Person person: people) {
//				System.out.print(i);
//				i++;
//				System.out.println(person.GetJob().isActive);
//			}
//			System.out.println(landlords.size());
			
			in.close();
			gui.addPeople(people);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  public void addPerson(String name, String role, double moneyVal, String houseOrApt) {

    		PersonAgent p = new PersonAgent(name);
    		int minWaiterIndex=0;
//    		for(Location r: restaurants) {
//    			System.err.println(r.numEmployees);
//    		}
    		if(role.equals("Restaurant Waiter")) {
    			for (int i=0; i<restaurants.size(); i++) {
    				if(restaurants.get(i).numEmployees<restaurants.get(minWaiterIndex).numEmployees)
    					minWaiterIndex=i;		
    			}	
    		}
    		//System.out.println(Integer.parseInt(restaurants.get(minWaiterIndex).name.substring(restaurants.get(minWaiterIndex).name.length()-1)));
    		int restAssignedTo = Integer.parseInt(restaurants.get(minWaiterIndex).name.substring(restaurants.get(minWaiterIndex).name.length()-1));
    		Role myJob=jobFactory("Waiter"+Integer.toString(restAssignedTo), p);
    		//System.out.println(myJob);
    		for(Location r: restaurants) {
    			if(r.name.equals("Restaurant"+Integer.toString(restAssignedTo))) {
    				r.numEmployees++;
    				break;
    			}
    		}
    		//System.out.println(restAssignedTo);
    		p.SetJob(myJob);
//    		System.out.println(p.getJob());
    		if(houseOrApt.equals("House") && houseNumCounter<=NUMHOUSES) {
				p.SetHome(HomeType.house);
				//System.err.println(houseNumCounter);
				p.SetHouseLocation(houseNumCounter);
				houseNumCounter++;
				
			}
			else if(aptNumCounter<=aptNumCounter) {
				p.SetHome(HomeType.apartment);
				p.SetApatmentLocation(aptNumCounter, aptLetCounter);
				//System.err.println(aptNumCounter + "  "+ aptLetCounter);
				if(aptLetCounter=='C') {
					aptLetCounter='A';
					aptNumCounter++;
				}
				else {
					aptLetCounter++;
				}
			}
			else {
				p.SetHome(HomeType.homeless);
			}
 
    		//give person directory
    		p.msgSetBuildingDirectory(buildings);
			p.msgSetBusDirectory(busStops);
			
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
		public String name;
		public int numEmployees=0;
	}
	
	public class Restaurant extends Location{
		Role host;
		String foodType;
		
		public Restaurant(String n, Role h, String type) {
			name = n;
			host = h;
			foodType = type;
		}
	}
	
	public class Bank extends Location{
		Role manager;
		
		public Bank(String n, Role myJob){
			name = n;
			manager = myJob;
		}
	}
	
	public class Market extends Location {
		Role manager;
		
		public Market(String n, Role m) {
			name = n;
			manager = m;
		}
	}
	
	public class MyLandlord {
		Person p;
		List<Person> tenants= new ArrayList<Person>();
		public MyLandlord(Person per) {
			p=per;
		}
	}
}
