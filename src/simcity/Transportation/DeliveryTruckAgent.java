package simcity.Transportation;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.Transportation.CarAgent.carState;
import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;
import simcity.interfaces.Car;
import simcity.interfaces.Cook;
import simcity.interfaces.DeliveryTruck;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.Person;
import simcity.interfaces.RestaurantCashier;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;
import agent.Agent;
import agent.Role;

public class DeliveryTruckAgent extends Agent implements DeliveryTruck{
	
//	public Person driver;
	public String destination;
	public List<MFoodOrder> supply;
	public Cook cook;
	public double check;
	public MarketManager manager;
	public MarketCashier mc;
	public RestaurantCashier cashier;
	public EventLog log;
	
	public enum truckState
	{available, parked, receivedLocation, travelling, arrived};
	
	public truckState state;
	
	public DeliveryTruckAgent(MarketManager m){
		log = new EventLog();
		manager = m;
		mc = null;
		cook = null;
		supply = null;
		destination = null;
		check = 0;
		state = truckState.available;
	}
	
	
	//Messages
	
	
	
	public void msgGoToDestination(MarketCashier cashier, List<MFoodOrder>deliver, String location, double bill, Cook c, RestaurantCashier rcashier) {
		mc = cashier;
		supply = deliver;
		destination = location;
		state = truckState.receivedLocation;
		cook = c;
		check = bill;
		this.cashier = rcashier;
		stateChanged();
		
	}
	

	@Override
	public void msgAtDestination() {
		state = truckState.arrived;
		stateChanged();
		
	}

	
	//SCHEDULER
	
		public boolean pickAndExecuteAnAction(){
			
			if(state==truckState.receivedLocation){
				goToLocation();
				return true;
			}
			if(state==truckState.arrived){
				HaveArrived();
				state=truckState.parked;
				return true;
			}
			return false;
		}
		
	//Actions
		
	private void goToLocation(){
		state=truckState.travelling;
		LoggedEvent e = new LoggedEvent("Going to destination");
		log.add(e);
		AlertLog.getInstance().logMessage(AlertTag.Market, "DeliveryTruck", "Going to location");
		Do("Go To Location");
		
/**************************** hack - need to add animation code *****************************************/
		state = truckState.arrived;
		//animation using destination
	}
	
	private void HaveArrived(){
		LoggedEvent e = new LoggedEvent("Arrived at destination");
		log.add(e);
		AlertLog.getInstance().logMessage(AlertTag.Market, "DeliveryTruck", "Arrived at destination");
		Do("Arrived at destination");
		cook.msgHereIsDelivery(supply, check, manager, mc);
		cashier.msgBillFromMarket(check, mc, manager);
		//animation to go home
		manager.msgBackFromDelivery();
		state = truckState.available;
	}
	
}