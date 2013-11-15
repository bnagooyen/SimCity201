package simcity;

import java.util.List;

import simcity.PersonAgent;
import agent.Role;

public class MarketCustomerRole extends Role {

	private List<FoodOrder> order;
	private double myCheck;
	
	public enum customerState { timeToOrder, waiting, paying, done, storeClosed, pending }
	private customerState state;
	
	private MarketCashier mc;
	private MarketManager manager;
	
	public MarketCustomerRole(PersonAgent p) {
		super(p); 
	}
	
	// messages
	
	public void msgGoToCashier(MarketCashier c) {
		mc = c;
		state = customerState.timeToOrder;	
		stateChanged();
	}
	
	public void msgHereIsOrderAndCheck(List<FoodOrder> canGive, double check) {
		myCheck = calculateBill(canGive);
		updateMyFood(canGive);
		state = customerState.paying;
		stateChanged();
	}

	public void msgMarketClosed() {
		state = customerState.storeClosed;
		stateChanged();
	}
	
	//scheduler
	public boolean pickAndExecuteAnAction() {
		if ( state == customerState.timeToOrder ) {
			orderFood();
			return true;
		}
		if ( state == customerState.paying ) {
			
		}
		return false;
	}

	
	// actions

	private void orderFood() {
		// TODO Auto-generated method stub
		
	}

	

	// utilities
	private void updateMyFood(List<FoodOrder> canGive) {
	// TODO Auto-generated method stub
	
	}
	
	private double calculateBill(List<FoodOrder> canGive) {
		// TODO Auto-generated method stub
		return 0;
	}

}