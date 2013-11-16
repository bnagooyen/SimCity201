package simcity.Bank;

import java.util.ArrayList;
import java.util.*;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyCustomer;
//import simcity.Bank.BankManagerRole.MyEmployee;
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
	public void msgDeposit(BankCustomer BC, int actNum, double amount){
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=amount;
		stateChanged();
	}
	public void msgWithdrawal(BankCustomer BC, int actNum, double amount){
		if(customer==null) customer= new MyCustomer(BC);
		customer.accountNumber=actNum;
		requested=-amount;
		stateChanged();
	}
	public void msgTransactionProcessed(double finalAmount){
		transacted=finalAmount;
		stateChanged();
	}
	public void msgIAmRobbingYou(BankRobber BR){
		robber=BR;
		stateChanged();
	}
	public void msgIShotYou(){
		//Tell person he was shot, Message? Directly add to state?
	}
	public void msgGoHome(){
		state=bankTellerState.finshed;
		stateChanged();
	}
	public void msgGoToTellerPosition(){
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
	if(requested!=0.00){
		executeTransaction();
		return true;
	}
	if(transacted!=0.00){
		closeTransaction();
		return true;
	}
	if(robber!=null){
		dealWithRobbery();
		return true;
	}
		return false;
	}

	
	//ACTIONS
	private void createNewAccount(){
		manager.msgCreateAccount("BankTeller");
		customer.state=accountState.requested;
	}
	
	private void hereIsYourAccount(){
		customer.BC.msgAccountMade(customer.accountNumber);
		customer.state=accountState.existing;
	}
	private void executeTransaction(){
		manager.msgProcessTransaction(customer.accountNumber, requested);
	}
	private void closeTransaction(){
		customer.BC.msgTransactionComplete(transacted);
		customer=null;
		transacted=requested=0.00;
		manager.msgAvailable(this);
	}
	private void dealWithRobbery(){
		Random generator=new Random();
		int choice = generator.nextInt(2);
		if(choice==0){
			robber.msgHereIsMoney(1000.00);
		}
		else if(choice==1){
			robber.msgIRefuseToPay();
		}
		else if(choice==2){
			robber.msgIShotYou();
		}
	}

	
	
		
}
