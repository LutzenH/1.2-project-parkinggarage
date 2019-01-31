package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.AbstractModel.ModelType;
import parkeersimulator.model.FinanceModel;

public class FinanceController extends AbstractController {
	private FinanceModel financeModel;
	
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
			case FINANCE_COSTPERTIMEFRAME_AMOUNT:
				financeModel.setCostPerTimeFrame((Float)data.get("amount"));
				return true;
			case FINANCE_MAINTENANCECOSTS_AMOUNT:
				financeModel.setMaintenanceCosts((Integer)data.get("amount"));
				return true;
		}
		return false;
	}
	
	@Override
	public ControllerType getControllerType() {
		return ControllerType.FINANCE;
	}
}
