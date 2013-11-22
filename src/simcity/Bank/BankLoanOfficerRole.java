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
	public BankManager manager;
	MyCustomer customer;
	public enum bankLoanState { working, atManager, waitingForLoanRequest, recieved, finished};
	public enum accountState {none,requested,created, exists, loanRequested, loanApproved, loanRequestSent};
	bankLoanState state=bankLoanState.working;
	private static List<String> acceptableJobs = Collections.synchronizedList(new ArrayList<String>());


	public class MyCustomer{
		BankCustomer BC;
		Integer accountNumber;
		String job;
		double amount;
		boolean accepted;
		public accountState state=accountState.none;
		
		MyCustomer(BankCustomer BankCust){
			BC=BankCust;
			accountNumber=null;
			job=null;
			amount=0.0;
			accepted=false;
		}
	}

	public BankLoanOfficerRole(PersonAgent p) {
		super(p);
		startHour=8;
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
		Do("Request to make an account");
		customer=new MyCustomer(BC);
		customer.state=accountState.none;
		stateChanged();
	}
	public void msgAccountCreated(int num){
		Do("Received created account");
		customer.accountNumber=num;
		customer.state=accountState.created;
		stateChanged();
	}
	public void msgINeedALoan(BankCustomer BC, Integer AN, double amt, String J){
		Do("Request for a loan");
		if(customer==null) customer=new MyCustomer(BC);
		customer.accountNumber=AN;
		customer.job=J;
		customer.amount=amt;
		customer.state=accountState.loanRequested;
		stateChanged();
	}
	public void msgLoanDenied(){
		Do("Request for loan was denied");
		state=bankLoanState.recieved;
		customer.accepted=false;
		stateChanged();
	}
	public void msgLoanComplete(){
		Do("Request for loan was completed");
		state=bankLoanState.recieved;
		customer.accepted=true;
		stateChanged();
	}
	
	@Override
	public void msgGoHome(double pay) {
		Do("Going home");
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
		if(customer!=null && customer.state==accountState.none && state==bankLoanState.working){
			createNewAccount();
			return true;
		}
		if(customer!=null && customer.state==accountState.created){
			hereIsYourAccount();
			return true;
		}
		if(customer!=null && customer.state==accountState.loanRequested && state==bankLoanState.waitingForLoanRequest){
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
		Do("Creating a new account");
		manager.msgCreateAccount("BankLoanOfficer");
		customer.state=accountState.requested;
		state=bankLoanState.atManager;
    }
	private void hereIsYourAccount(){
		Do("Giving customer account");
		customer.BC.msgAccountMade(customer.accountNumber);
		customer.state=accountState.exists;
		state=bankLoanState.waitingForLoanRequest;
	}
	private void analyzeLoan(){
		for (String job : acceptableJobs) {
			if (customer.job.toLowerCase().equals(job)) {
				customer.accepted=true;
			}
		}
		if(customer.accepted){
			Do("Analyzed loan. Accepted");
			manager.msgNewLoan(customer.accountNumber,customer.amount);
			state=bankLoanState.atManager;
			customer.state=accountState.loanRequestSent;
		}
		else{
			Do("Analzyed loan. Denied");
			customer.BC.msgLoanDenied();
			customer=null;
			manager.msgAvailable(this);
			state=bankLoanState.working;
		}
	}
	
	private void completeLoan(){
		if(customer.accepted){
			Do("Giving completed loan");
			customer.BC.msgHeresLoan(customer.amount);
		}
		else{
			Do("Loan was denied");
			customer.BC.msgLoanDenied();
		}
		customer=null;
		manager.msgAvailable(this);
		state=bankLoanState.working;
	}

	//utilites
	public MyCustomer GetCustomer() {
		return customer;
	}
	public bankLoanState getState() {
		return state;
	}
}
