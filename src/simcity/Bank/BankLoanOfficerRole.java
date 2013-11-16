package simcity.Bank;

import java.util.*;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyCustomer;
import simcity.Bank.BankManagerRole.MyTeller;
//import simcity.Bank.BankManagerRole.MyEmployee;
import simcity.interfaces.*;
import agent.Role;

public class BankLoanOfficerRole extends Role implements BankTeller {
	
	//data
	BankManager manager;
	MyCustomer customer;
	private static List<String> acceptableJobs = Collections.synchronizedList(new ArrayList<String>());


	class MyCustomer{
		BankCustomer BC;
		Integer accountNumber;
		String job;
		double amount;
		boolean accepted;
		
		MyCustomer(BankCustomer BankCust, Integer ANUM, String J, double amt){
			BC=BankCust;
			accountNumber=ANUM;
			job=J;
			amount=amt;
			accepted=true;
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
	public void msgINeedALoan(BankCustomer BC, Integer AN, double amt, String Job){
		customer=new MyCustomer(BC, AN, Job, amt);
	}
	
	
	//SCHEDULER	
	
	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub	
		if(customer!=null){
			analyzeLoan();
		}
		return false;
	}

	
	//ACTIONS
	private void analyzeLoan(){
		for (String job : acceptableJobs) {
			if (customer.job == job) {
				customer.accepted=false;
			}
		}
	}
		

	
	
		
}
