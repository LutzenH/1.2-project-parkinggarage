package parkeersimulator.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JSpinner;

import parkeersimulator.controller.AbstractController.ActionType;
import parkeersimulator.controller.FinanceController;
import parkeersimulator.model.FinanceModel;

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
		
		ActionListener actionListener;

		//Payment timeframe amount
		JSpinner paymentTimeframeAmountSpinner = buildComponent_spinner(financeModel.getPaymentTimeframe(), 0, 60, 5);
		actionListener = e -> controller.performAction(ActionType.FINANCE_PAYMENTTIMEFRAME_AMOUNT, new HashMap<String, Object>() {{ put("amount", paymentTimeframeAmountSpinner.getValue()); }});
		JPanel paymentTimeframeAmountController = buildController_spinner("Payment timeframe amount", "Set", paymentTimeframeAmountSpinner, "In minutes", actionListener);
		
		//Cost per timeframe amount
		JSpinner adHocCostPerTimeframeAmountSpinner = buildComponent_spinner(financeModel.getCostPerTimeFrame_adHocCar(), 0f, 60f, 0.1f);
		actionListener = e -> controller.performAction(ActionType.FINANCE_ADHOC_COSTPERTIMEFRAME_AMOUNT, new HashMap<String, Object>() {{ put("amount", adHocCostPerTimeframeAmountSpinner.getValue()); }});
		JPanel adHocCostPerTimeframeAmountController = buildController_spinner("AdHoc Cost per timeframe", "Set", adHocCostPerTimeframeAmountSpinner, actionListener);
		
		JSpinner reservationCostPerTimeframeAmountSpinner = buildComponent_spinner(financeModel.getCostPerTimeFrame_reservationCar(), 0f, 60f, 0.1f);
		actionListener = e -> controller.performAction(ActionType.FINANCE_RESERVATION_COSTPERTIMEFRAME_AMOUNT, new HashMap<String, Object>() {{ put("amount", reservationCostPerTimeframeAmountSpinner.getValue()); }});
		JPanel reservationCostPerTimeframeAmountController = buildController_spinner("Reservation Cost per timeframe", "Set", reservationCostPerTimeframeAmountSpinner, actionListener);
		
		JSpinner passHolderCostPerMonthAmountSpinner = buildComponent_spinner(financeModel.getCostPerMonth_passHolderCar(), 0f, 60f, 0.5f);
		actionListener = e -> controller.performAction(ActionType.FINANCE_PASSHOLDER_COSTPERMONTH_AMOUNT, new HashMap<String, Object>() {{ put("amount", passHolderCostPerMonthAmountSpinner.getValue()); }});
		JPanel passHolderCostPerMonthAmountController = buildController_spinner("PassHolder Cost per month", "Set", passHolderCostPerMonthAmountSpinner, actionListener);
		
		//Cost for props
		JSpinner entranceCostAmountSpinner = buildComponent_spinner(financeModel.getEntranceCosts(), 0, 15000, 100);
		actionListener = e -> controller.performAction(ActionType.FINANCE_ENTRANCECOST_AMOUNT, new HashMap<String, Object>() {{ put("amount", entranceCostAmountSpinner.getValue()); }});
		JPanel entranceCostAmountController = buildController_spinner("Entrance prop Cost per month", "Set", entranceCostAmountSpinner, actionListener);
		
		JSpinner ticketMachineCostAmountSpinner = buildComponent_spinner(financeModel.getTicketMachineCosts(), 0, 15000, 100);
		actionListener = e -> controller.performAction(ActionType.FINANCE_TICKETMACHINECOST_AMOUNT, new HashMap<String, Object>() {{ put("amount", ticketMachineCostAmountSpinner.getValue()); }});
		JPanel ticketMachineCostAmountController = buildController_spinner("Ticket machine prop Cost per month", "Set", ticketMachineCostAmountSpinner, actionListener);
		
		JSpinner exitCostAmountSpinner = buildComponent_spinner(financeModel.getExitCosts(), 0, 15000, 100);
		actionListener = e -> controller.performAction(ActionType.FINANCE_EXITCOST_AMOUNT, new HashMap<String, Object>() {{ put("amount", exitCostAmountSpinner.getValue()); }});
		JPanel exitCostAmountController = buildController_spinner("Exit prop cost per month", "Set", exitCostAmountSpinner, actionListener);
		
		//Cost for places, rows and floors
		JSpinner placeCostAmountSpinner = buildComponent_spinner(financeModel.getPlaceCosts(), 0f, 20f, 0.5f);
		actionListener = e -> controller.performAction(ActionType.FINANCE_PLACECOST_AMOUNT, new HashMap<String, Object>() {{ put("amount", placeCostAmountSpinner.getValue()); }});
		JPanel placeCostAmountController = buildController_spinner("Place cost per month", "Set", placeCostAmountSpinner, actionListener);
		
		JSpinner rowCostAmountSpinner = buildComponent_spinner(financeModel.getRowCosts(), 0, 1500, 20);
		actionListener = e -> controller.performAction(ActionType.FINANCE_ROWCOST_AMOUNT, new HashMap<String, Object>() {{ put("amount", rowCostAmountSpinner.getValue()); }});
		JPanel rowCostAmountController = buildController_spinner("Row Cost per month", "Set", rowCostAmountSpinner, actionListener);
		
		JSpinner floorCostAmountSpinner = buildComponent_spinner(financeModel.getFloorCosts(), 0, 10000, 100);
		actionListener = e -> controller.performAction(ActionType.FINANCE_FLOORCOST_AMOUNT, new HashMap<String, Object>() {{ put("amount", floorCostAmountSpinner.getValue()); }});
		JPanel floorCostAmountController = buildController_spinner("Floor cost per month", "Set", floorCostAmountSpinner, actionListener);
		
		//Maintenance costs amount
		JSpinner maintenanceCostsAmountSpinner = buildComponent_spinner(financeModel.getMaintenanceCosts(), 0, 100000, 1000);
		actionListener = e -> controller.performAction(ActionType.FINANCE_MAINTENANCECOSTS_AMOUNT, new HashMap<String, Object>() {{ put("amount", maintenanceCostsAmountSpinner.getValue()); }});
		JPanel maintenanceCostsAmountController = buildController_spinner("Maintenance costs per timeframe", "Set", maintenanceCostsAmountSpinner, actionListener);

		//Set the layoutManager of the JPanel that all these controls sit on
		this.setLayout(new FlowLayout());
		
		//Add the components to the panel
		add(paymentTimeframeAmountController);
		
		add(adHocCostPerTimeframeAmountController);
		add(reservationCostPerTimeframeAmountController);
		add(passHolderCostPerMonthAmountController);
		
		add(entranceCostAmountController);
		add(ticketMachineCostAmountController);
		add(exitCostAmountController);
		
		add(placeCostAmountController);
		add(rowCostAmountController);
		add(floorCostAmountController);
		
		add(maintenanceCostsAmountController);

		setVisible(true);
	}
}
