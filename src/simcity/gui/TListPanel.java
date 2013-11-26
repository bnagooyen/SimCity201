package simcity.gui;

import java.awt.*;

import javax.swing.*;

public class TListPanel extends JPanel {

	CityGui city;
	JLabel text;
	public static final int INFO_WIDTH = 300, INFO_HEIGHT = 150;
	
	public TListPanel(CityGui city) {
		this.city = city;
		this.setPreferredSize(new Dimension(INFO_WIDTH, INFO_HEIGHT));
		this.setBackground(Color.LIGHT_GRAY);
		this.setVisible(true);
		
		text = new JLabel("null");
		text.setForeground(Color.black);
		text.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		add(text);
	}
	
	public void setText(String s) {
		text.setText(s);
	}

}
