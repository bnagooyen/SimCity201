package simcity.BRestaurant;
import agent.*;
import simcity.BRestaurant.*;

import java.util.*;

import simcity.PersonAgent;
import simcity.interfaces.*;
import simcity.BRestaurant.*;







public class BCashierRole extends Role implements BCashier {

    

	BCookRole cook;
    public double totalMoney=500;
    public List<BCheck> myChecks = Collections.synchronizedList(new ArrayList<BCheck>());
    public List<BCustomer> myCustomers = Collections.synchronizedList(new ArrayList<BCustomer>());
    public List<marketCheck> marketChecks=Collections.synchronizedList(new ArrayList<marketCheck>());
    
    public enum MarketPaidState{notPaid, Paid};
    MarketPaidState checkState;
    
    public BCashierRole(PersonAgent p) {
		super(p);
		
		
    }
    
    
    
    public enum paidState {
		ReceivedByMarket, PaidByCashier};
		
	public paidState state=null;
    
	public class marketCheck{
    	public double check;
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
    	totalMoney=totalMoney-check.check;
    	
    	check.state=paidState.PaidByCashier;
    	
    	check.cashier.msgHereIsPayment(cook, check.check);
    }
    
    //messages
    
    public void msgPayCheck(BCheck check){
    	synchronized(myChecks) {
    	for (BCheck thisCheck: myChecks){
    		if(thisCheck==check){
    			check.paidbyCustomer=true;
    			
    		}
    	}
    	}
    	stateChanged();
    }

    public void msgHereisCheckfromMarket(double check, MarketCashier market){
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
    	synchronized(myCustomers) {
    	for (BCustomer thiscustomer: myCustomers){
    		if (thiscustomer==customer){
    			thiscustomer.msgHereisYourCheck(check);
    		}
    	}
    	}
    	
    	stateChanged();
    }
    
    public void msgRepeatCashierCheck(BCheck check, BCustomer customer)
    {
    	
    	myChecks.add(check);
    	synchronized(myCustomers) {
    	for (BCustomer thiscustomer: myCustomers){
    		if (thiscustomer==customer){
    			thiscustomer.msgHereisYourCheck(check);
    		}
    	}
    	}
    	
    	stateChanged();
    }
    
  // utilities
    public void setCook(BCookRole c) {
    	cook = c;
    }

	
}    