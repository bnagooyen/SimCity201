package simcity.BRestaurant;
import agent.Agent;
import agent.Role;

import java.util.*;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;







public class BCashierRole extends Role implements BCashier {

    

	BCookRole cook;
    public int totalMoney=500;
    public List<BCheck> myChecks = Collections.synchronizedList(new ArrayList<BCheck>());
    public List<BCustomer> myCustomers = Collections.synchronizedList(new ArrayList<BCustomer>());
    public List<marketCheck> marketChecks=new ArrayList<marketCheck>();
    
    public BCashierRole(PersonAgent p) {
		super(p);
		
		
    }
    
    public enum paidState {
		ReceivedByMarket, PaidByCashier};
		
	public paidState state=null;
    
	public class marketCheck{
    	public BCheck check;
    	public paidState state;
    	MarketCashier cashier;
    }
    
    
    public boolean pickAndExecuteAnAction() {

    	synchronized(myChecks){
    	for(BCheck thisCheck: myChecks) {
    		if(thisCheck.paidbyCustomer == true) {
    			takeMoney(thisCheck);
    			
    			return true;
    		}
    	}
    	}
    	synchronized(marketChecks){
    	for (marketCheck thisMarketCheck : marketChecks){
    		if (thisMarketCheck.state==paidState.ReceivedByMarket){
    			payMarket(thisMarketCheck);
    			return true;
    		}
    	}
    	}
    	
		return false;
    }
 
    //actions
    private void takeMoney(BCheck check){
    	totalMoney=totalMoney+check.price;
    	myChecks.remove(check);
    }  
    
    private void payMarket(marketCheck check){
    	totalMoney=totalMoney-check.check.price;
    	
    	check.state=paidState.PaidByCashier;
    	check.check.paidbyCashier=true;
    	check.cashier.msgHereIsPayment(cook, check.check.price);
    }
    
    //messages
    
    public void msgPayCheck(BCheck check){
    	for (BCheck thisCheck: myChecks){
    		if(thisCheck==check){
    			check.paidbyCustomer=true;
    			
    		}
    	}
    	stateChanged();
    }
/*********************************************************************************************/    
    public void msgHereisCheckfromMarket(double check, MarketCashier cashier) {
		// TODO Auto-generated method stub
		
	}
/********************************************************************************************/    
    public void msgHereisCheckfromMarket(BCheck check, MarketCashier market){
    	marketCheck thisMarketCheck=new marketCheck();
    	thisMarketCheck.check=check;
    	thisMarketCheck.state=paidState.ReceivedByMarket;
    	thisMarketCheck.cashier = market;
    	marketChecks.add(thisMarketCheck);
    	
    	
    	stateChanged();
    }
    
    public void msgCashierCheck(BCheck check, BCustomer customer)
    {
    	
    	myChecks.add(check);
    	myCustomers.add(customer);
    	for (BCustomer thiscustomer: myCustomers){
    		if (thiscustomer==customer){
    			thiscustomer.msgHereisYourCheck(check);
    		}
    	}
    	
    	
    	stateChanged();
    }
    
    public void msgRepeatCashierCheck(BCheck check, BCustomer customer)
    {
    	
    	myChecks.add(check);
    	
    	for (BCustomer thiscustomer: myCustomers){
    		if (thiscustomer==customer){
    			thiscustomer.msgHereisYourCheck(check);
    		}
    	}
    	
    	
    	stateChanged();
    }
    
  // utilities
    public void setCook(BCookRole c) {
    	cook = c;
    }

	
}
    