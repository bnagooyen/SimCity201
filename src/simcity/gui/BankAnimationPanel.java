package simcity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import simcity.Bank.gui.BankCustomerGui;
import simcity.Bank.gui.BankLoanGui;
import simcity.Bank.gui.BankManagerGui;
import simcity.Bank.gui.BankTellerGui;

public class BankAnimationPanel extends BuildingAnimationPanel implements ActionListener {


    private final int WINDOWX = 575;
    private final int WINDOWY = 325;
    
    public static final int xCounter = 125;
    public static final int yCounter = 100;
    public static final int CounterSize = 25;
    public static final int CounterLength = 200;
    public static final int xDesk = 200;
    public static final int yDesk = 0;
    public static final int deskSize = 15;
    public static final int deskLength = 50;        
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

	    public BankAnimationPanel(SimCityGui restaurantGui, String nm) {
	    	super(restaurantGui);
	    	//this.gui=gui;
	    	setSize(WINDOWX, WINDOWY);
	        //setVisible(true);
	        
	        //bufferSize = this.getSize();
	 
//	    	Timer timer = new Timer(20, this );
//	    	timer.start();
//	    	timer.addActionListener(this);
	    	
	    	restaurantGui.city.timer.addActionListener(this);
	    	
	    	//name = nm;
	    }

	    public void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D)g;

	        //Clear the screen by painting a rectangle the size of the frame
	        g2.setColor(getBackground());
	        g2.fillRect(0, 0, WINDOWX, WINDOWY );

	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
	        

	        //Bank Counter
	        g2.setColor(Color.DARK_GRAY);
	        g2.fillRect(xCounter, yCounter, CounterLength, CounterSize);
	        
	        //Manager Desk
	        g2.setColor(Color.DARK_GRAY);
	        g2.fillRect(xDesk, yDesk, deskLength, deskSize);
	      

	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }

	    }

	    public void addGui(BankTellerGui gui) {
	        guis.add(gui);
	    }
	    
	    public void addGui(BankManagerGui gui) {
	        guis.add(gui);
	    }
	    
	    public void addGui(BankLoanGui gui) {
	        guis.add(gui);
	    }
	    
	    public void addGui(BankCustomerGui gui) {
	        guis.add(gui);
	    }

		@Override
		public void addGui(Gui g) {
			// TODO Auto-generated method stub
			guis.add(g);
		}


}
