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

import simcity.housing.gui.TenantGui;

public class ApartmentAnimationPanel extends BuildingAnimationPanel implements ActionListener {

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

	    public ApartmentAnimationPanel(SimCityGui restaurantGui, String nm) {
	    	super(restaurantGui);
	    	//this.gui=gui;
	    	setSize(WINDOWX, WINDOWY);
	        //setVisible(true);
	        
	        //bufferSize = this.getSize();
	 
	    	//Timer timer = new Timer(20, this );
	    	//timer.start();
	    	restaurantGui.city.timer.addActionListener(this);
	    	name = nm;
	    }

	    public void paintComponent(Graphics g) {
	    	Graphics2D g2 = (Graphics2D)g;
	           
	        //Clear the screen by painting a rectangle the size of the frame
	        g2.setColor(getBackground());
	        g2.fillRect(0, 0, WINDOWX, WINDOWY );
	        
	        //Here are the walls
	        g2.setColor(Color.black);
	        g2.fillRect(180, 0, 10, WINDOWY);
	        g2.fillRect(370, 0, 10, WINDOWY);

	        //Here are the tables
	        int w = 50; 
	        int h = 50; 
	        g2.setColor(Color.PINK);
	        g2.fillRect(30, 60, w, h);
	        g2.setColor(Color.CYAN);
	        g2.fillRect(310, 100, w, h);
	        g2.setColor(Color.white);
	        g2.fillRect(500, 170, w, h);
	           
	        //Here are the beds
	        g2.setColor(Color.red);
	        g2.fillRect(130, 120, 50, 80);
	        g2.setColor(Color.blue);
	        g2.fillRect(190, 80, 50, 80);
	        g2.setColor(Color.black);
	        g2.fillRect(380, 220, 50, 80);
	        
	        //Here are the kitchens
	        int kitchenW = 80; 
	        int kitchenH = 130; 
	        g2.setColor(Color.lightGray); 
	        g2.fillRect(0, 170, kitchenW, kitchenH);
	        g2.fillRect(240, 240, kitchenH, kitchenW);
	        g2.fillRect(490, 0, kitchenW, kitchenH);
	        
	        g2.setColor(Color.white);
	        g2.fillRect(0, 170, 30, 90);
	        g2.setColor(Color.red);
	        g2.fillRect(0, 260, 30, 40);
	        
	        g2.setColor(Color.black);
	        g2.fillRect(240, 290, 90, 50); 
	        g2.setColor(Color.white);
	        g2.fillRect(330, 290, 40, 30);
	        
	        g2.setColor(Color.white);
	        g2.fillRect(540, 0, 50, 90); 
	        g2.setColor(Color.black);
	        g2.fillRect(540, 90, 30, 40);

	        
	        g2.setColor(Color.red); 
	        g2.fillOval(3, 175, 10, 10);
	        g2.fillOval(3, 190, 10, 10);
	        g2.fillOval(18, 175, 10, 10);
	        g2.fillOval(18, 190, 10, 10);
	        
	        g2.fillOval(245, 290, 10, 10);
	        g2.fillOval(245, 305, 10, 10);
	        g2.fillOval(260, 290, 10, 10);
	        g2.fillOval(260, 305, 10, 10);
	        

	        g2.fillOval(542, 5, 10, 10);
	        g2.fillOval(542, 20, 10, 10);
	        g2.fillOval(557, 5, 10, 10);
	        g2.fillOval(557, 20, 10, 10);
	        

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

	    public void addGui(TenantGui gui) {
	        guis.add(gui);
	    }
	    

		@Override
		public void addGui(Gui g) {
			// TODO Auto-generated method stub
			guis.add(g);
		}


}
