package simcity.KRestaurant.gui;

import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterRole;

import java.awt.*;

public class KFoodGui implements KGui{

	private KWaiterRole agent = null;
	private boolean isPresent = false;

	KRestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable1 = 200;
    public static final int yTable1 = 200;
    
    public static final int xTable2 = 350;
    public static final int yTable2 = 200;
    
    public static final int xTable3 = 500;
    public static final int yTable3 = 200;
	
    public static final int xTable4 = 650;
    public static final int yTable4 = 200;
    
	public final int width = 15;
	public final int height = 15;

	public int table;
	public String type;
	
	public KFoodGui(String s, KRestaurantGui gui, int table){ 
		this.table = table;
		isPresent = true;
		type = s;
			if(table == 1) {
				xPos = xTable1 +20;
				yPos = yTable1 ;
			}
			else if(table == 2) {
				xPos = xTable2 +20;
				yPos = yTable2 ;
			}
			else if(table == 3) {
				xPos = xTable3 +20;
				yPos = yTable3 ;
			}
			else if(table == 4) {
				xPos = xTable4 +20;
				yPos = yTable4 ;
			}
		this.gui = gui;
	}

	public void updatePosition() {

			command=Command.noCommand;
//		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.lightGray);
		g.fillRect(xPos, yPos, width, height);
		g.setColor(Color.BLACK);
		if(type.equals("Steak")) {
	        g.drawString("St?", xPos , yPos + 12);
		}
		else if (type.equals("Chicken")){
	        g.drawString("Ch?", xPos , yPos + 12);
		}
		else if (type.equals("Pizza")) {
	        g.drawString("P?", xPos + 2, yPos + 12);
		}
		else if (type.equals("Salad")) {
	        g.drawString("S?", xPos + 2, yPos + 12);
		}
	}

	public void gotFood() {
		isPresent = false;
	}
	public boolean isPresent() {
		return isPresent;
	}


	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void GoTo(int table) {
		
	}
	
}
