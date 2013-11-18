package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.PersonAgent;
import simcity.restaurant.FoodOrder;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import agent.Role;

public class InventoryBoyRole extends Role implements InventoryBoy{
	public List<MOrder> orders = Collections.synchronizedList(new ArrayList<MOrder>());
	public Map<String, Integer> inventory =Collections.synchronizedMap( new HashMap<String, Integer>());

	MarketCashier mc;
	MarketManager manager;
	
	enum state {arrived, working, leave, unavailable }
	state s;
	
	public EventLog log;

	
	public InventoryBoyRole(PersonAgent p) {
		super(p);
		log = new EventLog();
		
		// populate inventory
        inventory.put("Steak", 5);
        inventory.put("Chicken", 5);
        inventory.put("Salad", 5);
        inventory.put("Pizza", 5);

	}

	// messages
	public void msgCheckInventory(MOrder o) {
		orders.add(o);
		LoggedEvent e = new LoggedEvent("got an order to fulfill");
		log.add(e);
		stateChanged();
	}
	
	public void msgGoHome() {
		LoggedEvent e = new LoggedEvent("told to go home");
		log.add(e);
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
		s = state.working;
		manager.msgIAmHere(this, "inventory boy");
	}
	
	private void getOrder(MOrder o) {
		LoggedEvent e = new LoggedEvent("fulfilling an order");
		log.add(e);
		
		for(MFoodOrder f : o.foodsNeeded) {
			int currFood = inventory.get(f.type);
			if ( currFood > f.amount) {
				o.canGive.add(new MFoodOrder(f.type, f.amount));
				inventory.put(f.type, inventory.get(f.type) -f.amount);
			}
			else {
				o.canGive.add(new MFoodOrder(f.type, currFood));
				inventory.put(f.type, 0);
			}
		}
		mc.msgCanGive(o);
		orders.remove(o);
	}
	
	private void goHome() {
		LoggedEvent e = new LoggedEvent("going home");
		log.add(e);
		isActive = false;
		s = state.unavailable;
		DoGoHome();
	}

	// animation
	private void DoGoHome() {
		
	}

	// utilities
	public void setMarketCashier(MarketCashier c) {
		mc = c;
	}
	
}
