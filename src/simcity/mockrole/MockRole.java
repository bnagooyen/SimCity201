package simcity.mockrole;

import simcity.PersonAgent;
import agent.Role;

/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class MockRole extends Role {
	private String name;

	public MockRole(String name, PersonAgent p) {
		super(p);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + name;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

}
