package simcity.Transportation;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.Transportation.CarAgent.carState;
import simcity.interfaces.Car;
import simcity.interfaces.Cook;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketManager;
import simcity.interfaces.Person;
import agent.Agent;
import agent.Role;

public class DeliveryTruckAgent extends Agent implements Car{
	
//	public Person driver;
	public String destination;
	public List<MFoodOrder> supply;
	public Cook cook;
	public int check;
	public MarketManager manager;
	public MarketCashier mc;
	
	public enum truckState
	{available, parked, receivedLocation, travelling, arrived};
	
	public truckState state;
	
	public DeliveryTruckAgent(MarketManager m){
		manager = m;
		mc = null;
		cook = null;
		supply = null;
		destination = null;
		check = 0;
		state = truckState.available;
	}
	
	
	//Messages
	
	
	public void msgGoToDestination(MarketCashier cashier, List<MFoodOrder>deliver, String location, int bill, Cook c) {
		mc = cashier;
		supply = deliver;
		destination = location;
		state = truckState.receivedLocation;
		cook = c;
		check = bill;
		stateChanged();
		
	}
	
	@Override
	public void msgGoToDestination(String location, Person person) {
		// TODO Auto-generated method stub
		
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
				state=truckState.travelling;
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
		Do("Go To Location");
		//animation using destination
	}
	
	private void HaveArrived(){
		Do("Arrived at destination");
		cook.msgHereIsDelivery(supply, check, manager, mc);
		//animation to go home
		manager.msgBackFromDelivery();
		state = truckState.available;
	}
	
}