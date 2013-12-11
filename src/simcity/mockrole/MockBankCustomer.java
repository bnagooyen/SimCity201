package simcity.mockrole;

import simcity.PersonAgent;
import simcity.Bank.gui.BankCustomerGui;
import simcity.interfaces.*;
import simcity.test.mock.EventLog;
import simcity.test.mock.LoggedEvent;

public class MockBankCustomer extends MockRole implements BankCustomer{

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
		log.add(new LoggedEvent("Received loan"));
	}
	
	public void msgLoanDenied(){
		log.add(new LoggedEvent("Loan denied"));
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
	@Override
	public void msgAtTellerPos() {
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
	public PersonAgent getPerson() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setManager(BankManager m) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGui(BankCustomerGui BC) {
		// TODO Auto-generated method stub
		
	}


}
