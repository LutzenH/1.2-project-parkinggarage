package parkeersimulator.view.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.Font;

public class AboutFrame extends JFrame {

	private JPanel contentPane;
	
	/**
	 * Create the frame.
	 */
	public AboutFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 374, 170);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextPane txtpnCreditsLutzenhThowv = new JTextPane();
		txtpnCreditsLutzenhThowv.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtpnCreditsLutzenhThowv.setText("Credits:\nLutzenH\nThowV\nb-kuiper\nrizza99\n\nhttps://github.com/LutzenH/Project-Parkeergarage");
		contentPane.add(txtpnCreditsLutzenhThowv, BorderLayout.CENTER);
	}

}
