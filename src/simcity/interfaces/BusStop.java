package simcity.interfaces;

import java.util.List;

import simcity.PersonAgent;
import simcity.Transportation.BusAgent;



public interface BusStop {

	public abstract void msgWaitingForBus(PersonAgent p);
	public abstract void msgAnyPassengers(BusAgent b);
	

}

