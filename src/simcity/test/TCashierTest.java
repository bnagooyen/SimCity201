package simcity.test;

import simcity.test.mock.MockCustomer;
import simcity.test.mock.MockWaiter;
import simcity.test.mock.TMockCustomer;
import simcity.test.mock.TMockWaiter;
import simcity.TTRestaurant.TCashierRole;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class TCashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	TCashierRole cashier;
	TMockWaiter waiter;
	TMockWaiter secondWaiter; 
	TMockCustomer customer;
	TMockCustomer secondCustomer;
	//MockMarket market;
	//MockMarket secondMarket; 
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */


	public void setUp() throws Exception{
		super.setUp();		
		//cashier = new CashierRoleTT("cashier");		
		customer = new TMockCustomer("mockcustomer");
		secondCustomer = new TMockCustomer("secondCustomer"); 
		waiter = new TMockWaiter("mockwaiter");
		secondWaiter = new TMockWaiter("secondWaiter");
		//market = new MockMarket("market"); 
		//secondMarket = new MockMarket("secondMarket"); 
	}	
	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill. 
	 */
	public void testOneNormalCustomerScenario()
	{
		//checking preconditions
		assertEquals("Cashier should have no waiters right now. It doesn't.", cashier.waiters.size(), 0); 
		assertEquals("Cashier should have no customers right now. It doesn't.", cashier.payingCustomers.size(), 0);
		//assertEquals("Cashier should have no bills right now. It doesn't.", cashier.markets.size(), 0); 
		
		waiter.cashier = cashier;
		customer.cashier = cashier;
		cashier.addFood(); 
		
		//adding a bill for the waiter to compute
		cashier.msgComputeBill(waiter, customer, "Steak");
		
		//checking postconditions and preconditions 
		assertEquals("Cashier should only have one waiter right now, but it doesn't.", cashier.waiters.size(), 1);
		assertEquals("MockWaiter doesn't have an empty log. Instead, it has " + waiter.log.toString(), 0, waiter.log.size());
		
		//calling the scheduler to calculate bill for the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged an event for receiving msgHereIsCheck but instead it's: " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier to give to customer"));
		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have no waiters again. It doesn't.", cashier.waiters.size(), 0); 
		
		
		//adding a customer the cashier to return change to.
		cashier.msgHereIsMyMoney(customer, 14.99, 14.99);
		
		//checking postconditions and preconditions
		assertEquals("Cashier should only have one customer right now, but it doesn't.", cashier.payingCustomers.size(), 1);
		assertEquals("MockCustomer doesn't have an empty log. Instead, it has " + customer.log.toString(), 0, customer.log.size());
		
		//calling the scheduler to give back change
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockCustomer should have logged an event for receiving msgHereIsChange but instead it's: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received my change. Change = 0"));
		assertEquals("Cashier should now have more money in it's budget, but it doesnt.", cashier.budget, 114.99); 
		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have no customers again. It doesn't.", cashier.payingCustomers.size(), 0);
		
		
	}//end one normal customer scenario


	//tests normal customer scenario where there are two customers and one waiter. Both have enough money to pay for bill. The cashier
	//has one bill he needs to pay for the market. 
	public void testSecondNormalCustomerScenario()
	{
		
		//checking preconditions
		assertEquals("Cashier should have no waiters right now. It doesn't.", cashier.waiters.size(), 0); 
		assertEquals("Cashier should have no customers right now. It doesn't.", cashier.payingCustomers.size(), 0);
		//assertEquals("Cashier should have no bills right now. It doesn't.", cashier.markets.size(), 0); 
		
		waiter.cashier = cashier;
		customer.cashier = cashier;
		secondCustomer.cashier = cashier; 
		//market.cashier = cashier; 
		cashier.addFood(); 
		
		//adding a bill for the waiter to compute
		cashier.msgComputeBill(waiter, customer, "Steak");
		cashier.msgComputeBill(waiter, secondCustomer, "Salad");
		//cashier.msgPayForSupply(market, 75);
		
		//checking postconditions and preconditions 
		assertEquals("Cashier should only have two waiter right now, but it doesn't.", cashier.waiters.size(), 2);
		//assertEquals("Cashier should should only have one bill right now, but it doesnt.", cashier.markets.size(), 1); 
		assertEquals("MockWaiter doesn't have an empty log. Instead, it has " + waiter.log.toString(), 0, waiter.log.size());
		//assertEquals("MockMarket doesn't have an empty log. Instead, it has " + market.log.toString(), 0, market.log.size()); 
		
		//calling the scheduler to calculate bill for the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		//checking postconditions
		assertTrue("MockWaiter should have logged an event for receiving msgHereIsCheck but instead it's: " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier to give to customer"));
		assertEquals("Cashier should have one more bill to compute. It doesn't.", cashier.waiters.size(), 1);
		
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged an event for receiving msgHereIsCheck but instead it's: " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier to give to customer"));
		assertEquals("Cashier should have no waiters again. It doesn't.", cashier.waiters.size(), 0); 
		
		
		//calling the scheduler to pay for supplies
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		//assertTrue("MockMarket should have logged an event for receiving msgPaidForStock but instead it's: " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment of 75.0 for supplies"));
		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do now. It didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should now have less money in its budget, but it doesn't.", cashier.budget, 25.0); 
		
		//adding two customers the cashier to return change to.
		cashier.msgHereIsMyMoney(customer, 15.99, 14.99);
		cashier.msgHereIsMyMoney(secondCustomer, 12.50, 8.99);
		
		//checking postconditions and preconditions
		assertEquals("Cashier should only have two customers right now, but it doesn't.", cashier.payingCustomers.size(), 2);
		assertEquals("MockCustomer doesn't have an empty log. Instead, it has " + customer.log.toString(), 0, customer.log.size());
		assertEquals("Our second MockCustomer doesn't have an empty log. Instead, it has " + secondCustomer.log.toString(), 0, secondCustomer.log.size());
		
		//calling the scheduler to give back change
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockCustomer should have logged an event for receiving msgHereIsChange but instead it's: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received my change. Change = 1"));
		assertEquals("Cashier should still have one more customer. It doesn't.", cashier.payingCustomers.size(), 1);
		assertEquals("Cashier should now have more money in its budget, but it doesn't.", cashier.budget, 39.99); 

		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("SecondMockCustomer should have logged an event for receiving msgHereIsChange but instead it's: " + secondCustomer.log.getLastLoggedEvent().toString(), secondCustomer.log.containsString("Received my change. Change = 3.51"));
		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should now have more money in its budget, but it doesn't.", cashier.budget, 48.98); 
		assertEquals("Cashier should have no customers again. It doesn't.", cashier.payingCustomers.size(), 0);
		
	}
	
	//tests when there is two customers and two waiters where only one has enough money to pay. 
	public void testThirdNormalScenario()
	{
		//checking preconditions
			assertEquals("Cashier should have no waiters right now. It doesn't.", cashier.waiters.size(), 0); 
			assertEquals("Cashier should have no customers right now. It doesn't.", cashier.payingCustomers.size(), 0);
			//assertEquals("Cashier should have no bills right now. It doesn't.", cashier.markets.size(), 0); 
				
			waiter.cashier = cashier;
			secondWaiter.cashier = cashier; 
			customer.cashier = cashier;
			secondCustomer.cashier = cashier; 
			cashier.addFood(); 
				
			//adding a bill for the waiter to compute
			cashier.msgComputeBill(waiter, customer, "Chicken");
			cashier.msgComputeBill(secondWaiter, secondCustomer, "Pizza");
				
			//checking postconditions and preconditions 
			assertEquals("Cashier should only have two waiter right now, but it doesn't.", cashier.waiters.size(), 2);
			assertEquals("MockWaiter doesn't have an empty log. Instead, it has " + waiter.log.toString(), 0, waiter.log.size());
			assertEquals("The secondMockWaiter doesn't have an empty log. It has " + secondWaiter.log.toString(), 0, secondWaiter.log.size()); 
				
			//calling the scheduler to calculate bill for the waiter
			assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
			assertTrue("MockWaiter should have logged an event for receiving msgHereIsCheck but instead it's: " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier to give to customer"));
			assertEquals("Cashier should have one more bill to compute. It doesn't.", cashier.waiters.size(), 1); 
			
				
			assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
			assertTrue("MockWaiter should have logged an event for receiving msgHereIsCheck but instead it's: " + secondWaiter.log.getLastLoggedEvent().toString(), secondWaiter.log.containsString("Received check from cashier to give to customer"));
			assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do now. It didn't.", cashier.pickAndExecuteAnAction());
			assertEquals("Cashier should have no waiters again. It doesn't.", cashier.waiters.size(), 0); 
			
				
			//adding two customers the cashier to return change to.
			cashier.msgHereIsMyMoney(customer, 6, 8.99);
			cashier.msgHereIsMyMoney(secondCustomer, 13, 10.99);
				
			//checking postconditions and preconditions
			assertEquals("Cashier should only have two customers right now, but it doesn't.", cashier.payingCustomers.size(), 2);
			assertEquals("MockCustomer doesn't have an empty log. Instead, it has " + customer.log.toString(), 0, customer.log.size());
			assertEquals("Our second MockCustomer doesn't have an empty log. Instead, it has " + secondCustomer.log.toString(), 0, secondCustomer.log.size());
				
			//calling the scheduler to give back change
			assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
			assertTrue("MockCustomer should have logged an event for receiving msgHereIsChange but instead it's: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received my change. Change = -2.99"));
			assertEquals("Cashier should now have more money in its budget, but it doesn't.", cashier.budget, 106.0); 
			assertEquals("Cashier should still have one more customer. It doesn't.", cashier.payingCustomers.size(), 1);

			assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
			assertTrue("SecondMockCustomer should have logged an event for receiving msgHereIsChange but instead it's: " + secondCustomer.log.getLastLoggedEvent().toString(), secondCustomer.log.containsString("Received my change. Change = 2.01"));
			assertEquals("Cashier should now have more money in its budget, but it doesn't.", cashier.budget, 116.99); 
			assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
			assertEquals("Cashier should have no customers again. It doesn't.", cashier.payingCustomers.size(), 0);	
		
	}
	
	//tests when there is one customer and one waiter where the customer does not have enough money to pay. The cashier has 
	//one bill to pay, which he cannot pay for. 
	public void testNonNormScenario()
	{
		//checking preconditions
		assertEquals("Cashier should have no waiters right now. It doesn't.", cashier.waiters.size(), 0); 
		assertEquals("Cashier should have no customers right now. It doesn't.", cashier.payingCustomers.size(), 0);
		//assertEquals("Cashier should have no bills right now. It doesn't.", cashier.markets.size(), 0); 
		
		waiter.cashier = cashier;
		customer.cashier = cashier;
		//market.cashier = cashier; 
		cashier.addFood(); 
		
		//adding a bill for the waiter to compute
		cashier.msgComputeBill(waiter, customer, "Steak");
		
		//checking postconditions and preconditions 
		assertEquals("Cashier should only have one waiter right now, but it doesn't.", cashier.waiters.size(), 1);
		assertEquals("MockWaiter doesn't have an empty log. Instead, it has " + waiter.log.toString(), 0, waiter.log.size());
		
		//calling the scheduler to calculate bill for the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged an event for receiving msgHereIsCheck but instead it's: " + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier to give to customer"));
		assertEquals("Cashier should have no waiters again. It doesn't.", cashier.waiters.size(), 0); 
		
		//adding bill from the market
		//cashier.msgPayForSupply(market, 110);
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		//assertTrue("MockMarket should have logged an event for receiving msgPaidForStock but instead it's: " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment of 110.0 for supplies"));
		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do now. It didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should now have less money in its budget, but it doesn't.", cashier.budget, 0.0); 
		assertEquals("Cashier should also now have a debt. It doesn't.", cashier.debt, 10.0); 
		
		//adding a customer the cashier to return change to.
		cashier.msgHereIsMyMoney(customer, 13.99, 14.99);
		
		//checking postconditions and preconditions
		assertEquals("Cashier should only have one customer right now, but it doesn't.", cashier.payingCustomers.size(), 1);
		assertEquals("MockCustomer doesn't have an empty log. Instead, it has " + customer.log.toString(), 0, customer.log.size());
		
		//calling the scheduler to give back change
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockCustomer should have logged an event for receiving msgHereIsChange but instead it's: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received my change. Change = -1"));
		assertEquals("Cashier should now have more money in it's budget, but it doesn't.", cashier.budget, 13.99); 
		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have no customers again. It doesn't.", cashier.payingCustomers.size(), 0);
		
		
	}
	
	//the cashier pays the bill from one market. 
	public void testOneNormalMarketScenario()
	{
		//checking preconditions
		//assertEquals("Cashier should have no bills right now. It doesn't.", cashier.markets.size(), 0); 
		
		//market.cashier = cashier;
		
		//adding a bill for the waiter to compute
		//cashier.msgPayForSupply(market, 50);
		
		//checking postconditions and preconditions 
		//assertEquals("Cashier should only have one bill he needs to pay, but he doesn't.", cashier.markets.size(), 1);
		//assertEquals("MockMarket doesn't have an empty log. Instead, it has " + market.log.toString(), 0, market.log.size());
		
		//calling the scheduler to calculate bill for the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		//assertTrue("MockMarket should have logged an event for receiving msgPaidForStock but instead it's: " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment of 50.0 for supplies"));
		assertEquals("Cashier should now have less money in its budget, but it doesn't.", cashier.budget, 50.0); 
		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
		//assertEquals("Cashier should have no bills to pay again. It doesn't.", cashier.markets.size(), 0); 
		
		
	}
	
	//tests when the cashier needs to pay two different markets, where the cashier does have enough money. 
	public void testSecondNormalMarket()
	{
		//checking preconditions
		//assertEquals("Cashier should have no bills right now. It doesn't.", cashier.markets.size(), 0); 
		
		//market.cashier = cashier;
		//secondMarket.cashier = cashier; 
		
		//adding a bill for the waiter to compute
		//cashier.msgPayForSupply(market, 30);
		//cashier.msgPayForSupply(secondMarket, 45); 
		
		//checking postconditions and preconditions 
		//assertEquals("Cashier should only have two bill he needs to pay, but he doesn't.", cashier.markets.size(), 2);
		//assertEquals("MockMarket doesn't have an empty log. Instead, it has " + market.log.toString(), 0, market.log.size());
		//assertEquals("The second Mock Market doesn't have an empty log. Instead, it has "+ secondMarket.log.toString(), 0, secondMarket.log.size()); 
		
		//calling the scheduler to calculate bill for the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		//assertTrue("MockMarket should have logged an event for receiving msgPaidForStock but instead it's: " + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment of 30.0 for supplies"));
		assertEquals("Cashier should now have less money in its budget, but it doesn't.", cashier.budget, 70.0); 
		
		
		//assertEquals("Cashier should only have one bill he still needs to pay, but he doesn't.", cashier.markets.size(), 1);
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		//assertTrue("The secondMockMarket should have logged an event for receiving msgPaidForStock but instead it's: " + secondMarket.log.getLastLoggedEvent().toString(), secondMarket.log.containsString("Received payment of 45.0 for supplies"));
		assertEquals("Cashier should now have less money in its budget, but it doesn't.", cashier.budget, 25.0); 

		assertFalse("Cashier's scheduler should have returned false now, since it has nothing to do. It didn't.", cashier.pickAndExecuteAnAction());
		//assertEquals("Cashier should have no bills to pay again. It doesn't.", cashier.markets.size(), 0); 
		
		
	}
	

	
}
