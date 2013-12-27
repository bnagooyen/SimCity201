/*
 * SimCity201, Released December 2013
 * Contributors to file: Doreen Hakimi
 */

package simcity.gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

public abstract class BuildingListPanel extends JPanel implements ActionListener {

	SimCityGui city;
	JLabel text;
	public static final int INFO_WIDTH = 300, INFO_HEIGHT = 150;

	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	public JPanel top = new JPanel();
	private List<JCheckBox> list = new ArrayList<JCheckBox>();
	private List<JLabel> pplList = new ArrayList<JLabel>();
	private Object currentPerson;/* Holds the agent that the info is about.
	Seems like a hack */
	protected JPanel valueSetPanel = new JPanel();


	public BuildingListPanel(SimCityGui restaurantGui, String txt) {
		this.city = restaurantGui;
		this.setPreferredSize(new Dimension(INFO_WIDTH, INFO_HEIGHT));
		setBackground(Color.darkGray);
		this.setVisible(true);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		top.setLayout(new BorderLayout());
		top.setBackground(Color.darkGray);
		text = new JLabel(txt);
		//text.setPreferredSize(new Dimension(INFO_WIDTH, 30));
		text.setForeground(Color.white);
		text.setFont(new Font("Arial", Font.BOLD, 20));
		text.setPreferredSize(new Dimension(INFO_WIDTH, 30));
		text.setAlignmentX(CENTER_ALIGNMENT);
		//add(text);
		setLayout(new BorderLayout());
		top.add(text, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);

		Dimension dim = new Dimension(300, 220);
		pane.setSize(dim);
		pane.setPreferredSize(pane.getSize());
		pane.setMinimumSize(pane.getSize());
		pane.setMaximumSize(pane.getSize());

		view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);

		add(pane, BorderLayout.SOUTH);
		valueSetPanel.setPreferredSize(new Dimension(INFO_WIDTH, 80));
		//valueSetPanel.setBackground(Color.darkGray);
		valueSetPanel.setBackground(Color.DARK_GRAY);
		add(valueSetPanel, BorderLayout.CENTER);


	}

	public void setText(String s) {
		text.setText(s);
	}

	abstract public void addPerson(String name);

}
