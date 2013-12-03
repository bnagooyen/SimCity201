package simcity.test.mock;

import simcity.interfaces.DCook;
import simcity.interfaces.Market;

public class MockCook extends Mock implements DCook {
	public EventLog log = new EventLog();
	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgShouldIPayThisBill(double amt, Market ma) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received should I accept"));
	}
	
	

}
