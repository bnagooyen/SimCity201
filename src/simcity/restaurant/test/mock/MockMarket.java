package restaurant.test.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import restaurant.FoodOrder;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;

public class MockMarket extends Mock implements Market {

	public Cashier cashier;
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
	public void msgHereIsAnInventoryOrder(ArrayList<FoodOrder> orderToMarket,
			int id, Cashier c) {
			log.add(new LoggedEvent("Received msgHereIsAnInventoryOrder"));
			//if(marketFood.get())
			for(FoodOrder order: orderToMarket) {
				if(order.getVal()<=inventory) {
					log.add(new LoggedEvent("Can fullfill fully"));
				}
				else if(inventory>0) {
					log.add(new LoggedEvent("Can fullfill partially: "+inventory));
				}
				else {
					log.add(new LoggedEvent("Cannot fullfill order"));
				}
			}
	}

	@Override
	public void msgHereIsAPayment(double val, Cashier ca) {
		// TODO Auto-generated method stub
			log.add(new LoggedEvent("Received msgHereIsAPayment "+ val));
	}

}
