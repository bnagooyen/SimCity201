package simcity.Bank;

import java.util.*;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyCustomer;
import simcity.Bank.BankManagerRole.MyTeller;
import simcity.Bank.BankTellerRole.accountState;
import simcity.Bank.BankTellerRole.bankTellerState;
//import simcity.Bank.BankManagerRole.MyEmployee;
import simcity.interfaces.*;
import agent.Role;

public class BankLoanOfficerRole extends Role implements BankLoanOfficer {
	
	//data
	BankManager manager;
	MyCustomer customer;
	enum bankLoanState { working, atManager, recieved, finished};
	enum accountState {none,requested,justMade,existing};
	bankLoanState state=bankLoanState.working;
	private static List<String> acceptableJobs = Collections.synchronizedList(new ArrayList<String>());


	class MyCustomer{
		BankCustomer BC;
		Integer accountNumber;
		String job;
		double amount;
		boolean accepted;
		accountState state=accountState.existing;
		
		MyCustomer(BankCustomer BankCust){
			BC=BankCust;
			accountNumber=null;
			job=null;
			amount=0.0;
			accepted=false;
		}
	}

	protected BankLoanOfficerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
		
		//Populate List of jobs to which we loan
		acceptableJobs.add("waiter");
		acceptableJobs.add("bankteller");
		acceptableJobs.add("bankloanofficer");
		acceptableJobs.add("host");
		acceptableJobs.add("landlord");
	}

	
	//Messages
	public void msgMakeAccount(BankCustomer BC){
		customer=new MyCustomer(BC);
		customer.state=accountState.none;
		stateChanged();
	}
	public void msgAccountCreated(int num){
		customer.state=accountState.justMade;
		stateChanged();
	}
	public void msgINeedALoan(BankCustomer BC, Integer AN, double amt, String J){
		if(customer==null) customer=new MyCustomer(BC);
		customer.accountNumber=AN;
		customer.job=J;
		customer.amount=amt;
		stateChanged();
	}
	public void msgLoanDenied(){
		state=bankLoanState.recieved;
		customer.accepted=false;
		stateChanged();
	}
	public void msgLoanComplete(){
		state=bankLoanState.recieved;
		customer.accepted=true;
		stateChanged();
	}
	
	@Override
	public void msgGoHome(double pay) {
		// TODO Auto-generated method stub
		myPerson.money+=pay;
		state=bankLoanState.finished;
		stateChanged();
	}


	@Override
	public void msgGoToLoanOfficerPosition() {
		// TODO Auto-generated method stub
		
	}
	
	//SCHEDULER	
	
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub	
		if(customer!=null && customer.state==accountState.none){
			createNewAccount();
			return true;
		}
		if(customer!=null && customer.state==accountState.justMade){
			hereIsYourAccount();
			return true;
		}
		if(customer!=null && state==bankLoanState.working){
			analyzeLoan();
			return true;
		}
		if(state==bankLoanState.recieved){
			completeLoan();
			return true;
		}
		return false;
	}

	
	//ACTIONS
	private void createNewAccount(){
		manager.msgCreateAccount("BankLoanOfficer");
		customer.state=accountState.requested;
	}
	private void hereIsYourAccount(){
		customer.BC.msgAccountMade(customer.accountNumber);
		customer.state=accountState.existing;
	}
	private void analyzeLoan(){
		for (String job : acceptableJobs) {
			if (customer.job.toLowerCase().equals(job)) {
				customer.accepted=true;
			}
		}
		if(customer.accepted){
			manager.msgNewLoan(customer.accountNumber,customer.amount);
			state=bankLoanState.atManager;
		}
		else{
			customer.BC.msgLoanDenied();
			customer=null;
			manager.msgAvailable(this);
		}
	}
	
	private void completeLoan(){
		if(customer.accepted){
			customer.BC.msgHeresLoan(customer.amount);
		}
		else{
			customer.BC.msgLoanDenied();
		}
		customer=null;
		manager.msgAvailable(this);
		state=bankLoanState.working;
	}

		
}
