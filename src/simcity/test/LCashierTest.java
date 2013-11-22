package simcity.test;

import simcity.PersonAgent;
import simcity.LRestaurant.LCashierAgent;
import simcity.LRestaurant.LCashierAgent.OrderState;
import simcity.test.mock.EventLog;
//import simcity.LRestaurant.test.mock.MockMarket;
import simcity.interfaces.LCashier;
import simcity.interfaces.LCustomer;
import simcity.interfaces.LWaiter;
import simcity.test.mock.MockLCustomer;
import simcity.test.mock.MockLWaiter;
import junit.framework.*;


public class LCashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent p;
	LCashierAgent cashier;
	MockLWaiter waiter;
	MockLCustomer customer;
//	MockMarket market;
//	MockMarket market2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		p = new PersonAgent("person", cashier);
		cashier = new LCashierAgent(p);		
		customer = new MockLCustomer("mockcustomer");	
//		market = new MockMarket("mockmarket");
//		market2 = new MockMarket("mockmarket2");
		waiter = new MockLWaiter("mockwaiter");
		
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	/**public void testCashierMarket1()
	{
		//Cashier needs to pay one market bill
		
		market.cashier = cashier;
		
		//Check precondition
		assertEquals("There are no existing bill.", cashier.bills.size(), 0); //Market
		
		
		//Communication between Market and Cashier
		
		cashier.msgHereIsSupplyCheck(10, market);
		
		assertEquals("There is one bill.", cashier.bills.size(), 1);
		
		assertTrue("CashierSupplyBill should contain a bill with the right market in it.", 
				cashier.bills.get(0).m == market);
		
		assertTrue("Cashier is giving change to customer.", cashier.pickAndExecuteAnAction());
		
		assertTrue("Market logged: " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received msgHereIsMoney from cashier."));

		assertFalse("Cashier finished paying supply bill.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier fulfilled payement.", cashier.bills.size(), 0); 
	
	
	}*/
	
	/**public void testCashierMarket2()
	{
		//Cashier has two market bills to pay
		
		market.cashier = cashier;
		market2.cashier = cashier;
		
		//Check preconditions
		assertEquals("There are no existing bill.", cashier.bills.size(), 0); //Market
		

		//Communication between Market and Cashier
		
		cashier.msgHereIsSupplyCheck(10, market);
		cashier.msgHereIsSupplyCheck(10, market2);
		
		assertEquals("There are two bills.", cashier.bills.size(), 2);
		
		assertTrue("CashierSupplyBill should contain a bill with the right market in it.", 
				cashier.bills.get(0).m == market);
		
		assertTrue("CashierSupplyBill should contain a bill with the right market in it.", 
				cashier.bills.get(1).m == market2);
		
		assertTrue("Cashier is giving change to customer.", cashier.pickAndExecuteAnAction());
		
		assertTrue("Market logged: " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received msgHereIsMoney from cashier."));

		assertTrue("Cashier is giving change to customer.", cashier.pickAndExecuteAnAction());
		
		assertTrue("Market2 logged: " + market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received msgHereIsMoney from cashier."));
		
		assertFalse("Cashier finished paying supply bill.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier fulfilled payement.", cashier.bills.size(), 0); 
	
	
	}*/
	
	public void testCashierCustomer1()
	{
		//Cashier has enough money to pay bill.
		
		customer.cashier = cashier;
		
		//Check preconditions
		assertEquals("There are no existing reactions.", cashier.transactions.size(), 0); 
		
		
		//Communication between Customer and Cashier
		
		cashier.msgHereIsPayment(customer, 20, 10); //msg sent by customer to cashier
		
		assertEquals("There is one transaction.", cashier.transactions.size(), 1);
		
		assertTrue("CashierBill should contain a bill with the right customer in it.", 
				cashier.transactions.get(0).c == customer);
		
		assertTrue("Cashier is giving change to customer.", cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer logged: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsChange from cashier."));

		assertFalse("Cashier finished giving customer change.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier gave back customer's change.", cashier.transactions.size(), 0); 
		
	
	
	}
	
	public void testCashierCustomer2(){
		//When the customer does not have enough money
		
		customer.cashier = cashier;
		
		//Check preconditions
		assertEquals("There are no existing reactions.", cashier.transactions.size(), 0); 
		
		
		//Communication between Customer and Cashier
		
		cashier.msgHereIsPayment(customer, 0, 10); //msg sent by customer to cashier
		
		assertEquals("There is one transaction.", cashier.transactions.size(), 1);
		
		assertTrue("CashierBill should contain a bill with the right customer in it.", 
				cashier.transactions.get(0).c == customer);
		
		assertTrue("Cashier is giving change to customer.", cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer logged: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsChange from cashier, owe money."));

		assertFalse("Cashier finished giving customer change.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier gave back customer's change.", cashier.transactions.size(), 0); 
		
	
	}
	
	public void testCashierWaiter1()
	{
		//Waiter is asking for bill
		
		waiter.cashier = cashier;
		
		//Check preconditions
		
		assertEquals("There are no existing checks.", cashier.orders.size(), 0); //Waiter
		
		//Communication between Waiter and Cashier
		
		cashier.msgComputeCheck("S", customer, waiter);
			
		assertEquals("There is one check.", cashier.orders.size(), 1);
				
		assertTrue("CashierCheck should contain a check with the right waiter in it.", 
				cashier.orders.get(0).w == waiter);
				
		assertTrue("CashierCheck holds correct amount customer owes.",cashier.orders.get(0).state == OrderState.pending);
				
		assertTrue("Cashier is making check.", cashier.pickAndExecuteAnAction());
				
		assertTrue("Waiter logged: " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from cashier."));

		assertFalse("Cashier finished computing check.", cashier.pickAndExecuteAnAction());
				
		assertEquals("Cashier has given waiter check.", cashier.orders.size(), 0); 
		
	
	}
	
	public void testCashierAll()
	{
		//Normal situation with all mock agents
		
		customer.cashier = cashier;
//		market.cashier = cashier;
		waiter.cashier = cashier;
		
		//Check preconditions
		assertEquals("There are no existing reactions.", cashier.transactions.size(), 0); 
//		assertEquals("There are no existing bill.", cashier.bills.size(), 0); //Market
		assertEquals("There are no existing checks.", cashier.orders.size(), 0); //Waiter
		
		//Communication between Waiter and Cashier
		
		cashier.msgComputeCheck("S", customer, waiter);
			
		assertEquals("There is one check.", cashier.orders.size(), 1);
				
		assertTrue("CashierCheck should contain a check with the right waiter in it.", 
				cashier.orders.get(0).w == waiter);
				
		assertTrue("CashierCheck holds correct amount customer owes.",cashier.orders.get(0).state == OrderState.pending);
				
		assertTrue("Cashier is making check.", cashier.pickAndExecuteAnAction());
				
		assertTrue("Waiter logged: " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from cashier."));

		assertFalse("Cashier finished computing check.", cashier.pickAndExecuteAnAction());
				
		assertEquals("Cashier has given waiter check.", cashier.orders.size(), 0); 
		
		//Communication between Customer and Cashier
		
		cashier.msgHereIsPayment(customer, 20, 10); //msg sent by customer to cashier
		
		assertEquals("There is one transaction.", cashier.transactions.size(), 1);
		
		assertTrue("CashierBill should contain a bill with the right customer in it.", 
				cashier.transactions.get(0).c == customer);
		
		assertTrue("Cashier is giving change to customer.", cashier.pickAndExecuteAnAction());
		
		assertTrue("Customer logged: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsChange from cashier."));

		assertFalse("Cashier finished giving customer change.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier gave back customer's change.", cashier.transactions.size(), 0); 
		
		//Communication between Market and Cashier
		
//		cashier.msgHereIsSupplyCheck(10, market);
		
//		assertEquals("There is one bill.", cashier.bills.size(), 1);
		
//		assertTrue("CashierSupplyBill should contain a bill with the right market in it.", 
//				cashier.bills.get(0).m == market);
		
		assertTrue("Cashier is giving change to customer.", cashier.pickAndExecuteAnAction());
		
//		assertTrue("Market logged: " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received msgHereIsMoney from cashier."));

		assertFalse("Cashier finished paying supply bill.", cashier.pickAndExecuteAnAction());
		
//		assertEquals("Cashier fulfilled payement.", cashier.bills.size(), 0); 
	
	
	}
	
	
}
