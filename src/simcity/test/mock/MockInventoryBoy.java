package simcity.test.mock;

import simcity.Market.MOrder;
import simcity.interfaces.InventoryBoy;
import simcity.interfaces.MarketCashier;

public class MockInventoryBoy extends Mock implements InventoryBoy{

	public MarketCashier mc;
	public EventLog log;
	
	public MockInventoryBoy(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void msgCheckInventory(MOrder o){
		
	}
	
	public void msgGoHome(){
		
	}
	
}