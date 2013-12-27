package simcity.DRestaurant;

import java.util.Vector;


public class DProducerConsumerMonitor extends Object {
	private final int N=5;
	private int count = 0;
	private Vector<DOrder> theData;
	
	 synchronized public void insert(DOrder data) {
	    	//System.out.println("inserting an order");
	        while (count == N) {
	            try{ 
	                System.out.println("\tFull, waiting");
	                wait(5000);                         // Full, wait to add
	            } catch (InterruptedException ex) {};
	        }
	            
	        insert_Order(data);
	        count++;
	        if(count == 1) {
	            System.out.println("\tNot Empty, notify");
	            notify();                               // Not empty, notify a 
	                                                    // waiting consumer
	        }
	    }
	    
	    synchronized public DOrder remove() {
	    	DOrder data;

	        data = remove_Order();
	        if(data!=null)
	       
	        if(data!=null) {
	        	count--;
	        }
	        if(count==N-1) {
	        	System.out.println("\tnot full, notify");
	        	notify();
	        }

	        return data;
	    }
	    
	    private void insert_Order(DOrder data){
	        theData.addElement(data);
	    }
	    
	    private DOrder remove_Order(){
	    	DOrder data = null;
	    	if(theData.size() >0) {

	    		data = (DOrder)theData.firstElement();
	    		theData.removeElementAt(0);
	    	}
	        return data;
	    }
	    
	    public DProducerConsumerMonitor(){
	        theData = new Vector();
	    }
	
}
