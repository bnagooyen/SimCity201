package simcity.DRestaurant;

import java.util.Vector;


public class ProducerConsumerMonitor extends Object {
	 private final int N = 5;
	    private int count = 0;
	    private Vector<DOrder> theData;
	    
	    synchronized public void insert(DOrder data) {
	    	System.out.println("inserting an order");
	        while (count == N) {
	            try{ 
	                System.out.println("\tFull, waiting");
	                wait(5000);                         // Full, wait to add
	            } catch (InterruptedException ex) {};
	        }
	            
	        insert_DOrder(data);
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
	        data = remove_DOrder();
	        count--;
	        if(count == N-1){ 
	            System.out.println("\tNot full, notify");
	            notify();                               // Not full, notify a 
	                                                    // waiting producer
	        }
	        return data;
	    }
	    
	    private void insert_DOrder(DOrder data){
	        theData.addElement(data);
	    }
	    
	    private DOrder remove_DOrder(){
	    	DOrder data = null;
	    	if(theData.size() >0) {
	        data = (DOrder) theData.firstElement();
	        theData.removeElementAt(0);
	    	}
	        return data;
	    }
	    
	    public ProducerConsumerMonitor(){
	        theData = new Vector();
	    }
	
}
