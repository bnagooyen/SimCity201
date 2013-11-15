package simcity.Market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.PersonAgent;
import simcity.restaurant.CashierRole;
import agent.Role;


public class MarketCashierRole extends Role{

	List<MOrder> orders =Collections.synchronizedList(new ArrayList<MOrder>());
	
	boolean active;
	double marketMoney;
	
	InventoryBoyRole ib;
	MarketManagerRole manager; 
	
	enum orderState{pending, inquiring, ready, given, paid};
	enum myState{arrived, working, goHome, unavailable};
	
	protected MarketCashierRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}
	
	//Messages
	public void msgOrder(Role r, List<MFoodOrder> foods, String building){
		orders.add(new MOrder(foods, building, r, orderState.pending));
		stateChanged();
	}
	
	public void msgOrder(Role r, List<MFoodOrder> foods, String building, CashierRole c){
		orders.add(new MOrder(foods, building, r, c, orderState.pending));
		stateChanged();
	}
	
	public void canGive(MOrder o){
		
	}
	
	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
