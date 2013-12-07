package simcity.KRestaurant;

import java.util.Vector;

public class ProducerConsumerMonitor extends Object{
	 private final int N = 5;
	    private int count = 0;
	    private Vector<KRestaurantOrder> theData;
	    	    
	    synchronized public void insert(KRestaurantOrder data) {
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
	    
	    synchronized public KRestaurantOrder remove() {
	        KRestaurantOrder data;
//	        while(count == 0)
//	            try{ 
//	                System.out.println("\tEmpty, waiting");
//	                wait(5000);                         // Empty, wait to consume
//	            } catch (InterruptedException ex) {};
//	 
	        data = remove_KRestaurantOrder();
	        
	        if(data != null)
	        	count--;
	        if(count == N-1){ 
	            System.out.println("\tNot full, notify");
	            notify();                               // Not full, notify a 
	                                                    // waiting producer
	        }
	        return data;
	    }
	    
	    private void insert_KRestaurantOrder(KRestaurantOrder data){
	        theData.addElement(data);
	    }
	    
	    private KRestaurantOrder remove_KRestaurantOrder(){
	    	KRestaurantOrder data = null;
	    	if(theData.size() >0) {
	        data = (KRestaurantOrder) theData.firstElement();
	        theData.removeElementAt(0);
	    	}
	        return data;
	    }
	    
	    public ProducerConsumerMonitor(){
	        theData = new Vector();
	    }
	
}
