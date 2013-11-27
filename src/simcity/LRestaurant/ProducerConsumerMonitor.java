package simcity.LRestaurant;

import java.util.Vector;

public class ProducerConsumerMonitor extends Object{
	 private final int N = 5;
	    private int count = 0;
	    private Vector<LRestaurantOrder> theData;
	    	    
	    synchronized public void insert(LRestaurantOrder data) {
	    	System.out.println("inserting an order");
	        while (count == N) {
	            try{ 
	                System.out.println("\tFull, waiting");
	                wait(5000);                         // Full, wait to add
	            } catch (InterruptedException ex) {};
	        }
	            
	        insert_LRestaurantOrder(data);
	        count++;
	        if(count == 1) {
	            System.out.println("\tNot Empty, notify");
	            notify();                               // Not empty, notify a 
	                                                    // waiting consumer
	        }
	    }
	    
	    synchronized public LRestaurantOrder remove() {
	        LRestaurantOrder data;

	        data = remove_LRestaurantOrder();
	        count--;
	        if(count == N-1){ 
	            System.out.println("\tNot full, notify");
	            notify();                               // Not full, notify a 
	                                                    // waiting producer
	        }
	        return data;
	    }
	    
	    private void insert_LRestaurantOrder(LRestaurantOrder data){
	        theData.addElement(data);
	    }
	    
	    private LRestaurantOrder remove_LRestaurantOrder(){
	    	LRestaurantOrder data = null;
	    	if(theData.size() >0) {
	        data = (LRestaurantOrder) theData.firstElement();
	        theData.removeElementAt(0);
	    	}
	        return data;
	    }
	    
	    public ProducerConsumerMonitor(){
	        theData = new Vector();
	    }
	
}
