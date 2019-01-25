package parkeersimulator.view;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class TempView extends JPanel {

	/**
	 * Create the panel.
	 */
	public TempView() {
		setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Open Garage");
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Close Garage");
		btnNewButton_1.setHorizontalAlignment(SwingConstants.LEFT);
		toolBar.add(btnNewButton_1);

	}

}
