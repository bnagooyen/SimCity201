package simcity.TRestaurant;

import java.util.Vector;

public class OrderStand extends Object {
	private final int N = 5;
	private int count = 0;
	private Vector <RotatingOrders> rotatingTable;
	
	
	synchronized public void insert(RotatingOrders o) {
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
	
	synchronized public RotatingOrders remove() {
		RotatingOrders data; 
		data = remove_RotatingOrders(); 
	    count--;
	    if(count == N-1){ 
	         System.out.println("\tNot full, notify");
	         notify();                               // Not full, notify a 
	                                                    // waiting producer
	    }
		return data; 
	}
	
	public void insert_RotatingOrders(RotatingOrders o) {
		rotatingTable.addElement(o); 
	}
	
	private RotatingOrders remove_RotatingOrders() {
		RotatingOrders data = null; 
		if (rotatingTable.size() > 0) {
			data = (RotatingOrders) rotatingTable.firstElement(); 
			rotatingTable.removeElementAt(0);
		}
		return data; 
	}
	
	public OrderStand() {
		rotatingTable = new Vector(); 
	}
	
}
