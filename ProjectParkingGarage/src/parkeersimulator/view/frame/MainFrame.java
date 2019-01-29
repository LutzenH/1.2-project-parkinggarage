package parkeersimulator.view.frame;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.controller.ParkingGarageController.ActionType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.FlowLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {

	JSplitPane splitPane;
	public MainFrame(String string, JPanel controlPanel, JPanel[] tabbedPanels, JPanel simulationPanel) {

		super(string);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		setJMenuBar(createMenuBar());
		
		splitPane = new JSplitPane();
		this.getContentPane().add(splitPane);
		
		///get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(true);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		this.getContentPane().add(splitPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setEnabled(false);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		splitPane.setLeftComponent(splitPane_1);	
		splitPane.setRightComponent(controlPanel);
		splitPane_1.setTopComponent(tabbedPane);
		splitPane_1.setBottomComponent(simulationPanel);
		
		splitPane_1.getTopComponent().setMaximumSize(new Dimension(width / 1366 * 1116, height / 768 * 530));
		splitPane_1.getTopComponent().setMinimumSize(new Dimension(width / 1366 * 1056, height / 768 * 350));
		splitPane_1.getBottomComponent().setMaximumSize(new Dimension(width / 1366 * 1116, height / 768 * 418));
		splitPane_1.getBottomComponent().setMinimumSize(new Dimension(width / 1366 * 1056, height / 768 * 238));
		splitPane.getRightComponent().setMaximumSize(new Dimension(width / 1366 * 310, height));
		splitPane.getRightComponent().setMinimumSize(new Dimension(width / 1366 * 250, height));
		
		for(JPanel panel : tabbedPanels)
		{
			tabbedPane.addTab(panel.getName(), null, panel, null);
		}
		
		this.pack();
		
	}

	public void addResizeProperty(ParkingGarageController controller)
	{
		splitPane.addPropertyChangeListener(e -> controller.performAction(ActionType.EVENT_FRAME_RESIZE));
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
