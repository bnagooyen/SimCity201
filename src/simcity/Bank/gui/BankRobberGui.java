package simcity.Bank.gui;



import java.awt.*;

import simcity.Bank.BankCustomerRole;
import simcity.gui.Gui;
import simcity.interfaces.BankCustomer;
import simcity.interfaces.BankLoanOfficer;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankRobber;
import simcity.interfaces.BankTeller;

public class BankRobberGui implements Gui{

	private BankRobber Role = null;
	private boolean isPresent = true;
	
	public boolean shot=false;

	private BankManager manager;
	public Gui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, goToTellerPosition, goToLoanPosition, leaveBank};
	private Command command=Command.noCommand;

	public static final int xTeller = 255;
	public static final int yTeller = 125;
	public static final int xLoan = 175;
	public static final int yLoan = 125;
	
	public static final int customerSize = 20;
	public static final int xOffScreen = 100;
	public static final int yOffScreen = 350;

	public BankRobberGui(BankRobber BC, BankManager m) {
		Role = BC;
		xPos = 100;
		yPos = 350;
		xDestination = 100;
		yDestination = 400;
		manager = m;
		BC.setManager(m);
		//this.gui = gui;
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
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.goToTellerPosition) Role.msgAtTellerPos();
			else if (command==Command.leaveBank) Role.msgAnimationFinishedLeaveBank();
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillRect(xPos, yPos, customerSize, customerSize);
		if(shot){
			g.setColor(Color.RED);
			g.fillOval(xPos+customerSize/2, yPos+customerSize/2, customerSize/2, customerSize/2);
		}
	}

	public boolean isPresent() {
		return true;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
    public void goToTeller() {
    	xDestination = xTeller;
        yDestination = yTeller;
        command = Command.goToTellerPosition;
    }
    
    public void goToLoanPos() {
    	xDestination = xLoan;
        yDestination = yLoan;
        command = Command.goToLoanPosition;
    }
	
	public void DoExitBank() {
		xDestination = xOffScreen;
		yDestination = yOffScreen;
		command = Command.leaveBank;
	}
}
