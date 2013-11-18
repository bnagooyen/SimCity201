package simcity.Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.PersonAgent;
import simcity.Bank.BankLoanOfficerRole.bankLoanState;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import agent.Role;

public class BankCustomerRole extends Role implements BankCustomer {
	//data
	public BankManager manager; 
	public BankTeller teller; 
	public BankLoanOfficer loanOfficer; 
	public enum bankCustomerState { arrived, waiting, inProgress, done};
	public bankCustomerState state=bankCustomerState.arrived;
	public String purpose;
	public Integer accountNum=null;
	
	
	//messages
	public void msgGoToLoanOfficer(BankLoanOfficer BL){
		loanOfficer=BL;
	}
	
	public void msgGoToTeller(BankTeller BT){
		teller=BT;
	}
	
	public void msgTransactionComplete(double amount){
		myPerson.money-=amount;
		state=bankCustomerState.done;
	}
	
	public void msgHeresLoan(double amount){
		myPerson.money+=amount;
	}
	
	public void msgLoanDenied(){
		state=bankCustomerState.done;
	}
	
	public void msgAccountMade(int AN){
		accountNum=AN;
		state=bankCustomerState.waiting;
	}
	
	public BankCustomerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
		
		purpose="transaction";						//NEED A GOOD WAY FOR PERSON TO DECIDE
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if(state==bankCustomerState.arrived){
			tellManagerArrived();
			return true;
		}
		if((loanOfficer!=null || teller!=null) && accountNum==null){
			makeAccount();
			return true;
		}
		if(loanOfficer!=null && state==bankCustomerState.waiting){
			requestLoan();
			return true;
		}
		if(teller!=null && state==bankCustomerState.waiting){
			makeTransaction();
			return true;
		}
		if(state==bankCustomerState.done){
			leaveBank();
			return true;
		}
		return false;
	}
	
	//actions
	private void tellManagerArrived(){
		manager.msgIAmHere(this,purpose);
		state=bankCustomerState.waiting;
	}
	
	private void makeAccount(){
		if(loanOfficer!=null){
			loanOfficer.msgMakeAccount(this);
		}
		else if(teller!=null){
			teller.msgMakeAccount(this);
		}
		state=bankCustomerState.inProgress;
	}
	
	private void requestLoan(){
		//doGoToLoanOfficer
		loanOfficer.msgINeedALoan(this, accountNum, 1000, "waiter"/*myPerson.job*/);		//Need a way to decide how much $$ to request  
		state=bankCustomerState.inProgress;
	}
	
	private void makeTransaction(){
		//doGoToTeller
		if(myPerson.money>myPerson.depositThreshold){
			teller.msgDeposit(this, accountNum, myPerson.money-myPerson.depositThreshold);
		}
		if(myPerson.money<myPerson.withdrawalThreshold){
			teller.msgWithdrawal(this, accountNum, myPerson.withdrawalThreshold-myPerson.money);
		}
		state=bankCustomerState.inProgress;
	}
	
	private void leaveBank(){
		this.isActive=false;
		state=bankCustomerState.arrived;
		//doLeaveBank
	}
}
