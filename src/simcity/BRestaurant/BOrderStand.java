package simcity.BRestaurant;

import java.util.Vector;

public class BOrderStand extends Object {
	private final int N = 5;
	private int count = 0;
	private Vector <BRotatingOrders> rotatingTable;
	
	
	synchronized public void insert(BRotatingOrders o) {
    	System.out.println("inserting an order");
        while (count == N) {
            try{ 
                System.out.println("\tFull, waiting");
                wait(5000);                        
            } catch (InterruptedException ex) {};
        }
            
        insert_RotatingOrders(o);
        count++;
        if(count == 1) {
            System.out.println("\tNot Empty, notify");
            notify();                               
        }
	}
	
	synchronized public BRotatingOrders remove() {
		BRotatingOrders data; 
		data = remove_RotatingOrders(); 
	    count--;
	    if(count == N-1){ 
	         System.out.println("\tNot full, notify");
	         notify();                               // Not full, notify a 
	                                                    // waiting producer
	    }
		return data; 
	}
	
	public void insert_RotatingOrders(BRotatingOrders o) {
		rotatingTable.addElement(o); 
	}
	
	private BRotatingOrders remove_RotatingOrders() {
		BRotatingOrders data = null; 
		if (rotatingTable.size() > 0) {
			data = (BRotatingOrders) rotatingTable.firstElement(); 
			rotatingTable.removeElementAt(0);
		}
		return data; 
	}
	
	public BOrderStand() {
		rotatingTable = new Vector(); 
	}
	
}
