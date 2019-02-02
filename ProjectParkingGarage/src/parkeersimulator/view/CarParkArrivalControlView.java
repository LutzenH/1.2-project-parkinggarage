package parkeersimulator.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JSpinner;

import parkeersimulator.controller.AbstractController.ActionType;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;

/**
 * Class of a view that can start and stop the simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class CarParkArrivalControlView extends AbstractControllableView{
	/**
	 * Constructor of CarParkArrivalControlView
	 * @param model The model this view should be getting information from.
	 * @param controller The controller this view should send instruction to.
	 */ 
	public CarParkArrivalControlView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);

		ActionListener actionListener;
		
		//BaseArrival amounts
		JSpinner baseLineArrivalsAmountSpinner = buildComponent_spinner(model.getBaseLineArrivals(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_BASELINE_AMOUNT, new HashMap<String, Object>() {{ put("amount", baseLineArrivalsAmountSpinner.getValue()); }});
		JPanel baseLineArrivalsAmountController = buildController_spinner("Base arrivals", "Set", baseLineArrivalsAmountSpinner, actionListener);
		

		//AdHocArrival amounts
		JSpinner adHocWeekArrivalsAmountSpinner = buildComponent_spinner(model.getAdHocArrivalsPercent_week(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_ADHOCWEEK_AMOUNT, new HashMap<String, Object>() {{ put("amount", adHocWeekArrivalsAmountSpinner.getValue()); }});
		JPanel adHocWeekArrivalsAmountController = buildController_spinner("AdHoc week arrivals", "Set", adHocWeekArrivalsAmountSpinner, "in %", actionListener);
		
		JSpinner adHocWeekendArrivalsAmountSpinner = buildComponent_spinner(model.getAdHocArrivals_weekend(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_ADHOCWEEKEND_AMOUNT, new HashMap<String, Object>() {{ put("amount", adHocWeekendArrivalsAmountSpinner.getValue()); }});
		JPanel adHocWeekendArrivalsAmountController = buildController_spinner("AdHoc weekend arrivals", "Set", adHocWeekendArrivalsAmountSpinner, "in %", actionListener);
		
		JSpinner adHocEventArrivalsAmountSpinner = buildComponent_spinner(model.getAdHocArrivals_event(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_ADHOCEVENT_AMOUNT, new HashMap<String, Object>() {{ put("amount", adHocEventArrivalsAmountSpinner.getValue()); }});
		JPanel adHocEventdArrivalsAmountController = buildController_spinner("AdHoc event arrivals", "Set", adHocEventArrivalsAmountSpinner, "in %", actionListener);
		
		//PassArrival amounts
		JSpinner passWeekArrivalsAmountSpinner = buildComponent_spinner(model.getPassArrivals_week(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_PASSWEEK_AMOUNT, new HashMap<String, Object>() {{ put("amount", passWeekArrivalsAmountSpinner.getValue()); }});
		JPanel passWeekArrivalsAmountController = buildController_spinner("Pass week arrivals", "Set", passWeekArrivalsAmountSpinner, "in %", actionListener);
		
		JSpinner passWeekendArrivalsAmountSpinner = buildComponent_spinner(model.getPassArrivals_weekend(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_PASSWEEKEND_AMOUNT, new HashMap<String, Object>() {{ put("amount", passWeekendArrivalsAmountSpinner.getValue()); }});
		JPanel passWeekendArrivalsAmountController = buildController_spinner("Pass weekend arrivals", "Set", passWeekendArrivalsAmountSpinner, "in %", actionListener);
		
		JSpinner passEventWeekArrivalsAmountSpinner = buildComponent_spinner(model.getPassArrivals_eventWeek(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_PASSEVENTWEEK_AMOUNT, new HashMap<String, Object>() {{ put("amount", passEventWeekArrivalsAmountSpinner.getValue()); }});
		JPanel passEventWeekArrivalsAmountController = buildController_spinner("Pass week event arrivals", "Set", passEventWeekArrivalsAmountSpinner, "in %", actionListener);
		
		JSpinner maxPassAmountSpinner = buildComponent_spinner(model.getNumberOfPassHolderSpots(), 0, 300, 10);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_MAXPASS_AMOUNT, new HashMap<String, Object>() {{ put("amount", maxPassAmountSpinner.getValue()); }});
		JPanel maxPassAmountController = buildController_spinner("Max pass places", "Set", maxPassAmountSpinner, actionListener);
		
		//ReservationArrival amounts
		JSpinner reservationWeekArrivalsAmountSpinner = buildComponent_spinner(model.getReservationArrivals_week(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_RESERVATIONWEEK_AMOUNT, new HashMap<String, Object>() {{ put("amount", reservationWeekArrivalsAmountSpinner.getValue()); }});
		JPanel reservationWeekArrivalsAmountController = buildController_spinner("Reservation week arrivals", "Set", reservationWeekArrivalsAmountSpinner, "in %", actionListener);
		
		JSpinner reservationWeekendArrivalsAmountSpinner = buildComponent_spinner(model.getReservationArrivals_weekend(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_RESERVATIONWEEKEND_AMOUNT, new HashMap<String, Object>() {{ put("amount", reservationWeekendArrivalsAmountSpinner.getValue()); }});
		JPanel reservationWeekendArrivalsAmountController = buildController_spinner("Reservation weekend arrivals", "Set", reservationWeekendArrivalsAmountSpinner, "in %", actionListener);
		
		JSpinner reservationEventArrivalsAmountSpinner = buildComponent_spinner(model.getReservationArrivals_event(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.PARKINGGARAGE_RESERVATIONEVENT_AMOUNT, new HashMap<String, Object>() {{ put("amount", reservationEventArrivalsAmountSpinner.getValue()); }});
		JPanel reservationEventdArrivalsAmountController = buildController_spinner("Reservation event arrivals", "Set", reservationEventArrivalsAmountSpinner, "in %", actionListener);
			
		
		//Set the layoutManager of the JPanel that all these controls sit on
		this.setLayout(new FlowLayout());
		
		//Add the components to the panel
		add(baseLineArrivalsAmountController);
		
		add(adHocWeekArrivalsAmountController);
		add(adHocWeekendArrivalsAmountController);
		add(adHocEventdArrivalsAmountController);
		
		add(passWeekArrivalsAmountController);
		add(passWeekendArrivalsAmountController);
		add(passEventWeekArrivalsAmountController);
		add(maxPassAmountController);
		
		add(reservationWeekArrivalsAmountController);
		add(reservationWeekendArrivalsAmountController);
		add(reservationEventdArrivalsAmountController);

		setVisible(true);
	}
	
	
}
