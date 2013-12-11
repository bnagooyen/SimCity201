package simcity.test.mock;

import simcity.PersonAgent;
import simcity.Bank.gui.BankLoanGui;
import simcity.interfaces.*;
import simcity.mockrole.MockRole;

public class MockBankLoanOfficer extends MockRole implements BankLoanOfficer{

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

	@Override
	public void msgAnimationFinishedGoToCorner() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtLoanPos() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setManager(BankManager m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(BankLoanGui lOGui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PersonAgent getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

}
