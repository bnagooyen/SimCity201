package simcity.Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.PersonAgent;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import agent.Role;

public class BankCustomerRole extends Role implements BankCustomer {
	//data
	BankManager manager = person.managers.get("bank"); 
	BankTeller teller; 
	BankLoanOfficer loanOfficer; 
	//messages
	
	
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
