package simcity.interfaces;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;



public interface BWaiter {
	
	public abstract void msgSitCustomerAtTable(BCustomer customer, int tableNum);
	public abstract void msgIWantFood(BCustomer cust);
	public abstract void msgBreakAllowed();
	public abstract void msgBreakNotAllowed();
	public abstract void msgLeavingTable(BCustomer cust);
	public abstract void msgAtTable();
	public abstract void msgHereismyChoice(BCustomer customer, String choice);
	public abstract void msgOrderisReady(int tablenumber, String choice);
	public abstract void msgIWantCheck(BCustomer customer);
	public abstract void msgCustomerNoMoney();
	public abstract void msgReadytoOrder(BCustomer customer);
	public abstract void setCashier(BCashier cashier);
	public abstract void msgOutOfThatFood(int tablenumber);
       
	
}