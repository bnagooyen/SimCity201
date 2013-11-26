package simcity.mockrole;

import java.util.List;

import agent.Role;
import simcity.PersonAgent;
import simcity.Market.MFoodOrder;
import simcity.Market.MOrder;
import simcity.Market.MarketCashierRole.myState;
import simcity.Market.MarketCashierRole.orderState;
import simcity.Market.MarketManagerRole;
import simcity.interfaces.Car;
import simcity.interfaces.Cook;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.restaurant.interfaces.Cashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.test.mock.MockInventoryBoy;

public class MockRoleInventoryBoy extends MockRole implements InventoryBoy{

	public EventLog log;
	public myState state;
	public MockInventoryBoy ib;
	public MarketManagerRole m;
	
	public MockRoleInventoryBoy(String name) {
		super(name);
		log = new EventLog();
		// TODO Auto-generated constructor stub
	}
	
	
	public void msgCheckInventory(MOrder o){
		LoggedEvent e = new LoggedEvent("Received msgCheckInventory from market cashier.");
		log.add(e);
		o.canGive = o.foodsNeeded;
	}
	
	public void msgGoHome(){
		LoggedEvent e = new LoggedEvent("Gone Home.");
		log.add(e);
	}

	@Override
	public void msgGoHome(double paycheck) {
		// TODO Auto-generated method stub
		
	}
	
}