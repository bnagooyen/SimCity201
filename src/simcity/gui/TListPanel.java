package simcity.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class TListPanel extends JPanel {

	SimCityGui city;
	JLabel text;
	public static final int INFO_WIDTH = 300, INFO_HEIGHT = 150;
	
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private JPanel addCust = new JPanel();
    private List<JCheckBox> list = new ArrayList<JCheckBox>();
    private List<JLabel> pplList = new ArrayList<JLabel>();
    private Object currentPerson;/* Holds the agent that the info is about.
	Seems like a hack */
    private JPanel valueSetPanel = new JPanel();
    
	
	public TListPanel(SimCityGui restaurantGui, String txt) {
		this.city = restaurantGui;
		this.setPreferredSize(new Dimension(INFO_WIDTH, INFO_HEIGHT));
		this.setBackground(Color.darkGray);
		this.setVisible(true);
		
		text = new JLabel("<html><pre> <u>" + txt + "</u></pre></html>");
		text.setPreferredSize(new Dimension(INFO_WIDTH, 30));
		text.setForeground(Color.white);
		text.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		text.setAlignmentX(CENTER_ALIGNMENT);
		//add(text);
		setLayout(new BorderLayout());
		add(text, BorderLayout.NORTH);
		
		 Dimension dim = new Dimension(300, 220);
	        pane.setSize(dim);
	        pane.setPreferredSize(pane.getSize());
	        pane.setMinimumSize(pane.getSize());
	        pane.setMaximumSize(pane.getSize());
	        
	        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
	        pane.setViewportView(view);
	        
	        add(pane, BorderLayout.SOUTH);
		
		
		
	}
	
	public void setText(String s) {
		text.setText(s);
	}

}
