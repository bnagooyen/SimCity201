package simcity.test.mock;

import simcity.interfaces.*;

public class MockBankLoanOfficer extends Mock implements BankLoanOfficer{

	public EventLog log;
	public MockBankLoanOfficer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}
	
	public void msgMakeAccount(BankCustomer BC){
		
	}
	
	public void msgAccountCreated(int num){
		
	}
	
	public void msgINeedALoan(BankCustomer BC, Integer AN, double amt, String Job){
		
	}
	
	public void msgLoanDenied(){
		
	}
	
	public void msgLoanComplete(){
		
	}

	@Override
	public void msgGoHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToLoanOfficerPosition() {
		// TODO Auto-generated method stub
		
	}

}
