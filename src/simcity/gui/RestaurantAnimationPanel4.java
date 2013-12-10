package simcity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class RestaurantAnimationPanel4 extends BuildingAnimationPanel implements ActionListener {

	 private final int WINDOWX = 575;
	    private final int WINDOWY = 325;
	    
	    private final int framex = 0;
	    private final int framey = 0;
	    public static final int x_Offset = 100;
	    public static final int BTMX = 0, BTMY=0;
	    public static final int nTABLES=4;
	    //public static final int TABLE1x = 50, TABLE1y = 50; // 200, 250 orig
	    private final int tablex1 = 100;
	    private final int tabley1 = 200;
	    private final int tablew = 50;
	    private final int tableh = 50;
	   
	    private final int tablex2 = 225;
	    private final int tabley2 = 200;
	    
	    private final int tablex3 = 350;
	    private final int tabley3 = 200;
	    
	    private final int tablex4 = 475;
	    private final int tabley4 = 200;
	   
	    private final int grillx = 550;
	    private final int grilly = 0;
	    private final int grillw = 30;
	    private final int grillh = 100;
	    
	    private final int platex = 450;
	    private final int platey = 0;
	    private final int platew = 30;
	    private final int plateh = 100;
	    
	    private final int fridgex = 475;
	    private final int fridgey = 100;
	    private final int fridgew = 75;
	    private final int fridgeh = 30;
	    
	    String name;
	   // boolean isVisible = false;
	    
	    private Image bufferImage;
	    //private Dimension bufferSize;

	    //private CityGui gui;
	    
	    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	    public RestaurantAnimationPanel4(SimCityGui restaurantGui, String nm) {
	    	super(restaurantGui);
	    	//this.gui=gui;
	    	setSize(WINDOWX, WINDOWY);
	        //setVisible(true);
	        
	        //bufferSize = this.getSize();
	 
//	    	Timer timer = new Timer(20, this );
//	    	timer.start();
//	    	timer.addActionListener(this);
	    	
	    	restaurantGui.city.timer.addActionListener(this);
	    	name = nm;
	    }

	    @Override
		public void paintComponent(Graphics g) {
	    	 Graphics2D g2 = (Graphics2D)g;

	         //Clear the screen by painting a rectangle the size of the frame
	         g2.setColor(getBackground());
	         g2.fillRect(framex,  framey, this.getWidth(), this.getHeight());
	         
	         //make waiting area and waiter area
	         g2.setColor(Color.BLACK);
	         g2.drawLine(35, 35, 35, 300);
	         g2.drawLine(190, 30, 295, 30);
	         g2.drawLine(190, 0, 190, 30);
	         g2.drawLine(295, 0, 295, 30);
	         
	         //Here is the table
	         g2.setColor(Color.ORANGE);
	         g2.fillRect(tablex1, tabley1, tablew, tableh);//200 and 250 need to be table params

	         //g2.setColor(Color.ORANGE);
	         g2.fillRect(tablex2, tabley2, tablew, tableh);
	         g2.fillRect(tablex3, tabley3, tablew, tableh);
	         g2.fillRect(tablex4, tabley4, tablew, tableh);

	         // grill
	         g2.setColor(Color.black);
	         g2.fillRect(grillx, grilly, grillw, grillh);
	         
	         //plating area
	         g2.setColor(Color.gray);
	         g2.fillRect(platex, platey, platew, plateh);

	         // fridge
	         g2.setColor(Color.BLUE);
	         g2.fillRect(fridgex, fridgey, fridgew, fridgeh);
//	         
//	         for(Gui gui : guis) {
//	             if (gui.isPresent()) {
//	                 gui.updatePosition();
//	             }
//	         }
	         synchronized(guis) {
	 	        for(Gui gui : guis) {
	 	            if (gui.isPresent()) {
	 	                gui.draw(g2);
	 	            }
	 	        }
	         }
	    }
	    
	    public void actionPerformed(ActionEvent e) {
			
		      for(Gui gui : guis) {
		            if (gui.isPresent()) {
		                gui.updatePosition();
		            }
		        }
		      repaint();
	    }


		@Override
		public void addGui(simcity.gui.Gui g) {
			// TODO Auto-generated method stub
			guis.add(g);

		}



}
