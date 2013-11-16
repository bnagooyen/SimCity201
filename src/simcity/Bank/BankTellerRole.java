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
	
	enum bankTellerState { working, success, error, finshed };
	bankTellerState state=bankTellerState.working;
	
	enum accountState {none,requested,justMade,existing};
	
	class MyCustomer{
		BankCustomer BC;
		Integer accountNumber;
		accountState state=accountState.existing;
		
		MyCustomer(BankCustomer BankCust){
			BC=BankCust;
			accountNumber=null;
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
	public void msgMakeAccount(BankCustomer BC){
		customer=new MyCustomer(BC);
		customer.state=accountState.justMade;
	}
	public void msgAccountCreated(int num){
		customer.state=accountState.justMade;
	}
	public void msgDeposit(BankCustomer BC, int actNum, double amount){
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=amount;
	}
	public void msgWithdrawal(BankCustomer BC, int actNum, double amount){
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=-amount;
	}
	public void msgTransactionProcessed(double finalAmount){
		transacted=finalAmount;
	}
	public void msgIAmRobbingYou(BankRobber BR){
		robber=BR;
	}
	public void msgIShotYou(){
		//Tell person he was shot, Message? Directly add to state?
	}
	public void msgGoHome(){
		state=bankTellerState.finshed;
	}
	public void msgGoToTellerPosition(){
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
		manager.msgCreateAccount("BankTeller");
		customer.state=accountState.requested;
	}
	
	
		
}
