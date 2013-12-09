package simcity.Drew_restaurant;

import java.util.Vector;

public class ProducerConsumerMonitor extends Object{
	 private final int N = 5;
	    private int count = 0;
	    private Vector<Drew_RestaurantOrder> theData;
	    	    
	    synchronized public void insert(Drew_RestaurantOrder data) {
	    	System.out.println("inserting an order");
	        while (count == N) {
	            try{ 
	                System.out.println("\tFull, waiting");
	                wait(5000);                         // Full, wait to add
	            } catch (InterruptedException ex) {};
	        }
	            
	        insert_KRestaurantOrder(data);
	        count++;
	        if(count == 1) {
	            System.out.println("\tNot Empty, notify");
	            notify();                               // Not empty, notify a 
	                                                    // waiting consumer
	        }
	    }
	    
	    synchronized public Drew_RestaurantOrder remove() {
	        Drew_RestaurantOrder data;
	 
	        data = remove_Drew_RestaurantOrder();
	        count--;
	        if(count == N-1){ 
	            System.out.println("\tNot full, notify");
	            notify();                               // Not full, notify a 
	                                                    // waiting producer
	        }
	        return data;
	    }
	    
	    private void insert_KRestaurantOrder(Drew_RestaurantOrder data){
	        theData.addElement(data);
	    }
	    
	    private Drew_RestaurantOrder remove_Drew_RestaurantOrder(){
	    	Drew_RestaurantOrder data = null;
	    	if(theData.size() >0) {
	        data = (Drew_RestaurantOrder) theData.firstElement();
	        theData.removeElementAt(0);
	    	}
	        return data;
	    }
	    
	    public ProducerConsumerMonitor(){
	        theData = new Vector();
	    }
	
}
