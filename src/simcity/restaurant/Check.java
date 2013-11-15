package simcity.restaurant;

import simcity.restaurant.interfaces.Customer;
import simcity.restaurant.interfaces.Waiter;

public class Check {
	int tablenum;
	double BillAmnt;
	double CustomerPaid;
	double debt;
	String choice;
	Waiter waiter;
	String name;
	Customer customer; //for cashier to keep track of who to give change to
	public enum CheckState { processing, processed, sent, paid, debt };
	CheckState state;
	
	Check(String ch, int tnum, double amnt, Waiter wa) {
		choice = ch;
		tablenum = tnum;
		waiter = wa;
		BillAmnt = amnt;
		state=CheckState.processing;
		debt=0;
	}
	
	public Check(String ch, Customer cust, String nm, int tnum, Waiter wa) {
		choice = ch;
		name=nm;
		tablenum=tnum;
		customer=cust;
		waiter = wa;
		BillAmnt = 0;
		state=CheckState.processing;
	}
	
	public Check(Customer cust, String ch, int tnum, double d) { // constructor for the customer.. this is all the info it needs to see
		choice = ch;
		BillAmnt = d;
		customer=cust;
		tablenum= tnum;
	}


	public Check(Customer customer2, int amnt,
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
	Waiter getWaiter() {
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
	
	public Customer getCustomer() {
		return customer;
	}
	
	public CheckState getState() {
		return state;
	}
	
	public double getChange() {
		return BillAmnt - CustomerPaid;
	}
	
	void setCustomer(Customer cust) {
		customer=cust;
	}
	
}
