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

import simcity.housing.gui.ResidentGui;

public class HouseAnimationPanel extends BuildingAnimationPanel implements ActionListener {

	 private final int WINDOWX = 575;
	    private final int WINDOWY = 325;
	    public static final int x_Offset = 100;
	    public static final int BTMX = 0, BTMY=0;
	    public static final int nTABLES=4;
	    //public static final int TABLE1x = 50, TABLE1y = 50; // 200, 250 orig
	    public static final int TABLESZ_xy=50;
	    public static final int TABLE_gap=50;
	    public static final int TABLES_perRow = 4;
	    
	    public static final int allKitchenItems_x = 570;
	    
	    public static final int refrig_y = 300;
	    public static final int refrig_xsz=40;
	    public static final int refrig_ysz=60;

	    public static final int grill_xsz= 40;
	    public static final int grill_ysz = 30;
	  
	    public static final int grillPizza_y =260;
	    public static final int grillChicken_y =220;
	    public static final int grillSteak_y =180;
	    public static final int grillSalad_y =140;
	    public static final int plating_ysz=80;
	    public static final int plating_x=50;
	    
	    String name;
	   // boolean isVisible = false;
	    
	    private Image bufferImage;
	    //private Dimension bufferSize;

	    //private CityGui gui;
	    
	   // private List<Gui> guis = new ArrayList<Gui>();

	    public HouseAnimationPanel(SimCityGui restaurantGui, String nm) {
	    	super(restaurantGui);
	    	//this.gui=gui;
	    	setSize(WINDOWX, WINDOWY);
	        //setVisible(true);
	        
	        //bufferSize = this.getSize();
//	 
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
	        int beginX = 0; 
	        int beginY = 0; 
	        g2.setColor(getBackground());
	        g2.fillRect(beginX, beginY, WINDOWX, WINDOWY );

	        //Here is the table
	        int w = 70; 
	        int h = 70; 
	        g2.setColor(Color.yellow);
	        g2.fillRect(240, 180, w, h);
	           
	        //Here is the bed
	        g2.setColor(Color.blue);
	        g2.fillRect(0, 0, 100, 160);
	           
	        int kitchenW = 200; 
	        int kitchenH = 200; 
	        g2.setColor(Color.lightGray); 
	        g2.fillRect(450, 0, kitchenW, kitchenH);
	        
	        g2.setColor(Color.black); 
	        g2.fillRect(520, 0, 100, 150);
	        
	        g2.setColor(Color.white);
	        g2.fillRect(520, 150, 60, 50);
	        

	        g2.setColor(Color.red); 
	        g2.fillOval(530, 5, 15, 15);
	        g2.fillOval(530, 30, 15, 15);
	        g2.fillOval(550, 5, 15, 15);
	        g2.fillOval(550, 30, 15, 15);
	           

	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	               gui.updatePosition();
	            }
	        }

	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	             }
	        }
	    }

	    public void addGui(ResidentGui gui) {
	        guis.add(gui);
	    }

		@Override
		public void addGui(Gui g) {
			// TODO Auto-generated method stub
			guis.add(g);
		}


}
