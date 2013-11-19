package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketManager;
import agent.Role;

public class MarketCustomerRole extends Role implements MarketCustomer{

	private List<MFoodOrder> order = Collections.synchronizedList(new ArrayList<MFoodOrder>());
	private double myCheck;
	
	public enum customerState { talkToManager, timeToOrder, waiting, paying, done, storeClosed, pending }
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
	
	public void msgHereIsOrderAndCheck(List<MFoodOrder> canGive, double check) {
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
		if ( state == customerState.talkToManager) {
			goToManager();
			return true; 
		}
		if ( state == customerState.timeToOrder ) {
			orderFood();
			return true;
		}
		if ( state == customerState.paying ) {
			payCheck();
			return true;
		}
		if ( state == customerState.storeClosed ) {
			leaveStore();
			return true;
		}
		
		return false;
	}


	// actions
	private void goToManager() {
		manager.msgIAmHere(this, "customer");
	}
	
	private void orderFood() {
		state = customerState.waiting;
/************fix this message******************************************/
		mc.msgOrder(this, order, "");
	}

	private void payCheck() {
		state = customerState.done;
		mc.msgHereIsPayment(this, myCheck);
	}
	
	private void leaveStore() {
		state = customerState.done;
		super.isActive = false;
		DoGoHome();
	}
	
	// animation
	private void DoGoHome() {
		// TODO Auto-generated method stub
		
	}

	// utilities
	private void updateMyFood(List<MFoodOrder> canGive) {
	// TODO Auto-generated method stub
	
	}
	
	private double calculateBill(List<MFoodOrder> canGive) {
		// TODO Auto-generated method stub
		return 0;
	}

}