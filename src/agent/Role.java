package agent;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import simcity.test.mock.EventLog;
import simcity.PersonAgent;


//new comment to check git
/**
 * Base class for simple agents
 */
public abstract class Role {
    Semaphore stateChange = new Semaphore(1, true);//binary semaphore, fair
    public boolean isActive;
    public PersonAgent myPerson;
    public int startHour=11;
    public String purpose;
    

    public Role(PersonAgent p) {
    	myPerson=p;
    }
    
    public void timeUpdate(int hr) {
    	
    }

    /**
     * This should be called whenever state has changed that might cause
     * the agent to do something.
     */
    protected void stateChanged() {
        if(myPerson.stateChange.availablePermits()==0)
        	myPerson.stateChanged();
    }

    /**
     * Agents must implement this scheduler to perform any actions appropriate for the
     * current state.  Will be called whenever a state change has occurred,
     * and will be called repeated as long as it returns true.
     *
     * @return true iff some action was executed that might have changed the
     *         state.
     */
    public abstract boolean pickAndExecuteAnAction();

    /**
     * Return agent name for messages.  Default is to return java instance
     * name.
     */
    protected String getName() {
        return StringUtil.shortName(this);
    }

    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        print(msg, null);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        print(msg, null);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }

}

