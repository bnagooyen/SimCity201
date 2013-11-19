package simcity.test.mock;

import simcity.interfaces.*;

public class MockBankCustomer extends Mock implements BankCustomer{

	public EventLog log;
	public MockBankCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}
	public void msgGoToLoanOfficer(BankLoanOfficer BL){
		log.add(new LoggedEvent("Going to officer"));
	}
	
	public void msgGoToTeller(BankTeller BT){
		log.add(new LoggedEvent("Going to teller"));
	}
	
	public void msgTransactionComplete(double amount){
		
	}
	
	public void msgHeresLoan(double amount){
		
	}
	
	public void msgLoanDenied(){
		
	}
	
	public void msgAccountMade(int AN){
		LoggedEvent m = new LoggedEvent ("New Account made: " + AN); 
		log.add(m);
	}
	@Override
	public void msgLeaveBank() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Bank closed"));
	}

}
