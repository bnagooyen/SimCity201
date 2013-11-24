package simcity.test.mock;

import simcity.PersonAgent;
import simcity.test.mock.LoggedEvent;
import simcity.Market.InventoryBoyRole;
import simcity.Market.MOrder;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;

public class MockInventoryBoy extends Mock implements InventoryBoy{

	public MarketCashier mc;
	public EventLog log;
	public PersonAgent p;
	public InventoryBoyRole ib;
	
	public MockInventoryBoy(String name) {
		super(name);
		p = new PersonAgent("default");
		log = new EventLog();
		ib = new InventoryBoyRole(p);
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
	
}