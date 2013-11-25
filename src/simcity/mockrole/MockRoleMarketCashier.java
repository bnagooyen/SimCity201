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
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.restaurant.interfaces.Cashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import simcity.test.mock.MockInventoryBoy;

public class MockRoleMarketCashier extends MockRole implements MarketCashier{

	private String name;
	public EventLog log;
	public enum myState{arrived, working, goHome, unavailable};
	public myState state;
	public MockInventoryBoy ib;
	public MarketManagerRole m;
	
	public MockRoleMarketCashier(String name, PersonAgent p) {
		super(name, p);
		this.name = name;
		log = new EventLog();
	}
	
	public void msgOrder(MarketCustomer c, List<MFoodOrder> foods,String building) {
		LoggedEvent e = new LoggedEvent("got customer's order");
		log.add(e);
		
		double check = 0;
		for(MFoodOrder o :foods) {
			check += o.amount * o.price;
		}
		
		c.msgHereIsOrderAndCheck(foods, check);
	}

	public void msgOrder(Cook cook, List<MFoodOrder> foods, String building) {
		LoggedEvent e = new LoggedEvent("Recieved cook's order");
		log.add(e);
		
	}
	
	public void msgOrder(MarketCustomer c,String building) {
		LoggedEvent e = new LoggedEvent("got customer's order");
		log.add(e);
		
		
	}

	public void msgCanGive(MOrder o) {
		LoggedEvent e = new LoggedEvent("got order back of what we can give customer");
		log.add(e);
	}

	public void msgHereIsPayment(Role r, double payment) {
		LoggedEvent e = new LoggedEvent("received payment");
		log.add(e);
	}

	public void msgGoHome() {
		LoggedEvent e = new LoggedEvent("Gone Home");
		log.add(e);
		
	}
	
	public boolean pickAndExecuteAnAction() { //should not be called
		if(state == myState.goHome){
			LoggedEvent e = new LoggedEvent("Error if called");
			log.add(e);
		}
		
		if(state == myState.arrived){
			LoggedEvent e = new LoggedEvent("Error if called");
			log.add(e);
		}
		
//		synchronized(orders){
//			for(MOrder o: orders){
//				LoggedEvent e = new LoggedEvent("Error if called");
//				log.add(e);
//			}
//		}
//		
//		synchronized(orders){
//			for(MOrder o: orders){
//				LoggedEvent e = new LoggedEvent("Error if called");
//				log.add(e);
//			}
//		}
//		
//		synchronized(orders){
//			for(MOrder o: orders){
//				LoggedEvent e = new LoggedEvent("Error if called");
//				log.add(e);
//			}
//		}
		
		return false;
	}

	@Override
	public void msgGoHome(double paycheck) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCanGive(Car currCar, MOrder o) {
		// TODO Auto-generated method stub
		
	}
	
}