package simcity.interfaces;

import java.util.List;

import simcity.PersonAgent;
import simcity.Transportation.BusAgent;



public interface BusStop {

	public void msgWaitingForBus(PersonAgent p);
	public void msgAnyPassengers(BusAgent b);
	

}

