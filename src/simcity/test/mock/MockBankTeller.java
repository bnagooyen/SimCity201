package simcity.test.mock;

import simcity.interfaces.*;

public class MockBankTeller extends Mock implements BankTeller{

	public EventLog log;
	public MockBankTeller(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}
	
	public void msgMakeAccount(BankCustomer BC){
		LoggedEvent m = new LoggedEvent ("Make Account"); 
		log.add(m);
	}
	
	public void msgAccountCreated(int num){
		log.add(new LoggedEvent("Account created"));
	}
	
	public void msgDeposit(BankCustomer BC, int actNum, double amount){
		LoggedEvent m = new LoggedEvent ("Deposit"+ amount +"--"+actNum); 
		log.add(m);
	}
	
	public void msgWithdrawal(BankCustomer BC, int actNum, double amount){
		LoggedEvent m = new LoggedEvent ("Withdrawal"+ amount +"--"+actNum); 
		log.add(m);
	}
	
	public void msgTransactionProcessed(double finalAmount){
		log.add(new LoggedEvent("Transaction processed "+finalAmount));
	}
	
	public void msgIAmRobbingYou(BankRobber BR){
		LoggedEvent m = new LoggedEvent ("Being Robbed"); 
		log.add(m);
	}
	
	public void msgIShotYou(){
		LoggedEvent m = new LoggedEvent ("Got Shot"); 
		log.add(m);
	}
	
	public void msgGoHome(double pay){
		log.add(new LoggedEvent("Off duty pay = "+ pay));
	}
	
	public void msgGoToTellerPosition(){
		log.add(new LoggedEvent("On duty"));
	}

}
