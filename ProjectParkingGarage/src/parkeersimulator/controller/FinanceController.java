package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.FinanceModel;

/**
 * The Controller for the FinanceModel
 * 
 * @author ThowV
 */
public class FinanceController extends AbstractController {
	private FinanceModel financeModel;
	
	/**
	 * Constructor for FinanceController
	 * @param financeModel the model this controller should perform actions on.
	 */
	public FinanceController(FinanceModel financeModel) {
		super(financeModel);
		this.financeModel = financeModel;
	}

	@Override
	public boolean performAction(ActionType action, HashMap<String, Object> data) {
		switch(action) {
			case FINANCE_PAYMENTTIMEFRAME_AMOUNT:
				financeModel.setPaymentTimeframe((Integer)data.get("amount"));
				return true;
				
			case FINANCE_ADHOC_COSTPERTIMEFRAME_AMOUNT:
				financeModel.setCostPerTimeFrame_adHocCar((Float)data.get("amount"));
				return true;
			case FINANCE_RESERVATION_COSTPERTIMEFRAME_AMOUNT:
				financeModel.setCostPerTimeFrame_reservationCar((Float)data.get("amount"));
				return true;
			case FINANCE_PASSHOLDER_COSTPERMONTH_AMOUNT:
				financeModel.setCostPerMonth_passHolderCar((Float)data.get("amount"));
				return true;
				
			case FINANCE_ENTRANCECOST_AMOUNT:
				financeModel.setEntranceCosts((Integer)data.get("amount"));
				return true;
			case FINANCE_TICKETMACHINECOST_AMOUNT:
				financeModel.setTicketMachineCosts((Integer)data.get("amount"));
				return true;
			case FINANCE_EXITCOST_AMOUNT:
				financeModel.setExitCosts((Integer)data.get("amount"));
				return true;
				
			case FINANCE_PLACECOST_AMOUNT:
				financeModel.setPlaceCosts((Float)data.get("amount"));
				return true;
			case FINANCE_ROWCOST_AMOUNT:
				financeModel.setRowCosts((Integer)data.get("amount"));
				return true;
			case FINANCE_FLOORCOST_AMOUNT:
				financeModel.setFloorCosts((Integer)data.get("amount"));
				return true;
				
			case FINANCE_MAINTENANCECOSTS_AMOUNT:
				financeModel.setMaintenanceCosts((Integer)data.get("amount"));
				return true;
				
			default:
				break;
		}
		return false;
	}
	
	/**
	 * @return the type of controller
	 */
	@Override
	public ControllerType getControllerType() {
		return ControllerType.FINANCE;
	}
}
