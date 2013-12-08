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
//<<<<<<< HEAD
//import simcity.Transportation.CarAgent;
import simcity.gui.SimCityGui;
//=======
////import simcity.Transportation.CarAgent;

import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Car;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.Market.gui.IBGui;
import simcity.Market.gui.MCashierGui;
//import simcity.PersonAgent.EnergyState;
//import simcity.PersonAgent.LocationState;
import agent.Role;

public class InventoryBoyRole extends Role implements InventoryBoy{
	public List<MOrder> orders = Collections.synchronizedList(new ArrayList<MOrder>());
	public Map<String, Integer> inventory =Collections.synchronizedMap( new HashMap<String, Integer>());
	public List<Car> cars = Collections.synchronizedList(new ArrayList<Car>(20));
	
	private Semaphore gettingFood = new Semaphore(0, true);

	MarketCashier mc;
	MarketManager manager;
	IBGui ibGui;

	PersonAgent p;
	
	public enum state {arrived, working, leave, unavailable }
	public state s;
	
	public EventLog log;

	private SimCityGui gui;
	
	public InventoryBoyRole(SimCityGui gui) {
		super();
		this.gui = gui;
		//this.p = p;
		log = new EventLog();
		
		// populate inventory
        inventory.put("Steak", 20);
        inventory.put("Chicken", 20);
        inventory.put("Salad", 20);
        inventory.put("Pizza", 20);
        
//        for(int i = 0; i<20; i++){
//        	cars.add(new CarAgent());
//        }
        s = state.arrived;
		startHour = 11;

	}

	// messages
	public void msgGotFood() {
		gettingFood.release();
	}
	public void msgCheckInventory(MOrder o) {
		AlertLog.getInstance().logInfo(AlertTag.Market, "InventoryBoyRole", "Got an order to fulfill");
		Do("Got an order to fulfill");
		orders.add(o);
		LoggedEvent e = new LoggedEvent("got an order to fulfill");
		log.add(e);
		stateChanged();
	}
	
	public void msgGoHome(double paycheck) {
		AlertLog.getInstance().logInfo(AlertTag.Market, "InventoryBoyRole", "Told to go home");
		Do("Told to go home");
		LoggedEvent e = new LoggedEvent("told to go home");
		log.add(e);
//		myPerson.money += paycheck;
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
		if(ibGui == null) {
			ibGui = new IBGui(this);
			gui.myPanels.get("Market 1").panel.addGui(ibGui);
		}
		ibGui.setPresent(true);
		
		AlertLog.getInstance().logInfo(AlertTag.Market, "InventoryBoyRole", "Telling manager that I can work");
		Do("Telling manager that I can work");
		s = state.working;
		manager.msgIAmHere(this, "inventory boy");
	}
	
	private void getOrder(MOrder o) {
		AlertLog.getInstance().logInfo(AlertTag.Market, "InventoryBoyRole", "Going to the back. Fulfilling an order");
		Do("Going to the back to fulfill an order.");
		LoggedEvent ev = new LoggedEvent("fulfilling an order");
		log.add(ev);
		
		

			//animation for car
		
			if(o.foodsNeeded != null){
				synchronized(o.foodsNeeded){
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
						else if (f.type.equals("Car")) {
							Car currCar = cars.get(0);
							ibGui.DoGetCar();
							try {
								gettingFood.acquire();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mc.msgCanGive(currCar, o);
							cars.remove(currCar);
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
		AlertLog.getInstance().logInfo(AlertTag.Market, "InventoryBoyRole", "Going home");
		Do("Going home");
		LoggedEvent e = new LoggedEvent("going home");
		log.add(e);
		isActive = false;
		s = state.arrived;
//		
//		myPerson.energyState = EnergyState.tired;
//		myPerson.locationState = LocationState.Out;
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
