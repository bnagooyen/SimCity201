package simcity.test.mock;

import simcity.interfaces.*;

public class MockBankRobber extends Mock implements BankRobber{

	EventLog log;
	public MockBankRobber(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}
	public void msgGoToTeller(BankTeller t){
		
	}
	
	public void msgHereIsMoney(double amount){
		
	}
	
	public void msgIRefuseToPay(){
		
	}
	
	public void msgIShotYou(){
		
	}

}
