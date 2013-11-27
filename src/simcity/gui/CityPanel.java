package simcity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import simcity.gui.trace.AlertLog;
import simcity.gui.trace.AlertTag;

public class CityPanel extends TSimCityPanel implements ActionListener, MouseMotionListener {

	public static final int CITY_WIDTH = 575, CITY_HEIGHT = 385;
    public static final int streetWidth = 30;
    public static final int sidewalkWidth = 20;
    public static final int housingWidth=30;
    public static final int housingLength=35;
    public static final int parkingGap = 22;
    public static final int yardSpace=11;
    
    //private BufferedImage house = null;
    private BufferedImage houseL = null;
    private BufferedImage houseR = null;
    private BufferedImage apartmentL = null;
    private BufferedImage apartmentR = null;
    private BufferedImage restaurant = null;
    private BufferedImage bank = null;
    private BufferedImage market = null;
    
    
    
	boolean addingObject = false;
	CityComponent temp;
	
	String name = "City Panel";
	
	public CityPanel(SimCityGui city) {
		super(city);
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
		background = Color.white;
		
		
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
    	

		
		//buildings col1
		this.addStatic(new CityHouse(yardSpace, streetWidth+sidewalkWidth));
		this.addStatic(new CityApartment(yardSpace, streetWidth+sidewalkWidth+housingLength+ parkingGap));
		this.addStatic(new CityHouse(yardSpace, streetWidth+sidewalkWidth+2*housingLength+ 2*parkingGap, "House 2"));
		this.addStatic(new CityApartment(yardSpace, streetWidth+sidewalkWidth+3*housingLength+ 3*parkingGap, "Apartment 2"));
		this.addStatic(new CityHouse(yardSpace, streetWidth+sidewalkWidth+4*housingLength+ 5*parkingGap, "House 3"));
		
		//buildings col2
		this.addStatic(new CityMarket(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth));
		this.addStatic(new CityHouse(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength+ sidewalkWidth + parkingGap, "House 4"));
		this.addStatic(new CityApartment(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, "Apartment 3"));
		this.addStatic(new CityHouse(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, "House 5"));
		this.addStatic(new CityBank(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap));
		
		//buildings col3
		this.addStatic(new CityRestaurant(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth));
		this.addStatic(new CityApartment(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength + sidewalkWidth+ parkingGap, "Apartment 4"));
		this.addStatic(new CityHouse(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, "House 6"));
		this.addStatic(new CityApartment(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, "Apartment 5"));
		this.addStatic(new CityRestaurant(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, "Restaurant 2"));
		
		//buildings col4
		this.addStatic(new CityRestaurant(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, "Restaurant 3"));
		this.addStatic(new CityHouse(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, "House 7"));
		this.addStatic(new CityApartment(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, "Apartment 6"));
		this.addStatic(new CityHouse( 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, "House 8"));
		this.addStatic(new CityMarket(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, "Market 2"));
		
		//buildings col5
		this.addStatic(new CityRestaurant(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, "Restaurant 4"));
		this.addStatic(new CityApartment(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, "Apartment 7"));
		this.addStatic(new CityHouse(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, "House 9"));
		this.addStatic(new CityApartment( 3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, "Apartment 8"));
		this.addStatic(new CityRestaurant(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, "Restaurant 5"));
		
		//buildings col6
		this.addStatic(new CityMarket(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, "Market 3"));
		this.addStatic(new CityHouse(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, "House 10"));
		this.addStatic(new CityApartment(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, "Apartment 9"));
		this.addStatic(new CityHouse( 3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, "House 11"));
		this.addStatic(new CityRestaurant(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, "Restaurant 6"));
		
		//buildings col7
		this.addStatic(new CityBank(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, "Bank 2"));
		this.addStatic(new CityHouse(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, "House 12"));
		this.addStatic(new CityApartment(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, "Apartment 10"));
		this.addStatic(new CityHouse( 4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, "House 13"));
		this.addStatic(new CityMarket(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, "Market 4"));
		
		//buildings col8
		this.addStatic(new CityApartment(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+sidewalkWidth, "Apartment 11"));
		this.addStatic(new CityHouse(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, "House 14"));
		this.addStatic(new CityApartment(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, "Apartment 12"));
		this.addStatic(new CityHouse( 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, "House 15"));
		this.addStatic(new CityHomelessShelter(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap));
		
		
		//drawing images
		
		
		//buildings col1
		this.addStatic(new CityBuildingImage(yardSpace, streetWidth+sidewalkWidth, houseR));
		this.addStatic(new CityBuildingImage(yardSpace, streetWidth+sidewalkWidth+housingLength+ parkingGap, apartmentR));
		this.addStatic(new CityBuildingImage(yardSpace, streetWidth+sidewalkWidth+2*housingLength+ 2*parkingGap, houseR));
		this.addStatic(new CityBuildingImage(yardSpace, streetWidth+sidewalkWidth+3*housingLength+ 3*parkingGap, apartmentR));
		this.addStatic(new CityBuildingImage(yardSpace, streetWidth+sidewalkWidth+4*housingLength+ 5*parkingGap, houseR));
		
		//buildings col2
		this.addStatic(new CityBuildingImage(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, market));
		this.addStatic(new CityBuildingImage(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength+ sidewalkWidth + parkingGap, houseL));
		this.addStatic(new CityBuildingImage(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, apartmentL));
		this.addStatic(new CityBuildingImage(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, houseL));
		this.addStatic(new CityBuildingImage(yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, bank));
		
		//buildings col3
		this.addStatic(new CityBuildingImage(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, restaurant));
		this.addStatic(new CityBuildingImage(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength + sidewalkWidth+ parkingGap, apartmentR));
		this.addStatic(new CityBuildingImage(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, houseR));
		this.addStatic(new CityBuildingImage(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, apartmentR));
		this.addStatic(new CityBuildingImage(2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, restaurant));
		
		//buildings col4
		this.addStatic(new CityBuildingImage(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, restaurant));
		this.addStatic(new CityBuildingImage(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, houseL));
		this.addStatic(new CityBuildingImage(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, apartmentL));
		this.addStatic(new CityBuildingImage( 2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, houseL));
		this.addStatic(new CityBuildingImage(2*yardSpace+3*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, market));
		
		//buildings col5
		this.addStatic(new CityBuildingImage(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, restaurant));
		this.addStatic(new CityBuildingImage(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, apartmentR));
		this.addStatic(new CityBuildingImage(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, houseR));
		this.addStatic(new CityBuildingImage( 3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, apartmentR));
		this.addStatic(new CityBuildingImage(3*yardSpace+4*housingWidth+4*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, restaurant));
		
		//buildings col6
		this.addStatic(new CityBuildingImage(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, market));
		this.addStatic(new CityBuildingImage(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, houseL));
		this.addStatic(new CityBuildingImage(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, apartmentL));
		this.addStatic(new CityBuildingImage( 3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, houseL));
		this.addStatic(new CityBuildingImage(3*yardSpace+5*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, restaurant));
		
		//buildings col7
		this.addStatic(new CityBuildingImage(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, bank));
		this.addStatic(new CityBuildingImage(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, houseR));
		this.addStatic(new CityBuildingImage(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap, apartmentR));
		this.addStatic(new CityBuildingImage( 4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, houseR));
		this.addStatic(new CityBuildingImage(4*yardSpace+6*housingWidth+6*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap, market));
		
		//buildings col8
		this.addStatic(new CityBuildingImage(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+sidewalkWidth, apartmentL));
		this.addStatic(new CityBuildingImage(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+housingLength+ sidewalkWidth+ parkingGap, houseL));
		this.addStatic(new CityBuildingImage(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+2*housingLength+ sidewalkWidth+ 2*parkingGap,apartmentL));
		this.addStatic(new CityBuildingImage( 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+3*housingLength+ sidewalkWidth+ 3*parkingGap, houseL));
		this.addStatic(new CityHomelessShelter(4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+4*housingLength+ sidewalkWidth+ 5*parkingGap));
		
	 	  
	 		int counter=0;
			int i=yardSpace+housingWidth;
			while(i<550) {
				this.addStatic(new CityRoad(i, RoadDirection.VERTICAL, Color.gray));

				if(counter%2==0) i += streetWidth+sidewalkWidth;
				else i += 2*housingWidth+ streetWidth;
				counter++;
			}
			
			this.addStatic(new CityRoad(streetWidth, RoadDirection.HORIZONTAL, Color.gray));
			this.addStatic(new CityRoad(streetWidth+5*housingLength+ sidewalkWidth+ 5*parkingGap, RoadDirection.HORIZONTAL, Color.gray));
			
			int w=housingWidth+streetWidth;
			while(w<500) {
				this.addStatic(new CityRoad(w, RoadDirection.VERTICAL, Color.DARK_GRAY));

				w+=2*sidewalkWidth+2*housingWidth+yardSpace+streetWidth;
			}
			
			this.addStatic(new CityRoad(0, RoadDirection.HORIZONTAL, Color.DARK_GRAY));
			this.addStatic(new CityRoad(streetWidth+5*housingLength+ sidewalkWidth+ 5*parkingGap+sidewalkWidth, RoadDirection.HORIZONTAL, Color.DARK_GRAY));
			
	 	  
	
		addMouseListener(this);
		addMouseMotionListener(this);

		 
	}
	

	
	public void mouseClicked(MouseEvent arg0) {
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		if (addingObject) {
			//make sure we aren't overlapping anything
			for (CityComponent c: statics) {
				if (c.equals(temp))
					continue;
				if (c.rectangle.intersects(temp.rectangle)) {
					AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, this.name, "Can't add building, location obstructed!");
					return;
				}
			}
			AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Building successfully added");
			addingObject = false;
			city.view.addView(new CityCard(city, Color.pink), temp.ID);
			temp = null;
		}
		for (CityComponent c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				city.info.setText(c.ID);
				city.view.setView(c.ID);
				AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Building Selected: " + c.ID);
			}
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void addObject(CityComponents c) {
		if (addingObject)
			return;
		addingObject = true;
		switch (c) {
		case RESTAURANT: temp = new CityRestaurant(-100, -100, "Restaurant " + (statics.size()-19)); break;
		case ROAD: temp = new CityRoad(-100, RoadDirection.HORIZONTAL, Color.gray); break; //NOTE: DON'T MAKE NEW ROADS
		case BANK: temp = new CityBank(-100, -100, "Bank " + (statics.size()-19)); break;
		default: return;
		}
		addStatic(temp);
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
		if (addingObject) {
			temp.setPosition(arg0.getPoint());
		}
	}
	
}
