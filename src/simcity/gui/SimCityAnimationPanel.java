package simcity.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SimCityAnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 740;
    private final int WINDOWY = 444;
   
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
    
    
    private Image bufferImage;
    private Dimension bufferSize;

    private SimCityGui gui;
    
    private List<Gui> guis = new ArrayList<Gui>();
    
    
    //simcity
    
    //private BufferedImage house = null;
    private BufferedImage houseL = null;
    private BufferedImage houseR = null;
    private BufferedImage apartmentL = null;
    private BufferedImage apartmentR = null;
    private BufferedImage restaurantT = null;
    private BufferedImage restaurantB = null;
    private BufferedImage bankT = null;
    private BufferedImage bankB = null;
    private BufferedImage marketT = null;
    private BufferedImage marketB = null;
 
    
    public static final int buildingWidth=70;
    public static final int parkingWidth=30;
    public static final int bldgOffset=110;
    public static final int streetOffset=40;
    public static final int businessStreety=20;
    public static final int residentialStreety=175;
    public static final int residentialStreet2y=370;
    public static final int streetWidth = 50;
    public static final int sidewalkWidth = 30;
    public static final int housingWidth=30;
    public static final int housingLength=57;
    public static final int yardSpace=11;
    
    public SimCityAnimationPanel(SimCityGui gui) {
    	this.gui=gui;
    	setSize(WINDOWX, WINDOWY);

        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    	
    	
    	
    	  try {
              StringBuilder path = new StringBuilder("images"+ File.separator);
              houseL = ImageIO.read(new File(path.toString() + "houseL.png"));
              houseR = ImageIO.read(new File(path.toString() + "houseR.png"));
              apartmentR = ImageIO.read(new File(path.toString() + "apartmentR.png"));
              apartmentL = ImageIO.read(new File(path.toString() + "apartmentL.png"));
              marketT= ImageIO.read(new File(path.toString() + "marketT.png"));
              marketB=ImageIO.read(new File(path.toString() + "marketB.png"));
              bankB = ImageIO.read(new File(path.toString() + "bankB.png"));
              bankT = ImageIO.read(new File(path.toString() + "bankT.png"));
              restaurantT=ImageIO.read(new File(path.toString() + "restaurantT.png"));
              restaurantB=ImageIO.read(new File(path.toString() + "restaurantB.png"));
              
      } catch (IOException e) {
    	  System.out.println("couldn't find file");
      }
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    @Override
	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(BTMX, BTMY, WINDOWX, WINDOWY );

        //Here is the table
   
//        for(int i=0; i<nTABLES; i++)
//        {
//        	int fillx = (i%TABLES_perRow)*TABLESZ_xy*2 + TABLE_gap + x_Offset;
//        	int filly = (i/TABLES_perRow)*TABLESZ_xy*2 + TABLE_gap;
//        	g2.fillRect(fillx, filly, TABLESZ_xy, TABLESZ_xy);//200 and 250 need to be table params
//        
//        }
        
//        //draw kitchen components
//        g2.setColor(Color.cyan);
//        g2.fillRect(allKitchenItems_x, refrig_y, refrig_xsz, refrig_ysz);
//        g2.setColor(Color.LIGHT_GRAY);
//        g2.fillRect(allKitchenItems_x, grillPizza_y, grill_xsz, grill_ysz);
//        g2.fillRect(allKitchenItems_x, grillChicken_y, grill_xsz, grill_ysz);
//        g2.fillRect(allKitchenItems_x, grillSteak_y, grill_xsz, grill_ysz);
//        g2.fillRect(allKitchenItems_x, grillSalad_y, grill_xsz, grill_ysz);
//
//        g2.setColor(Color.pink);
//        g2.fillRect(allKitchenItems_x, plating_x, grill_xsz, plating_ysz);
        
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
        
        //doreen's restaurant
        
//        g2.fillRect(330, 70, 40, 40);
//        System.out.println(house);
//        g2.drawImage(house, 70, 40, null);
//        g2.drawImage(apartment, 230, 40, null);
        
        //simcity
        
        //roads
    
//        g2.setColor(Color.GREEN);
//        g2.fillRect(110, 0, WINDOWX-220, 65);
//        g2.fillRect(110, 175, WINDOWX-220, 65);
//        
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(0, 30,WINDOWX, 30);
//        g2.fillRect(110, 240,WINDOWX, 30);
//        g2.fillRect(65, 0,30, WINDOWY);
//        g2.fillRect(WINDOWX-110, 0, 30, WINDOWY);
//        
    
        //PARKING1
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(housingWidth+yardSpace-15, 0, 15, WINDOWY);
        g2.fillRect(housingWidth+streetWidth+2*sidewalkWidth+yardSpace, 0, 15, WINDOWY);
        g2.fillRect(4*housingWidth+streetWidth+2*sidewalkWidth-25, 0, 18, WINDOWY);
        g2.fillRect(4*housingWidth+2*streetWidth+3*sidewalkWidth+2*yardSpace, 0, 15, WINDOWY);
        g2.fillRect(5*housingWidth+2*streetWidth+3*sidewalkWidth+4*yardSpace+5, 0, 15, WINDOWY);
        g2.fillRect(10*housingWidth+3*streetWidth+3*sidewalkWidth+4*yardSpace-15, 0, 15, WINDOWY);
        g2.fillRect(10*housingWidth+3*streetWidth+4*sidewalkWidth+4*yardSpace, 0, 15, WINDOWY);
        g2.fillRect(6*housingWidth+3*streetWidth+5*sidewalkWidth+3*yardSpace, 0, 15, WINDOWY);
        g2.fillRect(10*housingWidth+4*streetWidth+5*sidewalkWidth+5*yardSpace-12, 0, 15, WINDOWY);
        
        
        g2.setColor(Color.GRAY);
        g2.fillRect(0,streetWidth, WINDOWX, sidewalkWidth);
        g2.fillRect(0,WINDOWY-sidewalkWidth-streetWidth, WINDOWX, sidewalkWidth);

        //vertical street
        //street row 1
        g2.fillRect(housingWidth+yardSpace, 0, sidewalkWidth, WINDOWY);
        
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(housingWidth+sidewalkWidth+yardSpace, 0, streetWidth, WINDOWY);
        g2.setColor(Color.GRAY);
        
        g2.fillRect(housingWidth+streetWidth+sidewalkWidth+yardSpace, 0, sidewalkWidth, WINDOWY);
        
    
    
        //street row 2
        g2.fillRect(4*housingWidth+streetWidth+sidewalkWidth+2*yardSpace, 0, sidewalkWidth, WINDOWY);
  
        
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(4*housingWidth+streetWidth+2*sidewalkWidth+2*yardSpace, 0, streetWidth, WINDOWY);
        g2.setColor(Color.GRAY);
        
        g2.fillRect(4*housingWidth+2*streetWidth+2*sidewalkWidth+2*yardSpace, 0, sidewalkWidth, WINDOWY);
   


        //street row 3
//        
//        //PARKING1
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(7*housingWidth+2*streetWidth+2*sidewalkWidth+3*yardSpace-15, 0, 15, WINDOWY);
//        
//        
        g2.fillRect(7*housingWidth+2*streetWidth+2*sidewalkWidth+3*yardSpace, 0, sidewalkWidth, WINDOWY);
        
        
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(7*housingWidth+2*streetWidth+3*sidewalkWidth+3*yardSpace, 0, streetWidth, WINDOWY);
        g2.setColor(Color.GRAY);
        
        g2.fillRect(7*housingWidth+3*streetWidth+3*sidewalkWidth+3*yardSpace, 0, sidewalkWidth, WINDOWY);


        
        //street row 4
        g2.fillRect(10*housingWidth+3*streetWidth+3*sidewalkWidth+4*yardSpace, 0, sidewalkWidth, WINDOWY);
        

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(10*housingWidth+3*streetWidth+4*sidewalkWidth+4*yardSpace, 0, streetWidth, WINDOWY);
        g2.setColor(Color.GRAY);
        
        g2.fillRect(10*housingWidth+4*streetWidth+4*sidewalkWidth+4*yardSpace, 0, sidewalkWidth, WINDOWY);
        
        

        
        //horizontal
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0,0, WINDOWX, streetWidth);
        g2.fillRect(0,WINDOWY-streetWidth, WINDOWX, streetWidth);
  
        
        //house row 1
        g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth, null);
        g2.drawImage(apartmentR, yardSpace, streetWidth+sidewalkWidth+housingLength, null);
        g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth+2*housingLength, null);
        g2.drawImage(apartmentR, yardSpace, streetWidth+sidewalkWidth+3*housingLength, null);
        g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth+4*housingLength, null); 
        
        //row2
        g2.drawImage(marketT, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, null);
        g2.drawImage(houseL, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentL, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth, null);
        g2.drawImage(houseL, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth, null);
        g2.drawImage(bankB, yardSpace+housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth, null);

        //row3
        g2.drawImage(restaurantT, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+sidewalkWidth, null);
        g2.drawImage(apartmentR, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+housingLength+ sidewalkWidth, null);
        g2.drawImage(houseR, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+2*housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentR, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+3*housingLength+ sidewalkWidth, null);
        g2.drawImage(restaurantB, 2*yardSpace+2*housingWidth+2*sidewalkWidth+streetWidth, streetWidth+4*housingLength+ sidewalkWidth, null);
        
        //row4
        g2.drawImage(restaurantT, 2*yardSpace+2*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, null);
        g2.drawImage(houseL, 2*yardSpace+2*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentL, 2*yardSpace+2*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth, null);
        g2.drawImage(houseL, 2*yardSpace+2*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth, null);
        g2.drawImage(marketB, 2*yardSpace+2*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth, null);
        
        //row5
        g2.drawImage(restaurantT, 3*yardSpace+3*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+sidewalkWidth, null);
        g2.drawImage(apartmentR, 3*yardSpace+3*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+housingLength+ sidewalkWidth, null);
        g2.drawImage(houseR, 3*yardSpace+3*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+2*housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentR, 3*yardSpace+3*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+3*housingLength+ sidewalkWidth, null);
        g2.drawImage(restaurantB, 3*yardSpace+3*housingWidth+5*sidewalkWidth+2*streetWidth, streetWidth+4*housingLength+ sidewalkWidth, null);
        
        
        //row6
        g2.drawImage(marketT, 3*yardSpace+4*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, null);
        g2.drawImage(houseL, 3*yardSpace+4*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentL, 3*yardSpace+4*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth, null);
        g2.drawImage(houseL, 3*yardSpace+4*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth, null);
        g2.drawImage(restaurantB, 3*yardSpace+4*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth, null);
        
        //row7
        g2.drawImage(bankT, 4*yardSpace+5*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+sidewalkWidth, null);
        g2.drawImage(houseR, 4*yardSpace+5*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentR, 4*yardSpace+5*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+2*housingLength+ sidewalkWidth, null);
        g2.drawImage(houseR, 4*yardSpace+5*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+3*housingLength+ sidewalkWidth, null);
        g2.drawImage(marketB, 4*yardSpace+5*housingWidth+7*sidewalkWidth+3*streetWidth, streetWidth+4*housingLength+ sidewalkWidth, null);
      
        //row8
        g2.drawImage(apartmentL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+sidewalkWidth, null);
        g2.drawImage(houseL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+2*housingLength+ sidewalkWidth, null);
        g2.drawImage(houseL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+3*housingLength+ sidewalkWidth, null);
        g2.drawImage(apartmentL, 4*yardSpace+7*housingWidth+8*sidewalkWidth+4*streetWidth, streetWidth+4*housingLength+ sidewalkWidth, null);
        
        
        
//        g2.drawImage(apartmentR, yardSpace, streetWidth+sidewalkWidth+housingLength, null);
//        g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth+2*housingLength, null);
//        g2.drawImage(apartmentR, yardSpace, streetWidth+sidewalkWidth+3*housingLength, null);
//        g2.drawImage(houseR, yardSpace, streetWidth+sidewalkWidth+4*housingLength, null); 
//        g2.drawImage(market, bldgOffset, businessStreety, null);
//        g2.drawImage(restaurant, bldgOffset+buildingWidth, businessStreety, null);
//        g2.drawImage(restaurant, bldgOffset+2*buildingWidth, businessStreety, null);
//        g2.drawImage(restaurant, bldgOffset+3*buildingWidth, businessStreety, null);
//        g2.drawImage(bank, bldgOffset+4*buildingWidth, businessStreety, null);
//        g2.drawImage(restaurant, bldgOffset+5*buildingWidth, businessStreety, null);
//        g2.drawImage(restaurant, bldgOffset+6*buildingWidth, businessStreety, null);
//        g2.drawImage(restaurant, bldgOffset+7*buildingWidth, businessStreety, null);
//        g2.drawImage(market, bldgOffset+8*buildingWidth, businessStreety, null);
////       
//      
////        //residential1
//        g2.drawImage(house, bldgOffset, residentialStreety, null);
//        g2.drawImage(apartment, bldgOffset+buildingWidth, residentialStreety, null);
//        g2.drawImage(house, bldgOffset+2*buildingWidth, residentialStreety, null);
//        g2.drawImage(apartment, bldgOffset+3*buildingWidth, residentialStreety, null);
//        g2.drawImage(house, bldgOffset+4*buildingWidth, residentialStreety, null);
//        g2.drawImage(apartment, bldgOffset+5*buildingWidth, residentialStreety, null);
//        g2.drawImage(house, bldgOffset+6*buildingWidth, residentialStreety, null);
//        g2.drawImage(apartment, bldgOffset+7*buildingWidth, residentialStreety, null);
//        g2.drawImage(house, bldgOffset+8*buildingWidth, residentialStreety, null);
//        
//        //residential2
//        g2.drawImage(house, bldgOffset, residentialStreet2y, null);
//        g2.drawImage(apartment, bldgOffset+buildingWidth, residentialStreet2y, null);
//        g2.drawImage(house, bldgOffset+2*buildingWidth, residentialStreet2y, null);
//        g2.drawImage(apartment, bldgOffset+3*buildingWidth, residentialStreet2y, null);
//        g2.drawImage(house, bldgOffset+4*buildingWidth, residentialStreet2y, null);
//        g2.drawImage(apartment, bldgOffset+5*buildingWidth, residentialStreet2y, null);
//        g2.drawImage(house, bldgOffset+6*buildingWidth, residentialStreet2y, null);
//        g2.drawImage(apartment, bldgOffset+7*buildingWidth, residentialStreet2y, null);
//        g2.drawImage(house, bldgOffset+8*buildingWidth, residentialStreet2y, null);
        
    }
   
    public void addGui(PersonGui gui) {
        guis.add(gui);
    }
//
//    public void addGui(CustomerGui gui) {
//        guis.add(gui);
//    }
//
//    public void addGui(WaiterGui gui) {
//        guis.add(gui);
//    }
//    
//    public void addGui(CookGui gui) {
//        guis.add(gui);
//    }
}
