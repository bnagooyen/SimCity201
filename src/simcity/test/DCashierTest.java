package simcity.test;

import java.text.DecimalFormat;
import java.util.ArrayList;

import simcity.interfaces.DHost;
import simcity.mockrole.MockRoleCook;
import simcity.test.mock.MockCook;
import simcity.test.mock.MockCustomer;
import simcity.test.mock.MockDHost;
import simcity.test.mock.MockDRestaurantCustomer;
import simcity.test.mock.MockDRestaurantWaiter;
import simcity.test.mock.MockMarket;
import simcity.test.mock.MockWaiter;
import simcity.DRestaurant.DCashierRole;
import simcity.DRestaurant.DCheck;
import simcity.DRestaurant.DFoodOrder;
import simcity.DRestaurant.DCashierRole.InventoryBill.InventoryBillState;
import simcity.DRestaurant.DCheck.CheckState;
import simcity.DRestaurant.DHostRole;
import simcity.PersonAgent;
import junit.framework.*;

/*
 * 
 * 	import simcity.PersonAgent;
	import simcity.KRestaurant.KCashierRole;
	import simcity.KRestaurant.KCashierRole.Order;
	import simcity.KRestaurant.KCashierRole.orderState;
	import simcity.KRestaurant.KCashierRole.paidState;
	import simcity.interfaces.KCashier;
	import simcity.test.mock.MockCustomer;
	//import restaurant.CashierAgent.cashierBillState;
	//import restaurant.WaiterAgent.Bill;
	import simcity.test.mock.MockCustomer;
	import simcity.test.mock.MockKRestaurantCustomer;
	import simcity.test.mock.MockKRestaurantWaiter;
	//import simcity.test.mock.MockMarket;
	import simcity.test.mock.MockWaiter;
	//import restaurant.test.mock.MockWaiter;
	import junit.framework.*;
 * 
 */

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class DCashierTest extends TestCase
{



		//these are instantiated for each test separately via the setUp() method.
		DCashierRole cashier;
		MockDRestaurantWaiter waiter;
		MockDRestaurantCustomer customer;
		//hack .. I know its wrong
		MockDHost host;
		PersonAgent p;
//		MockMarket market1;
//		MockMarket market2;
		
		/**
		 * This method is run before each test. You can use it to instantiate the class variables
		 * for your agent and mocks, etc.
		 */
		
		public void setUp() throws Exception{
			super.setUp();		
			p = new PersonAgent("person");
			cashier = new DCashierRole();
			cashier.myPerson = p;
			customer = new MockDRestaurantCustomer("mockcustomer");		
			waiter = new MockDRestaurantWaiter("mockwaiter");
			host = new DHost();
			//cashier.onDuty = false;
//			market1 = new MockMarket("mockMarket1");
//			market2 = new MockMarket("mockMarket2");
		}	
		
		/**
		 * tests cashier when a waiter asks cashier to compute a bill for a customers
		 */
		public void testWaiterAsksForBill()
		{
			waiter.cashier = cashier;
			cashier.AddHost(host);
			assertTrue(cashier.pickAndExecuteAnAction());
			// pre-conditions
			assertEquals("cashier should have zero bills but doesn't", cashier.myBills.size(), 0);
			//assertEquals("CashierAgent should have an empty event log before his msgBill is called. Instead, the cashier's event log read: " + cashier.log.toString(), 0, cashier.log.size());
			assertEquals("MockWaiter should have an empty event log. Instead, the MockWaiter's event log reads: "
	                + waiter.log.toString(), 0, waiter.log.size());
			//cashier arrives
			// give cashier bill to compute
			cashier.msgComputeBill("Chicken", customer, "Doreen", 1, waiter);
			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
	                + waiter.log.toString(), 0, waiter.log.size());
			assertEquals("cashier got a new bill but doesn't", cashier.myBills.size(), 1);
			assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
			assertTrue("Waiter should have logged \"received check from cashier\" but didn't. His log reads instead: " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("received check from cashier"));

			// post-conditions
			assertFalse("Cashier's scheduler should return false but doesn't", cashier.pickAndExecuteAnAction());
			assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called. Instead, the MockCustomer's event log reads: "
	                                + customer.log.toString(), 0, customer.log.size());
			
		}

		
		/**
		 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
		 */
		public void testOneNormalCustomerScenario()
		{
			customer.cashier = cashier;
			
			// check preconditions
			assertEquals("cashier should have zero bills but doesn't", cashier.myBills.size(), 0);
		
			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
	                + waiter.log.toString(), 0, waiter.log.size());
			assertEquals("MockCustomer should have an empty event log. Instead, the MockCustomer's event log reads: "
	                + customer.log.toString(), 0, customer.log.size());

			// waiter gives bill to cashier to compute
			cashier.msgComputeBill("Chicken", customer, "Doreen", 1, waiter);
			
			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
	                + waiter.log.toString(), 0, waiter.log.size());
			assertEquals("cashier got a new bill", cashier.myBills.size(), 1);
	        assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
			assertTrue("Waiter should have logged \"received check from cashier\" but didn't. His log reads instead: " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("received check from cashier"));
			assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
	                                + customer.log.toString(), 0, customer.log.size());
			
			// customer gives payment to cashier
	 		 cashier.msgHereIsAPayment(customer, 1, 10.99);
			 assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.", cashier.myBills.get(0).state == CheckState.paid);
			 //assertTrue("Cashier should have logged \"Received payment\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment"));
			 assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
	                 + cashier.myBills.get(0).BillAmnt, cashier.myBills.get(0).BillAmnt == 10.99);
			 assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
	                 cashier.myBills.get(0).customer == customer);
			 assertTrue("bills contains a bill with status == givenPayment. It doesn't", cashier.myBills.get(0).state == CheckState.paid);

			 // make sure cashier handles bill/payment correctly
			 assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
	                 cashier.pickAndExecuteAnAction());
			 assertTrue("MockCustomer should have logged an event for receiving \"msgChange\" with the correct balance, but his last event logged reads instead: " 
	                 + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgChange from cashier. Total = 0"));
			 //assertTrue("Cashier should have logged \"customer has paid\" but didn't. His log reads instead: " 
	           //      + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("customer has paid"));
			 assertTrue("bills shouldn't contain a bill with moneyOwed >0. It doesn't", cashier.myBills.get(0).debt == 0);

			 // postconditions
			 assertFalse("Cashier's scheduler should have returned false because nothing else to do, but didn't.", 
	                 cashier.pickAndExecuteAnAction());

		}//end one normal customer scenario
		
		/**
		 * tests cashier when customer pays more than the bill and needs change
		 */
//		public void testCustomerNeedsChange()
//		{
//			customer.cashier =  cashier;
//			
//			// check preconditions
//			assertEquals("cashier should have zero bills", cashier.bills.size(), 0);
//			assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
//	                + cashier.log.toString(), 0, cashier.log.size());
//			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//	                + waiter.log.toString(), 0, waiter.log.size());
//			assertEquals("MockCustomer should have an empty event log. Instead, the MockCustomer's event log reads: "
//	                + customer.log.toString(), 0, customer.log.size());
//			
//			// waiter gives bill to cashier to compute
//			cashier.msgBill(customer, waiter, "Chicken");
//			
//			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//	                + waiter.log.toString(), 0, waiter.log.size());
//			assertEquals("cashier got a new bill", cashier.bills.size(), 1);
//	        assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Waiter should have logged \"received check from cashier\" but didn't. His log reads instead: " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("received check from cashier"));
//
//			assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
//	                                + customer.log.toString(), 0, customer.log.size());
//			
//			// customer gives payment to cashier
//	 		 cashier.msgPayment(20, customer, 10.99);
//			 assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.", cashier.bills.get(0).s == orderState.givenPayment);
//			 assertTrue("Cashier should have logged \"Received payment\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment"));
//			 assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
//	                 + cashier.bills.get(0).price, cashier.bills.get(0).price == 10.99);
//			 assertTrue("CashierBill should contain a bill with payment > $10.99. It contains something else instead: $" 
//	                 + cashier.bills.get(0).price, cashier.bills.get(0).payment > 10.99);
//			 assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
//	                 cashier.bills.get(0).c == customer);
//			 assertTrue("bills should contain a bill with status == givenPayment, but doesn't", cashier.bills.get(0).s == orderState.givenPayment);
//
//			 // make sure cashier handles bill correctly
//			 assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//	                 cashier.pickAndExecuteAnAction());
//			 assertTrue("MockCustomer should have logged an event for receiving \"msgChange\" with the correct balance, but his last event logged reads instead: " 
//	                 + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgChange from cashier. Total = 9.01"));
//			 assertTrue("Cashier should have logged \"customer has paid\" but didn't. His log reads instead: " 
//	                 + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("customer has paid"));
//			 assertTrue("bills shouldn't contain a bill with moneyOwed >0. It doesn't", cashier.bills.get(0).moneyOwed == 0);
//
//			 // postconditions
//			 assertFalse("Cashier's scheduler should have returned false because nothing else to do, but didn't.", 
//	                 cashier.pickAndExecuteAnAction());
//			 assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
//	                 cashier.bills.get(0).s == orderState.completed);
//		}
		
		/**
		 * tests cashier when a customer doesn't have any money
		 */
//		public void testNoMoneyCustomer()
//		{
//			customer.cashier = cashier;
//			
//			// check preconditions
//			assertEquals("cashier should have zero bills", cashier.bills.size(), 0);
//			assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
//	                + cashier.log.toString(), 0, cashier.log.size());
//			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//	                + waiter.log.toString(), 0, waiter.log.size());
//			assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
//	                + customer.log.toString(), 0, customer.log.size());
//			
//			// waiter gives bill to cashier to compute
//			cashier.msgBill(customer, waiter, "Chicken");
//			
//			assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//	                + waiter.log.toString(), 0, waiter.log.size());
//			assertEquals("cashier got a new bill", cashier.bills.size(), 1);
//	        assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//			assertTrue("Waiter should have logged \"received check from cashier\" but didn't. His log reads instead: " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("received check from cashier"));
//
//			assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
//	                                + customer.log.toString(), 0, customer.log.size());
//			
//			// customer gives cashier payment
//	 		 cashier.msgPayment(5, customer, 10.99);
//			 assertTrue("CashierBill should contain a bill with state == givenPayment. It doesn't.", cashier.bills.get(0).s == orderState.givenPayment);
//			 assertTrue("Cashier should have logged \"Received payment\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment"));
//			 assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
//	                 + cashier.bills.get(0).price, cashier.bills.get(0).price == 10.99);
//			 assertTrue("CashierBill should contain a bill with payment > $5. It contains something else instead: $" 
//	                 + cashier.bills.get(0).price, cashier.bills.get(0).payment == 5);
//			 assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
//	                 cashier.bills.get(0).c == customer);
//			 assertTrue("bills should contain a bill with status == givenPayment, but doesn't", cashier.bills.get(0).s == orderState.givenPayment);
//
//			 // make sure cashier handles payment correctly
//			 assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//	                 cashier.pickAndExecuteAnAction());
//			 assertTrue("Cashier's log should say \"customer is a flake and didn't have enough money\"", cashier.log.containsString("customer is a flake and didn't have enough money"));
//			 assertEquals("Customer should have an empty event log since cashier doesn't have any change to give him and is angry. Instead, the Customer's event log reads: "
//		                + customer.log.toString(), 0, customer.log.size());
//			 assertTrue("bills should contain a bill with moneyOwed >0. It doesn't", cashier.bills.get(0).moneyOwed > 0);
//
//			 // post-conditions
//			 assertFalse("Cashier's scheduler should have returned false because nothing else to do, but didn't.", 
//	                 cashier.pickAndExecuteAnAction());
//			 assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
//	                 cashier.bills.get(0).s == orderState.completed);
//			 
//			 // step 5 - customer comes back and he pays of current and past bill
//			 //preconditions
//			 assertEquals("cashier should have one bill in bills, but doesn't", cashier.bills.size(), 1);
//			 assertTrue("CashierAgent shouldn't have empty event log before the Cashier's msgBill is called again. Instead, the Cashier's event log reads: "
//		                + cashier.log.toString(), cashier.log.size() >0);
//			 
//			 // waiter gives cashier bill to compute - cashier should recognize this customer
//			 cashier.msgBill(customer, waiter, "Chicken");
//			 assertEquals("MockWaiter shouldn't have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
//		                + waiter.log.toString(), 1, waiter.log.size());
//			 assertEquals("cashier got a new bill, but same customer, so size should still be 1, but it isn't", cashier.bills.size(), 1);
//		     assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
//			 assertTrue("Waiter should have logged \"received check from cashier\" but didn't. His log reads instead: " + waiter.log.getLastLoggedEvent(), waiter.log.containsString("received check from cashier"));
//			 assertTrue("Cashier knows this customer owes money", cashier.bills.get(0).owesMoney);
//			 assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler, since cashier didn't asnwer him. Instead, the MockCustomer's event log reads: "
//		                                + customer.log.toString(), 0, customer.log.size());
//			 
//			 // customer comes to pay
//			 cashier.msgPayment(20, customer, 10.99);
//			 assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.", cashier.bills.get(0).s == orderState.givenPayment);
//			 assertTrue("Cashier should have logged \"Received payment\" but didn't. His log reads instead: " + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment"));
//			 assertTrue("CashierBill should contain a bill of price = $10.99. It contains something else instead: $" 
//	                 + cashier.bills.get(0).price, cashier.bills.get(0).price == 10.99);
//			 assertTrue("CashierBill should contain a bill with payment == 20. It contains something else instead: $" 
//	                 + cashier.bills.get(0).price, cashier.bills.get(0).payment == 20);
//			 assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
//	                 cashier.bills.get(0).c == customer);
//			 assertTrue("bills should contain a bill with status == givenPayment, but doesn't", cashier.bills.get(0).s == orderState.givenPayment);
//			 
//			 // process payment
//			 assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
//	                 cashier.pickAndExecuteAnAction());
//			 assertEquals("Customer shouldn't have an empty event log since cashier gave him change. Instead, the Customer's event log reads: "
//		                + customer.log.toString(), 1, customer.log.size());
//			 assertTrue("bills shouldn't contain a bill with moneyOwed >0. It doesn't", cashier.bills.get(0).moneyOwed == 0);
//
//			 // check postconditions
//			 assertFalse("Cashier's scheduler should have returned false because nothing else to do, but didn't.", 
//	                 cashier.pickAndExecuteAnAction());
//			 assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
//	                 cashier.bills.get(0).s == orderState.completed);
//		}
		
		
		/**
		 * Tests the cashier when a single market fulfills and order and the bill is paid in full by cashier
		 */
//		public void testOneOrderFullfilledByOneMarket()
//		{
//			market1.cashier = cashier;
//			
//			// check preconditions
//			assertEquals("cashier should have zero bills to pay, but isnt", cashier.toPay.size(), 0);
//			assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
//	                + cashier.log.toString(), 0, cashier.log.size());
//			assertEquals("Market's log should be empty, but isn't", 0, market1.log.size());
//			
//			cashier.setMoney(100);
//			double check = 50.0;
//			
//			// send bill to cashier
//			cashier.msgBillFromMarket(check, market1);
//			
//			assertTrue("Cashier's toPay list should contain a bill that is unpaid", cashier.toPay.get(0).paid == paidState.notPaid);
//			assertEquals("Market's log should be empty", 0, market1.log.size());	
//			assertTrue("Cashier should have logged \"received bill from market to pay\"", cashier.log.containsString("received bill from market to pay"));
//			assertTrue("toPay should contain a bill", cashier.toPay.size() == 1);
//			assertTrue("the bill should have a price greater than 0", cashier.toPay.get(0).bill > 0);
//			assertTrue("the bill should contain the right market", cashier.toPay.get(0).m == market1);
//			
//			// make sure bill is handled correctly
//			assertTrue("Cashier's scheduler should return true and handle the bill but doesn't", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier should have logged \"paid Market\" but instead says" + cashier.log.getLastLoggedEvent(), cashier.log.containsString("paid Market"));
//			assertTrue("Bool paid of bill should be true but isn't", cashier.toPay.get(0).paid == paidState.paid);
//			assertTrue("Market should have logged \"got paid by cashier. Got $50.0\" but didn't", market1.log.containsString("got paid by cashier. Got $50.0"));
//			
//			// post conditions
//			assertFalse("Cashier's scheduler should have returned false because nothing else to do, but didn't.", 
//	                cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's toPay list should contain a bill that is paid, but doesn't", cashier.toPay.get(0).paid == paidState.paid);
//			 
//		}
	//	
//		/**
//		 * Tests cashier when two markets fulfill an order and both bills are paid in full by cashier
//		 */
	//	
//		public void testOneOrderFulfilledByTwoMarkets()
//		{
//			market1.cashier = cashier;
//			market2.cashier = cashier;
//			cashier.setMoney(100);
//			
//			// test preconditions
//			assertEquals("cashier should have zero bills to pay, but isn't", cashier.toPay.size(), 0);
//			assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
//	                + cashier.log.toString(), 0, cashier.log.size());
//			assertEquals("Market1's log should be empty, but isn't", 0, market1.log.size());
//			assertEquals("Market2's log should be empty, but isn't", 0, market2.log.size());
	//
//			
//			// check if both markets send msg right after another
//			double check1 = 50.0;
	//
//			cashier.msgBillFromMarket(check1, market1);
//			assertTrue("Cashier's toPay list should contain a bill that is unpaid, but doesn't", cashier.toPay.get(0).paid == paidState.notPaid);
//			assertEquals("Market1's log should be empty, but isn't", 0, market1.log.size());	
//			assertTrue("Cashier should have logged \"received bill from market to pay\"", cashier.log.containsString("received bill from market to pay"));
//			assertTrue("toPay should contain a bill, but doesn't", cashier.toPay.size() == 1);
//			assertTrue("the bill should have a price greater than 0, but isn't", cashier.toPay.get(0).bill > 0);
//			assertTrue("the bill should contain the right market, but doesn't", cashier.toPay.get(0).m == market1);
//			
//			// send another bill
//			double check2 = 30.0;
//			cashier.msgBillFromMarket(check2,  market2);
//			assertTrue("Cashier's toPay list should contain a bill that is unpaid, but doesn't", cashier.toPay.get(1).paid == paidState.notPaid);
//			assertEquals("Market2's log should be empty, but isn't", 0, market2.log.size());	
//			assertTrue("Cashier should have logged \"received bill from market to pay\"", cashier.log.containsString("received bill from market to pay"));
//			assertTrue("toPay should contain a bill. It doesn't", cashier.toPay.size() == 2);
//			assertTrue("the bill should have a price greater than 0. It isn't", cashier.toPay.get(1).bill > 0);
//			assertTrue("the bill should contain the right market - it doesn't", cashier.toPay.get(1).m == market2);
	//
//			// run scheduler
//			assertTrue("Cashier's scheduler should return true and handle the bill, but didn't", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier should have logged \"paid Market\" but instead says" + cashier.log.getLastLoggedEvent(), cashier.log.containsString("paid Market"));
//			assertTrue("Bool paid of bill should be true but isn't", cashier.toPay.get(0).paid == paidState.paid);
//			assertTrue("Market should have logged \"got paid by cashier. Got $50.0\" but didn't", market1.log.containsString("got paid by cashier. Got $50.0"));
//			assertEquals("Market2's log should be empty, but isn't", 0, market2.log.size());	
	//
//			// run scheduler again
//			assertTrue("Cashier's scheduler should return true and handle the bill, but didn't", cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier should have logged \"paid Market\" but instead says" + cashier.log.getLastLoggedEvent(), cashier.log.containsString("paid Market"));
//			assertTrue("Bool paid of bill should be true but isn't", cashier.toPay.get(1).paid == paidState.paid);
//			assertTrue("Market should have logged \"got paid by cashier. Got $30.0\" but didn't", market2.log.containsString("got paid by cashier. Got $30.0"));
//			
//			// post-condition
//			assertFalse("Cashier's scheduler should have returned false because nothing else to do, but didn't.", 
//	                cashier.pickAndExecuteAnAction());
//			assertTrue("Cashier's toPay list should contain a bill that is paid, but doesn't", cashier.toPay.get(0).paid == paidState.paid);
//			assertTrue("Cashier's toPay list should contain another bill that is paid, but doesn't", cashier.toPay.get(1).paid == paidState.paid);
	//
//		}
		
	}




