package parkeersimulator.view.frame;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;

import parkeersimulator.controller.ParkingGarageController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {

	JSplitPane splitPane_horizontal;
	private int controlPanelWidth = 350;
	
	public MainFrame(String string, JPanel[] tabPanels_controlPanel, JPanel[] tabPanels_graphView, JPanel simulationPanel) {
		super(string);
		
		//Set JFrame properties
		this.setBounds(100, 100, 450, 300); //Set the size of the JFrame when the program launches
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.NORMAL);
		
		//Set JFrame contentPane properties
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		//Create the top menu bar
		setJMenuBar(createMenuBar());
		
		//Create the graphView tabbedPane
		JTabbedPane tabPane_graphView = new JTabbedPane(JTabbedPane.TOP);

		for(JPanel panel : tabPanels_graphView) { //Add the tabs to the tabbedPanes
			tabPane_graphView.addTab(panel.getName(), null, panel, null);
		}
		
		//Create the tabPane
		JTabbedPane tabPane_controlPanel = new JTabbedPane(JTabbedPane.TOP);
		tabPane_controlPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		for(JPanel panel : tabPanels_controlPanel) { //Add the tabs to the tabbedPane
			JScrollPane controlPanel_scrollPane = new JScrollPane();
			controlPanel_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			controlPanel_scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			
			JPanel scrollPane_filler = new JPanel();
			scrollPane_filler.setLayout(new BorderLayout(0, 0));
			
			tabPane_controlPanel.addTab(panel.getName(), null, controlPanel_scrollPane, null);

			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); //Set the layout type of the panel
			panel.add(scrollPane_filler); //Add the filler
			
			controlPanel_scrollPane.setViewportView(panel); //Add the panel to the scrollPane
		}
		
		this.getContentPane().add(tabPane_controlPanel, BorderLayout.EAST);
		
		//Create the splitPanes
		JSplitPane splitPane_horizontal = new JSplitPane();
		
		splitPane_horizontal.setEnabled(true);
		splitPane_horizontal.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_horizontal.setTopComponent(tabPane_graphView);
		splitPane_horizontal.setBottomComponent(simulationPanel);

		this.getContentPane().add(splitPane_horizontal, BorderLayout.CENTER);
		
		//Finish the setting up of the JFrame
		this.pack();
		this.setVisible(true);
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExport = new JMenuItem("Export");
		mnFile.add(mntmExport);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mnFile.add(mntmQuit);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenuItem mntmReset = new JMenuItem("Reset");
		mnSettings.add(mntmReset);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		return menuBar;
	}
}
