package simcity.test.mock;

import java.util.ArrayList;

import simcity.DRestaurant.DFoodOrder;
import simcity.interfaces.DCashier;
import simcity.interfaces.Market;
import simcity.mockrole.MockRole;

public class MockMarket extends MockRole implements Market {

	public DCashier cashier;
	public EventLog log = new EventLog();
	int inventory;
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public void setInventory(int d) {
		inventory = d;
	}
	
	public int getInventory() {
		return inventory;
	}

	@Override
	public void msgHereIsAnInventoryOrder(ArrayList<DFoodOrder> orderToMarket,
			int id, DCashier c) {

	}

	@Override
	public void msgHereIsAPayment(double val, DCashier ca) {
		// TODO Auto-generated method stub
			log.add(new LoggedEvent("Received msgHereIsAPayment "+ val));
	}


}
