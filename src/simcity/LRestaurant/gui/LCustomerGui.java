package simcity.LRestaurant.gui;

import simcity.LRestaurant.LCustomerRole;
import simcity.LRestaurant.LWaiterRole;
//import restaurant.HostAgent.MyCustomers;

import simcity.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LCustomerGui implements Gui{

	public static List<LCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<LCustomerRole>());
	
	private LCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean hasFood = false;
	private boolean orderFood = false;
	private String food;
	private String name;
	private boolean waiting;
	LRestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable1 = 300;
    public static final int yTable1 = 250;
    
    public static final int xTable2 = 100;
    public static final int yTable2 = 200;
    
    public static final int xTable3 = 350;
    public static final int yTable3 = 100;
    
    public static final int xTable4 = 170;
    public static final int yTable4 = 60;
    
    private int rectSize = 20;
    private int xDpos = 10, yDpos = 20;

	public LCustomerGui(LCustomerRole c, String name, LRestaurantGui gui){ 
		this.name = name;
		agent = c;
		xPos = xDpos;
		yPos = yDpos;
		xDestination = xDpos;
		yDestination = yDpos;
		//maitreD = m;
		this.gui = gui;
	}
	
	
	public void setFood(String f){
    	food = f;
    }
    
    public void gotFood(){
    	hasFood = true;
    }
    
    public void ateFood(){
    	hasFood = false;
    }
    
    public void deciding(){
    	orderFood = true;
    }
    
    public void doneDeciding(){
    	orderFood = false;
    }

	@Override
	public void updatePosition() {
		if(waiting){
			yDestination = yDpos*(waitingCustomers.indexOf(agent)+1)+(waitingCustomers.indexOf(agent)*20);
		}
		
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, rectSize, rectSize);
		g.drawString(name, xPos, yPos-5);//offset so name doesn't overlap with cust
		if(orderFood){
			g.setColor(Color.BLACK);
        	g.drawString("?",xPos+10, yPos+10);//offset so food label doesn't overlap with cust
        }

		if(hasFood){
			g.setColor(Color.BLACK);
        	g.drawString(food, xPos+30, yPos+30);//offset so food label is placed on table
        }
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoWait(){
		waitingCustomers.add(agent);
		waiting = true;
		xDestination = xDpos;
		//yDestination = yDpos*(customers.indexOf(agent)+1)+(customers.indexOf(agent)*20);
	}
	

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		waitingCustomers.remove(agent);
		waiting = false;
		if(seatnumber == 1){
			xDestination = xTable1;
			yDestination = yTable1;
		}

		else if(seatnumber == 2){
			xDestination = xTable2;
			yDestination = yTable2;
		}

		else if(seatnumber == 3){
			xDestination = xTable3;
			yDestination = yTable3;
		}

		else if(seatnumber == 4){
			xDestination = xTable4;
			yDestination = yTable4;
		}

		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		waitingCustomers.remove(agent);
		waiting = false;
		xDestination = -50;
		yDestination = -50;
		command = Command.LeaveRestaurant;
	}
}