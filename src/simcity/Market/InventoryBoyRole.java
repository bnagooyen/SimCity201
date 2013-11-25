package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.PersonAgent;
import simcity.DRestaurant.DFoodOrder;
import simcity.Transportation.CarAgent;
import simcity.interfaces.Car;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.Market.gui.IBGui;
import agent.Role;

public class InventoryBoyRole extends Role implements InventoryBoy{
	public List<MOrder> orders = Collections.synchronizedList(new ArrayList<MOrder>());
	public Map<String, Integer> inventory =Collections.synchronizedMap( new HashMap<String, Integer>());
	public List<Car> cars = Collections.synchronizedList(new ArrayList<Car>());
	
	private Semaphore gettingFood = new Semaphore(0, true);

	MarketCashier mc;
	MarketManager manager;
	IBGui ibGui;

	PersonAgent p;
	
	enum state {arrived, working, leave, unavailable }
	state s;
	
	public EventLog log;

	
	public InventoryBoyRole(PersonAgent p) {
		super(p);
		this.p = p;
		log = new EventLog();
		
		// populate inventory
        inventory.put("Steak", 20);
        inventory.put("Chicken", 20);
        inventory.put("Salad", 20);
        inventory.put("Pizza", 20);
        
        for(int i = 0; i<20; i++){
        	cars.add(new CarAgent());
        }

	}

	// messages
	public void msgGotFood() {
		gettingFood.release();
	}
	public void msgCheckInventory(MOrder o) {
		Do("Got an order to fulfill");
		orders.add(o);
		LoggedEvent e = new LoggedEvent("got an order to fulfill");
		log.add(e);
		stateChanged();
	}
	
	public void msgGoHome(double paycheck) {
		Do("Told to go home");
		LoggedEvent e = new LoggedEvent("told to go home");
		log.add(e);
		p.money += paycheck;
		s = state.leave;
		stateChanged();
	}
	
	
	// scheduler
	public boolean pickAndExecuteAnAction() {
		if (s == state.arrived) {
			tellManager();
			return true;
		}
		
		synchronized(orders) {
			for (MOrder o : orders) {
				getOrder(orders.get(0));
				return true;
			}
		}
		
		if ( s == state.leave ) {
			goHome();
			return true;
		}
		
		return false;
	}

	private void tellManager() {
		Do("Telling manager that I can work");
		s = state.working;
		manager.msgIAmHere(this, "inventory boy");
	}
	
	private void getOrder(MOrder o) {
		Do("Going to the back. Fulfilling an order.");
		LoggedEvent ev = new LoggedEvent("fulfilling an order");
		log.add(ev);
		
		if(purpose.equals("car")){ //customer ordered a car
			Car currCar = cars.get(0);
			ibGui.DoGoToCashier();
			try {
				gettingFood.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mc.msgCanGive(currCar, o);
			cars.remove(currCar);
			//animation for car
		}
		
		if(o.foodsNeeded != null){
			synchronized(o.foodsNeeded){
				if(purpose.equals("food")){
					for(MFoodOrder f : o.foodsNeeded) {
						int currFood = inventory.get(f.type);
						if (currFood > f.amount) {
							o.canGive.add(new MFoodOrder(f.type, f.amount));
							inventory.put(f.type, inventory.get(f.type) -f.amount);
						}
						else {
							o.canGive.add(new MFoodOrder(f.type, currFood));
							inventory.put(f.type, 0);
						}
						if(f.type.equals("Steak")) {
							ibGui.DoGetSteak();
							try {
								gettingFood.acquire();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						else if(f.type.equals("Chicken")) {
							ibGui.DoGetChicken();try {
								gettingFood.acquire();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						else if(f.type.equals("Salad")) { 
							ibGui.DoGetSalad();try {
								gettingFood.acquire();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}	
						else if (f.type.equals("Pizza")) {
							ibGui.DoGetPizza();
							try {
								gettingFood.acquire();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			
			ibGui.DoGoToCashier();
			try {
				gettingFood.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mc.msgCanGive(o);
			orders.remove(o);
			ibGui.DoGoToWaitingPos();
			}
		}
	}
	
	private void goHome() {
		Do("Going home");
		LoggedEvent e = new LoggedEvent("going home");
		log.add(e);
		isActive = false;
		s = state.unavailable;
		DoGoHome();
	}

	// animation
	private void DoGoHome() {
		ibGui.DoGoHome();

	}

	// utilities
	public void setMarketCashier(MarketCashier c) {
		mc = c;
	}
	
	public void setMarketManager(MarketManager m) {
		this.manager = m;
	}
	public void setGui(IBGui g) {
		ibGui = g;
	}
}
