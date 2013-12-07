package simcity.DRestaurant;

import simcity.interfaces.DCustomer;
import simcity.interfaces.DWaiter;

public class DCheck {
	int tablenum;
	double BillAmnt;
	double CustomerPaid;
	double debt;
	//double Change;
	String choice;
	DWaiter waiter;
	String name;
	DCustomer customer; //for cashier to keep track of who to give change to
	public enum CheckState { processing, processed, sent, paid, debt };
	CheckState state;
	
	DCheck(String ch, int tnum, double amnt, DWaiter wa) {
		choice = ch;
		tablenum = tnum;
		waiter = wa;
		//Change = 0;
		BillAmnt = amnt;
		state=CheckState.processing;
		debt=0;
	}
	
	public DCheck(String ch, DCustomer cust, String nm, int tnum, DWaiter wa) {
		choice = ch;
		name=nm;
		tablenum=tnum;
		customer=cust;
		waiter = wa;
		//Change = 0;
		BillAmnt = 0;
		state=CheckState.processing;
	}
	
	public DCheck(DCustomer cust, String ch, int tnum, double d) { // constructor for the customer.. this is all the info it needs to see
		choice = ch;
		BillAmnt = d;
		customer=cust;
		tablenum= tnum;
	}


	public DCheck(DCustomer customer2, int amnt,
			double d) {
		// TODO Auto-generated constructor stub
		tablenum = amnt;
		BillAmnt=d;
	}

	public double getBillAmnt() {
		return BillAmnt;
	}

	void setBillAmnt(double a) {
		BillAmnt=a;
	}
	String getChoice() {
		return choice;
	}
	DWaiter getWaiter() {
		return waiter;
	}
	int getTablenum() {
		return tablenum;
	}
	void setCustomerPaid(double c) {
		CustomerPaid=c;
	}
	public double getCustomerPaid() {
		return CustomerPaid;
	}
	
	public DCustomer getCustomer() {
		return customer;
	}
	
	public CheckState getState() {
		return state;
	}
	
	public double getChange() {
		return BillAmnt - CustomerPaid;
	}
	
	void setCustomer(DCustomer cust) {
		customer=cust;
	}
	
}
