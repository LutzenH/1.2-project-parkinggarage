package parkeersimulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import parkeersimulator.controller.AbstractController.ActionType;
import parkeersimulator.controller.FinanceController;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.FinanceModel;
import parkeersimulator.model.ParkingGarageModel;

/**
 * Class of a view that can start and stop the simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class CarParkFinanceControlView extends AbstractControllableView{
	/**
	 * Constructor of CarParkFinanceControlView
	 * @param model The model this view should be getting information from.
	 * @param controller The controller this view should send instruction to.
	 */ 
	public CarParkFinanceControlView(FinanceModel financeModel, FinanceController financeController) {
		super(financeModel, financeController);
		
		ChangeListener changeListener;
		ActionListener actionListener;

		//Payment timeframe amount
		JSpinner paymentTimeframeAmountSpinner = buildComponent_spinner(financeModel.getPaymentTimeframe(), 0, 60, 5);
		actionListener = e -> controller.performAction(ActionType.FINANCE_PAYMENTTIMEFRAME_AMOUNT, new HashMap<String, Object>() {{ put("amount", paymentTimeframeAmountSpinner.getValue()); }});
		JPanel paymentTimeframeAmountController = buildController_spinner("Payment timeframe amount", "Set", paymentTimeframeAmountSpinner, "In minutes", actionListener);
		
		//Cost per timeframe amount
		JSpinner costPerTimeframeAmountSpinner = buildComponent_spinner(financeModel.getCostPerTimeFrame(), 0f, 60f, 0.1f);
		actionListener = e -> controller.performAction(ActionType.FINANCE_COSTPERTIMEFRAME_AMOUNT, new HashMap<String, Object>() {{ put("amount", costPerTimeframeAmountSpinner.getValue()); }});
		JPanel costPerTimeframeAmountController = buildController_spinner("Cost per timeframe", "Set", costPerTimeframeAmountSpinner, actionListener);
		
		//Maintenance costs amount
		JSpinner maintenanceCostsAmountSpinner = buildComponent_spinner(financeModel.getMaintenanceCosts(), 0, 100000, 1000);
		actionListener = e -> controller.performAction(ActionType.FINANCE_MAINTENANCECOSTS_AMOUNT, new HashMap<String, Object>() {{ put("amount", maintenanceCostsAmountSpinner.getValue()); }});
		JPanel maintenanceCostsAmountController = buildController_spinner("Maintenance costs per timeframe", "Set", maintenanceCostsAmountSpinner, actionListener);

		//Set the layoutManager of the JPanel that all these controls sit on
		this.setLayout(new FlowLayout());
		
		//Add the components to the panel
		add(paymentTimeframeAmountController);
		add(costPerTimeframeAmountController);
		add(maintenanceCostsAmountController);

		setVisible(true);
	}
}
