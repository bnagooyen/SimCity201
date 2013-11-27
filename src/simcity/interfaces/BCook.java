package simcity.interfaces;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;

import agent.Agent;

import java.util.*;



/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface BCook {
       
	public abstract void setWaiter(BWaiter waiter);
	public abstract void msgHereisanOrder(BWaiter w, String choice, int tablenumber);
	public abstract void msgHereisCompletedOrder(List<BFood> foodOrder);
	public abstract void msgCannotCompleteOrder();
	
}