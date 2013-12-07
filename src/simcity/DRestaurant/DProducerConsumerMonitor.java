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
//	        while(count == 0)
//	            try{ 
//	                System.out.println("\tEmpty, waiting");
//	                wait(5000);                         // Empty, wait to consume
//	            } catch (InterruptedException ex) {};
//	 
	        data = remove_Order();
	        if(data!=null)
	        //System.err.println("****"+data+"***"+count);
	        if(data!=null) {
	        	count--;
	        }
	        if(count==N-1) {
	        	System.out.println("\tnot full, notify");
	        	notify();
	        }
	        //System.out.println(data);
	        return data;
	    }
	    
	    private void insert_Order(DOrder data){
	        theData.addElement(data);
	    }
	    
	    private DOrder remove_Order(){
	    	DOrder data = null;
	    	if(theData.size() >0) {
	    		//System.out.println("found something!");
	    		data = (DOrder)theData.firstElement();
	    		//System.out.println(data);
	    		theData.removeElementAt(0);
	    		//System.out.println(data);
	    	}
	    	//System.out.println(data);
	        return data;
	    }
	    
	    public DProducerConsumerMonitor(){
	        theData = new Vector();
	    }
	
}
