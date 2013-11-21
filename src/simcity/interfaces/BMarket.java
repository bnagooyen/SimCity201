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
public interface BMarket {
	
	
       
	public abstract void setCashier(BCashier cashier);
	public abstract void msgPaytoMarket(BCheck check);
	public abstract void msgHereisCookOrder(BCook cook, List<BFood> orderFromCook);
	
}