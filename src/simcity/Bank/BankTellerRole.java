package simcity.Bank;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyCustomer;
import simcity.Bank.BankManagerRole.MyEmployee;
import simcity.interfaces.*;
import agent.Role;

public class BankTellerRole extends Role implements BankTeller {
	
	//data
	BankManager manager;
	BankRobber robber;
	MyCustomer customer;
	Double requested=0.00;
	Double transacted=0.00;
	
	enum bankTellerState { working, success, error };
	bankTellerState state=bankTellerState.working;
	
	enum accountState {none,requested,justMade,existing};
	
	class MyCustomer{
		BankCustomer BC;
		int accountNumber;
		accountState state=accountState.existing;
		
		MyCustomer(BankCustomer BankCust){
			BC=BankCust;
		}
	}
		
	protected BankTellerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Messages
	public void makeAccount(BankCustomer BC){
		customer=new MyCustomer (MyCustomer(BC));
		customer.state=accountState.justMade;
	}
	public void accountCreated(int num){
		customer.state=accountState.justMade;
	}
	public void Deposit(BankCustomer BC, int actNum, double amount){
		if(customer==null) customer= new MyCustomer (MyCustomer(BC));
		customer.accountNumber=actNum;
		requested=amount;
	}
	public void Withdrawal(BankCustomer BC, int actNum, double amount){
		if(customer==null) customer= new MyCustomer (MyCustomer(BC));
		customer.accountNumber=actNum;
		requested=-amount;
	}
	public void transactionProcessed(double finalAmount){
		transacted=finalAmount;
	}
	public void iAmRobbingYou(BankRobber BR){
		robber=BR;
	}
	public void iShotYou(){
		//Tell person he was shot, Message? Directly add to state?
	}
	
	//SCHEDULER
	if(customer!=null && customer.state==accountState.none){
		createNewAccount();
	}
	if(customer!=null && customer.state==accountState.justMade){
		hereIsYourAccount();
	}
	if(requested!=0.00){
		executeTransaction();
	}
	if(transacted!=0.00){
		closeTransacted();
	}
	if(robber!=null){
		dealWithRobbery();
	}
	
	//ACTIONS
	
	private void createNewAccount(){
		manager.createNewAccount();
		customer.state=accountState.requested;
	}
		
	
}
