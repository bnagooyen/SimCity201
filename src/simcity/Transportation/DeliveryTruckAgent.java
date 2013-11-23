package simcity.Transportation;

import java.util.List;

import simcity.Market.MFoodOrder;
import simcity.Transportation.CarAgent.carState;
import simcity.interfaces.Car;
import simcity.interfaces.Person;
import agent.Agent;

public class DeliveryTruckAgent extends Agent implements Car{
	
	public Person driver=null;
	public String destination;
	public List<MFoodOrder> supply;
	
	public enum truckState
	{parked, receivedLocation, travelling, arrived};
	
	public truckState state;
	
	
	
	@Override
	public void msgGoToDestination(String location, Person person) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
}