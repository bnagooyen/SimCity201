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
	BankManager manager; 
	BankTeller teller; 
	BankLoanOfficer loanOfficer; 
	enum bankCustomerState { arrived, waiting, inProgress, done};
	bankCustomerState state=bankCustomerState.arrived;
	Integer accountNum=null;
	
	
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
	
	protected BankCustomerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean pickAndExecuteAnAction() {

		
		return false;
	}
	
	//actions

	
}
