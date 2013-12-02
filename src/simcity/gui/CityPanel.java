package simcity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.sun.tools.javac.util.List;

import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;

public class CityPanel extends JPanel implements ActionListener {

	 public static final int BTMX = 0, BTMY=0;
	public static final int CITY_WIDTH = 575, CITY_HEIGHT = 385;
    public static final int streetWidth = 30;
    public static final int sidewalkWidth = 20;
    public static final int housingWidth=30;
    public static final int housingLength=35;
    public static final int parkingGap = 22;
    public static final int yardSpace=11;
    public Color background;
	boolean addingObject = false;
	SimCityGui gui;
	ArrayList<Building> buildings;
	String name = "City Panel";
    private ArrayList<Gui> guis = new ArrayList<Gui>();
    
    //private BufferedImage house = null;
    private BufferedImage houseL = null;
    private BufferedImage houseR = null;
    private BufferedImage apartmentL = null;
    private BufferedImage apartmentR = null;
    private BufferedImage restaurant = null;
    private BufferedImage bank = null;
    private BufferedImage market = null;
    
    public Timer timer;
	
	public CityPanel(SimCityGui city) {
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
//		background = new Color(50, 64, 0);
		background = Color.white;
		gui = city;
		
		 buildings = new ArrayList<Building>();
		
		//buildings col1
		buildings.add(new Building(yardSpace, streetWidth+sidewalkWidth, Color.blue, "House 1"));
		buildings.add(new Building(yardSpace, streetWidth+sidewalkWidth+housingLength+ parkingGap, Color.cyan, "Apartment 1"));
		buildings.add(new Building(yardSpace, streetWidth+sidewalkWidth+2*housingLength+ 2*parkingGap, Color.blue, "House 2"));
		buildings.add(new Building(yardSpace, streetWidth+sidewalkWidth+3*housingLength+ 3*parkingGap, Color.cyan,"Apartment 2"));
		buildings.add(new Building(yardSpace, streetWidth+sidewalkWidth+4*housingLength+ 5*parkingGap, Color.blue, "House 3"));
		
		//buildings col2
		buildings.add(new Building(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, Color.orange, "Market 1"));
		buildings.add(new Building(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength+ sidewalkWidth + parkingGap,Color.blue, "House 4"));
		buildings.add(new Building(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, Color.cyan,"Apartment 3"));
		buildings.add(new Building(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, Color.blue, "House 5"));
		buildings.add(new Building(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, Color.green, "Bank 1"));
		
		//buildings col3
		buildings.add(new Building(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, Color.red, "Restaurant 1"));
		buildings.add(new Building(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength + sidewalkWidth+ parkingGap, Color.cyan,"Apartment 4"));
		buildings.add(new Building(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, Color.blue,"House 6"));
		buildings.add(new Building(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, Color.cyan,"Apartment 5"));
		buildings.add(new Building(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap,  Color.red,"Restaurant 2"));
		
		//buildings col4
		buildings.add(new Building(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, Color.red, "Restaurant 3"));
		buildings.add(new Building(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, Color.blue,"House 7"));
		buildings.add(new Building(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap,Color.cyan, "Apartment 6"));
		buildings.add(new Building( 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap,Color.blue, "House 8"));
		buildings.add(new Building(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, Color.orange, "Market 2"));
		
		//buildings col5
		buildings.add(new Building(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, Color.red, "Restaurant 4"));
		buildings.add(new Building(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, Color.cyan,"Apartment 7"));
		buildings.add(new Building(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, Color.blue, "House 9"));
		buildings.add(new Building( 3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, Color.cyan,"Apartment 8"));
		buildings.add(new Building(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, Color.red, "Restaurant 5"));
		
		//buildings col6
		buildings.add(new Building(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, Color.orange,"Market 3"));
		buildings.add(new Building(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, Color.blue,"House 10"));
		buildings.add(new Building(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap,Color.cyan, "Apartment 9"));
		buildings.add(new Building( 3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, Color.blue,"House 11"));
		buildings.add(new Building(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, Color.red, "Restaurant 6"));
		
		//buildings col7
		buildings.add(new Building(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, Color.green,"Bank 2"));
		buildings.add(new Building(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, Color.blue,"House 12"));
		buildings.add(new Building(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, Color.cyan,"Apartment 10"));
		buildings.add(new Building( 4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, Color.blue, "House 13"));
		buildings.add(new Building(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, Color.orange,"Market 4"));
		
		//buildings col8
		buildings.add(new Building(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+sidewalkWidth,Color.cyan, "Apartment 11"));
		buildings.add(new Building(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, Color.blue, "House 14"));
		buildings.add(new Building(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, Color.cyan,"Apartment 12"));
		buildings.add(new Building( 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, Color.blue, "House 15"));
		buildings.add(new Building(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, Color.gray, "Homeless Shelter"));
		
		timer =  new Timer(20, this);
    	timer.start();
		


    	MouseListener mouseListener = createMouseListener();
    	   addMouseListener(mouseListener);
		//addMouseMotionListener(this);
    	   
    	   try {
               StringBuilder path = new StringBuilder("images"+ File.separator);
               houseL = ImageIO.read(new File(path.toString() + "houseL.png"));
               houseR = ImageIO.read(new File(path.toString() + "houseR.png"));
               apartmentR = ImageIO.read(new File(path.toString() + "apartmentR.png"));
               apartmentL = ImageIO.read(new File(path.toString() + "apartmentL.png"));
               market= ImageIO.read(new File(path.toString() + "market.png"));
               bank = ImageIO.read(new File(path.toString() + "bank.png"));
               restaurant=ImageIO.read(new File(path.toString() + "restaurant.png"));
               
       } catch (IOException e) {
     	  System.out.println("couldn't find file");
       }
    	   
    	   
    	   repaint();

	
	}
	
	protected MouseListener createMouseListener() {
	    MouseListener l = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
//				System.err.println("***clicked");
				for (Building b: buildings) {
				if (b.contains(arg0.getX(), arg0.getY())) {
					//city.info.setText(c.ID);
					System.err.println("clicked on "+ b.name);
					gui.view.setView(b.name);
//					AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Building Selected: " + c.ID);
				}
			}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    	
	    
	    };
	    
	   return l;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}
	
	 @Override
	public void paintComponent(Graphics g) {
		 
		 Graphics2D g2 = (Graphics2D)g;
		 
	      g2.setColor(background);
	        g2.fillRect(BTMX, BTMY, CITY_WIDTH, CITY_HEIGHT); 

	     //RECTANGLES

		 for(Building b: buildings) {
			 g2.setColor(b.color);
			 g2.fillRect(b.x, b.y, b.rectangle.width, b.rectangle.height);
		 }
		 
		 //IMAGES
		 
		
			//buildings col1
			 g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth, null);
			 g2.drawImage(apartmentR, yardSpace, streetWidth+sidewalkWidth+housingLength+ parkingGap, null);
			 g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth+2*housingLength+ 2*parkingGap, null);
			 g2.drawImage(apartmentR, yardSpace, streetWidth+sidewalkWidth+3*housingLength+ 3*parkingGap, null);
			 g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth+4*housingLength+ 5*parkingGap, null);
			
			//buildings col2
			 g2.drawImage(market, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, null);
			 g2.drawImage(houseL, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength+ sidewalkWidth + parkingGap,null);
			 g2.drawImage(apartmentL, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, null);
			 g2.drawImage(houseL, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, null);
			 g2.drawImage(bank, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, null);
			
			//buildings col3
			 g2.drawImage(restaurant, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, null);
			 g2.drawImage(apartmentR, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength + sidewalkWidth+ parkingGap,null);
			 g2.drawImage(houseR, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, null);
			 g2.drawImage(apartmentR, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, null);
			 g2.drawImage(restaurant, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap,  null);
			
			//buildings col4
			 g2.drawImage(restaurant, 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, null);
			 g2.drawImage(houseL, 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, null);
			 g2.drawImage(apartmentL, 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap,null);
			 g2.drawImage(houseL, 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap,null);
			 g2.drawImage(market, 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, null);
			
			//buildings col5
			 g2.drawImage(restaurant, 3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, null);
			 g2.drawImage(apartmentR, 3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, null);
			 g2.drawImage(houseR, 3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, null);
			 g2.drawImage(apartmentR,  3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap,null);
			 g2.drawImage(restaurant, 3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, null);
			
			//buildings col6
			 g2.drawImage(market, 3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, null);
			 g2.drawImage(houseL, 3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, null);
			 g2.drawImage(apartmentL, 3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap,null);
			 g2.drawImage(houseL,  3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, null);
			 g2.drawImage(restaurant, 3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap,null);
			
			//buildings col7
			 g2.drawImage(bank, 4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, null);
			 g2.drawImage(houseR, 4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap,null);
			 g2.drawImage(apartmentR, 4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, null);
			 g2.drawImage(houseR,  4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, null);
			 g2.drawImage(market, 4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, null);
			
			//buildings col8
			 g2.drawImage(apartmentL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+sidewalkWidth,null);
			 g2.drawImage(houseL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap,null);
			 g2.drawImage(apartmentL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, null);
			 g2.drawImage(houseL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap,null);
			 g2.drawImage(apartmentL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, null);
			
		 
		 //ROADS
			int counter=0;
			int i=yardSpace+housingWidth;
			while(i<550) {
				g2.setColor(Color.gray);
				g2.fillRect(i, 0, sidewalkWidth, CITY_HEIGHT);
	
				if(counter%2==0) i += streetWidth+sidewalkWidth-1;
				else i += 2*housingWidth+ streetWidth+2;
				counter++;
			}
			
			g2.fillRect(0, streetWidth, CITY_WIDTH, sidewalkWidth);
			g2.fillRect(0, streetWidth+5*housingLength+ sidewalkWidth+ 5*parkingGap, CITY_WIDTH, sidewalkWidth);
			
			g2.setColor(Color.DARK_GRAY);
			int w=housingWidth+streetWidth;
			while(w<500) {
				g2.fillRect(w, 0, streetWidth, CITY_HEIGHT);
	
				w+=2*sidewalkWidth+2*housingWidth+yardSpace+streetWidth;
			}
			
			g2.fillRect(0, 0, CITY_WIDTH, streetWidth);
			g2.fillRect(0, streetWidth+5*housingLength+ sidewalkWidth+ 5*parkingGap+sidewalkWidth, CITY_WIDTH, streetWidth);
			
		
			//GUIS (ADJUST ORDER LATER)
			
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

	 public void addGui(Gui g) {
		 guis.add(g);
	 }

	
}
