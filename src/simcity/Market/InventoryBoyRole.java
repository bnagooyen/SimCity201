package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import simcity.PersonAgent;
import simcity.restaurant.FoodOrder;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import agent.Role;

public class InventoryBoyRole extends Role implements InventoryBoy{
	List<MOrder> orders = Collections.synchronizedList(new ArrayList<MOrder>());
	Map<String, Integer> inventory;
	
	MarketCashier mc;
	MarketManager manager;
	
	enum state {arrived, working, leave, unavailable }
	state s;
	
	
	public InventoryBoyRole(PersonAgent p) {
		super(p);
	}

	// messages
	public void msgCheckInventory(MOrder o) {
		orders.add(o);
		stateChanged();
	}
	
	public void msgGoHome() {
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
			o.foodsNeeded.remove(f);
		}
		mc.msgCanGive(o);
		orders.remove(o);
	}
	
	private void goHome() {
		super.isActive = false;
		s = state.unavailable;
		DoGoHome();
	}

	// animation
	private void DoGoHome() {
		
	}

	
}
