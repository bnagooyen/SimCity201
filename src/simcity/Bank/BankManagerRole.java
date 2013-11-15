package simcity.Bank;

import java.util.ArrayList;
import java.util.List;

import simcity.PersonAgent;
import simcity.Bank.BankManagerRole.MyEmployee.EmployeeType;
import simcity.Bank.BankManagerRole.MyEmployee.MyTellerState;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import agent.Role;

public class BankManagerRole extends Role implements BankManager {
	
	//data
	List<MyEmployee> workers = new ArrayList<MyEmployee>();
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	
	protected BankManagerRole(PersonAgent p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	private List<MyEmployee> tellers;
	
	//MESSAGES
	public void msgIAmHere(Role person, String type) {
		if(type.equals("BankTeller")) {
			workers.add(new MyEmployee(person, MyEmployee.EmployeeType.Teller));
			stateChanged();
		}
		else if (type.equals("BankLoanOfficer")) {
			workers.add(new MyEmployee(person, MyEmployee.EmployeeType.LoanOfficer));
			stateChanged();
		}
		else if(type.equals("loan")) {
			customers.add(new MyCustomer(person, MyCustomer.MyCustomerState.loan));
			stateChanged();
		}
	}
	
	public void msgAvailable(BankTeller t) {
		for (MyEmployee wee: workers) {
			if(wee.type==EmployeeType.Teller) {
				wee.state=MyTellerState.available;
				stateChanged();
			}
		}
	}

	public void msgAvailable(BankLoanOfficer t) {
		for (MyEmployee wee: workers) {
			if(wee.type==EmployeeType.Teller) {
				wee.state=MyTellerState.available;
				stateChanged();
			}
		}
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class MyEmployee {
		Role emp;
		//boolean needsAccount=false;
		int accountNum = 0;
		enum MyTellerState {justArrived, available, occupied};
		MyTellerState state;
		enum EmployeeType {LoanOfficer, Teller};
		EmployeeType type;
		
		MyEmployee (Role p, EmployeeType tp) {
			type=tp;
			emp = p;
			state= MyTellerState.justArrived;
		}
		
	}
	
	static class MyCustomer {
		Role BankCustomer;
		enum MyCustomerState {transaction, loan};
		MyCustomerState state;
		
		MyCustomer(Role b, MyCustomerState st) {
			state=st;
			BankCustomer = b;
		}
		
	}
	
}
