package simcity.test.mock;

import simcity.test.mock.LoggedEvent;
import simcity.Market.MOrder;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;

public class MockInventoryBoy extends Mock implements InventoryBoy{

	public MarketCashier mc;
	public EventLog log;
	
	public MockInventoryBoy(String name) {
		super(name);
		log = new EventLog();
		// TODO Auto-generated constructor stub
	}
	
	public void msgCheckInventory(MOrder o){
		LoggedEvent e = new LoggedEvent("Received msgCheckInventory from market cashier.");
		log.add(e);
		o.canGive = o.foodsNeeded;
		System.out.println("Sent");
		mc.msgCanGive(o);
	}
	
	public void msgGoHome(){
		
	}
	
}