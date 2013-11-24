package simcity.test;

import java.math.*;

import simcity.PersonAgent;
import simcity.Drew_restaurant.Drew_CashierRole.*;
import simcity.Drew_restaurant.interfaces.*;
import simcity.Drew_restaurant.Drew_CashierRole;
import simcity.Drew_restaurant.test.mock.EventLog;
import simcity.Drew_restaurant.test.mock.*;
//import simcity.Drew_restaurant.test.mock.MockMarket;
import junit.framework.*;

/**
 * 
 */
public class Drew_CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	Drew_CashierRole cashier;
	Drew_MockWaiter waiter;
	Drew_MockCustomer customer;
	//MockMarket market;		MarketStuff
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new Drew_CashierRole(new PersonAgent("cashier"));		
		customer = new Drew_MockCustomer("mockcustomer");		
		waiter = new Drew_MockWaiter("mockwaiter");
		//market = new MockMarket("mockmarket");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		System.out.println("\n\n***TEST - Customer Pays Exact***\n\n");
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);
		//assertEquals("Cashier should have 0 Owed bills in it. It doesn't.",cashier.owedBills.size(), 0);
		
		//step 1 of the test
		cashier.calculateBill(waiter, "steak", 2, 0.0);
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertEquals("Bill should go to table 2. It doesn't.", cashier.bills.get(0).t, 2);
		
		assertTrue("Cashier's scheduler should have returned true (gives bill to waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		//step 2 of the test
		assertTrue("Customer is equal to Null when it should not be", customer!=null);

		cashier.payBill(15.99,15.99, customer);
		
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).getState() == BillState.recievedFromCustomer);
		

		assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.bills.get(0).b, cashier.bills.get(0).b == 15.99);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).c == customer);
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		
		
		//step 4
		assertFalse("Cashier's scheduler should have returned False , but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
	
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).getState() == BillState.done);
		
		assertTrue("Change should be equal to $0.00", customer.change==(0.0));
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}//end one normal customer scenario
	
	
	public void testtwoCustomerUnderpays()
	{
		//System.out.println("\n\n***TEST TWO- Customer overpays***\n\n");
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		System.out.println("\n\n***TEST - Customer Pays Too Much***\n\n");
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);
		//assertEquals("Cashier should have 0 Owed bills in it. It doesn't.",cashier.owedBills.size(), 0);
		
		//step 1 of the test
		cashier.calculateBill(waiter, "steak", 2, 0.0);
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertEquals("Bill should go to table 2. It doesn't.", cashier.bills.get(0).t, 2);
		assertEquals("Bill should be in state calculated.", cashier.bills.get(0).getState(), BillState.calculated);
		
		assertTrue("Cashier's scheduler should have returned true (gives bill to waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		//step 2 of the test
		assertTrue("Customer is equal to Null when it should not be", customer!=null);

		cashier.payBill(15.99,76.63, customer);
		
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).getState() == BillState.recievedFromCustomer);
		
		
		
		assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.bills.get(0).b, cashier.bills.get(0).b == 15.99);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).c == customer);
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		
		
		//step 4
		assertFalse("Cashier's scheduler should have returned False , but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
	
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).getState() == BillState.done);
		
		assertTrue("Change should be equal to $60.64", Math.abs(customer.change-60.64)<.1);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	public void testThreeCustomerOverpays()
	{
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		System.out.println("\n\n***TEST - Customer Doesn't pay Enough***\n\n");
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);
		//assertEquals("Cashier should have 0 Owed bills in it. It doesn't.",cashier.owedBills.size(), 0);
		
		//step 1 of the test
		cashier.calculateBill(waiter, "steak", 2, 0.0);
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertEquals("Bill should go to table 2. It doesn't.", cashier.bills.get(0).t, 2);
		assertEquals("Bill should be in state calculated.", cashier.bills.get(0).getState(), BillState.calculated);
		
		assertTrue("Cashier's scheduler should have returned true (gives bill to waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		//step 2 of the test
		assertTrue("Customer is equal to Null when it should not be", customer!=null);

		cashier.payBill(15.99,10.99, customer);
		
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).getState() == BillState.recievedFromCustomer);
		
		assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.bills.get(0).b, cashier.bills.get(0).b == 15.99);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).c == customer);
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		
		//step 4
		assertFalse("Cashier's scheduler should have returned False , but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
	
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).getState() == BillState.done);
		
		assertTrue("Debt should be equal to $5.0", Math.abs(customer.debt-5.0)<.1);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//step 5 Customer comes back
		cashier.calculateBill(waiter, "steak", 3, 5.0);
		
		assertEquals("Cashier should have 2 bills in it. It doesn't.", cashier.bills.size(), 2);
		assertEquals("Bill should go to table 3. It doesn't.", cashier.bills.get(1).t, 3);
		assertEquals("Bill should be in state calculated.", cashier.bills.get(1).getState(), BillState.calculated);
		assertTrue("Bill should be 20.99.", Math.abs(cashier.bills.get(1).b-20.99)<0.1);
		
		assertTrue("Cashier's scheduler should have returned true (gives bill to waiter), but didn't.", cashier.pickAndExecuteAnAction());
		//Step 6, pay second bill
		cashier.payBill(15.99+customer.debt,25.0, customer);
		
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(1).getState() == BillState.recievedFromCustomer);
		
		assertTrue("CashierBill should contain a bill of price = $20.99. It contains something else instead: $" 
				+ cashier.bills.get(1).b, Math.abs(cashier.bills.get(1).b-20.99)<0.1);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(1).c == customer);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	/*public void testFourSingleMarketOrder()
	{
		System.out.println("\n\n***TEST - Cashier Pays Market***\n\n");
		
		
		//Preconditions
		assertEquals("Cashier should have no owed bills to start.", cashier.owedBills.size(), 0);
		assertEquals("Cashier should start with $100.00.", cashier.getCash(), 100.0);
		double cash=cashier.getCash();
		
		
		//Cashier gets invoice from market
		cashier.marketBill(45.0, market);
		assertEquals("Cashier should have 1 owed bill.", cashier.owedBills.size(), 1);
		assertEquals("Owed Bill should be calculate.", cashier.owedBills.get(0).getState(), BillState.calculated);
		assertEquals("Cashier know which market.", cashier.owedBills.get(0).market, market);
		
		assertTrue("Statechanged should return true", cashier.pickAndExecuteAnAction());
		//Market Paid
		assertEquals("Owed Bill should be done.", cashier.owedBills.get(0).getState(), BillState.done);
		assertEquals("Cash has gone down by invoice amount", cashier.getCash(), cash-45.0);
		
		assertFalse("Statechanged should return false", cashier.pickAndExecuteAnAction());
		
	}*/
	
	/*public void testFiveTwoMarketOrder()
	{
		System.out.println("\n\n***TEST - Cashier Pays Two Markets***\n\n");
		
		
		//Preconditions
		assertEquals("Cashier should have no owed bills to start.", cashier.owedBills.size(), 0);
		assertEquals("Cashier should start with $100.00.", cashier.getCash(), 100.0);
		double cash=cashier.getCash();
		
		//Cashier gets invoice from market
		cashier.marketBill(25.0, market);
		cashier.marketBill(15.0, market);
		assertEquals("Cashier should have 2 owed bills.", cashier.owedBills.size(), 2);
		assertEquals("Owed Bill should be calculate.", cashier.owedBills.get(0).getState(), BillState.calculated);
		assertEquals("Cashier know which market.", cashier.owedBills.get(0).market, market);
		
		//Pay First Market
		assertTrue("Statechanged should return true", cashier.pickAndExecuteAnAction());
		assertEquals("The First Owed Bill should be done.", cashier.owedBills.get(0).getState(), BillState.done);
		assertEquals("The Second Owed Bill should be calculated.", cashier.owedBills.get(1).getState(), BillState.calculated);
		assertEquals("Cash has gone down by first invoice amount", cashier.getCash(), cash-25.0);
		
		//Pay second Market
		assertTrue("Statechanged should return True", cashier.pickAndExecuteAnAction());
		assertEquals("The Second Owed Bill should be done.", cashier.owedBills.get(1).getState(), BillState.done);
		assertEquals("Cash has gone down by total amount", cashier.getCash(), cash-40.0);
		
		assertFalse("Statechanged should return false", cashier.pickAndExecuteAnAction());
	}*/
	
	/*public void testSixOneMarketOrderDuringCustomer()
	{
		System.out.println("\n\n***TEST - Market invoice comes in During customer Transaction***\n\n");
		
		
		//Preconditions
		assertEquals("Cashier should have no owed bills to start.", cashier.owedBills.size(), 0);
		assertEquals("Cashier should start with $100.00.", cashier.getCash(), 100.0);
		double cash=cashier.getCash();
		
		//Customers bill
		cashier.calculateBill(waiter, "steak", 2, 0.0);
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertEquals("Bill should go to table 2. It doesn't.", cashier.bills.get(0).t, 2);
		
		//Get invoice from market
		cashier.marketBill(45.0, market);
		assertEquals("Cashier should have 1 owed bill.", cashier.owedBills.size(), 1);
		assertEquals("Owed Bill should be calculate.", cashier.owedBills.get(0).getState(), BillState.calculated);
		assertEquals("Cashier know which market.", cashier.owedBills.get(0).market, market);
		assertTrue("Statechanged should return true", cashier.pickAndExecuteAnAction());
		//Market Paid
		assertEquals("Owed Bill should be done.", cashier.owedBills.get(0).getState(), BillState.done);
		assertEquals("Cash has gone down by invoice amount", cashier.getCash(), cash-45.0);
		
		
		//Customer Pays bill
		cashier.payBill(15.99,15.99, customer);
		
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).getState() == BillState.recievedFromCustomer);
		assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.bills.get(0).b, cashier.bills.get(0).b == 15.99);	
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).c == customer);
		assertTrue("Cashier's scheduler should have returned true (gives bill to waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		//Cashier processes customers bill
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).getState() == BillState.done);
		//Check that $ transaction turned out correctly
		assertTrue("Cash has gone up by payment amount", Math.abs(cashier.getCash()-(cash-45.0+15.99))<0.1);
		
		
		assertFalse("Cashier's scheduler should have returned False , but didn't.", 
				cashier.pickAndExecuteAnAction());
	}*/
	
}
