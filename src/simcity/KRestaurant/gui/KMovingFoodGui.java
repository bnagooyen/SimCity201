package simcity.KRestaurant.gui;

import simcity.KRestaurant.KCookRole;
import simcity.KRestaurant.KCustomerRole;
import simcity.KRestaurant.KHostRole;
import simcity.KRestaurant.KWaiterRole;
import simcity.gui.Gui;
import simcity.gui.SimCityGui;

import java.awt.*;

public class KMovingFoodGui implements Gui{

	private KWaiterRole agent = null;
	private boolean isPresent = false;

	SimCityGui gui;
	KCookRole cook;
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable1 = 100;
    public static final int yTable1 = 200;
    
    public static final int xTable2 = 225;
    public static final int yTable2 = 200;
    
    public static final int xTable3 = 350;
    public static final int yTable3 = 200;
	
    public static final int xTable4 = 475;
    public static final int yTable4 = 200;
    
    public static final int xplate = 455;
    public static final int yplate1 = 5;
    public static final int yplate2 = 25;
    public static final int yplate3 = 45;
    public static final int yplate4 = 65;

	public final int width = 15;
	public final int height = 15;

	public int table;
	public String type;
	
	public KMovingFoodGui(KCookRole c, KWaiterRole w, SimCityGui gui2, int ypos, String s){ //HostAgent m) {
		cook = c;
		agent = w;
		xPos = 500;
		this.yPos = 100;
		xDestination = 555;
		yDestination = ypos;
		type = s;
		isPresent = true;
		
		this.gui = gui2;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if(xPos == xDestination && yPos == yDestination){
			command=Command.noCommand;
		}
		//}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(xPos, yPos, width, height);
		g.setColor(Color.BLACK);
		if(type.equals("Steak")) {
	        g.drawString("St", xPos + 1, yPos + 12);
		}
		else if (type.equals("Chicken")){
	        g.drawString("Ch", xPos + 1, yPos + 12);
		}
		else if (type.equals("Pizza")) {
	        g.drawString("P", xPos + 3, yPos + 12);
		}
		else if (type.equals("Salad")) {
	        g.drawString("S", xPos + 3, yPos + 12);
		}
	}

	public void GoToGrill(int g) {
		xDestination = 755;
		switch(g){
			case 0: yDestination = yplate1;
				break;
			case 1: yDestination = yplate2;
				break;
			case 2: yDestination = yplate3;
				break;
			case 3: yDestination = yplate4;
				break;
		}
	}
	public void GoToPlating(int p) {
		xDestination = xplate;
	}
	
	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void GoTo(int table) {
		
	}
	
	public void DoGoToSeat(int seatnumber) {
		table = seatnumber;
		if(seatnumber == 1)
		{
			xDestination = xTable1+20;
			yDestination = yTable1;
			command = Command.GoToSeat;
		}
		else if(seatnumber == 2)
		{
			xDestination = xTable2+20;
			yDestination = yTable2;
			command = Command.GoToSeat;
		}
		else if(seatnumber == 3)
		{
			xDestination = xTable3+20;
			yDestination = yTable3;
			command = Command.GoToSeat;
		}
		else if(seatnumber == 4)
		{
			xDestination = xTable4+20;
			yDestination = yTable4;
			command = Command.GoToSeat;
		}
	}


}
