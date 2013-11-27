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
		log.add(new LoggedEvent("Processed loan"));
	}
	
	public void msgLoanComplete(){
		log.add(new LoggedEvent("Processed loan"));
	}

	@Override
	public void msgGoHome(double pay) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Off duty pay = "+ pay));
	}

	@Override
	public void msgGoToLoanOfficerPosition() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("On duty"));
	}

}
