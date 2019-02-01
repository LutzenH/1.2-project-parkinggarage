package parkeersimulator.view;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import parkeersimulator.controller.LayoutController;
import parkeersimulator.controller.AbstractController.ActionType;
import parkeersimulator.model.LayoutModel;

/**
 * Class of a view that can start and stop the simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class CarParkLayoutControlView extends AbstractControllableView{
	/**
	 * Constructor of CarParkLayoutControlView
	 * @param layoutModel The model this view should be getting information from.
	 * @param layoutController The controller this view should send instruction to.
	 */ 
	public CarParkLayoutControlView(LayoutModel layoutModel, LayoutController layoutController) {
		super(layoutModel, layoutController);
		
		ChangeListener changeListener;
		ActionListener actionListener;

		//Floor amount
		JSpinner floorAmountSpinner = buildComponent_spinner(layoutModel.getNumberOfFloors(), 1, 5, 1);
		actionListener = e -> layoutController.performAction(ActionType.LAYOUT_ADD_FLOOR, new HashMap<String, Object>() {{ put("amount", floorAmountSpinner.getValue()); }});
		JPanel floorAmountController = buildController_spinner("Floor amount", "Set", floorAmountSpinner, actionListener);
		
		//Row amount
		JSpinner rowAmountSpinner = buildComponent_spinner(layoutModel.getNumberOfRows(), 2, 16, 2);
		actionListener = e -> layoutController.performAction(ActionType.LAYOUT_ADD_ROW, new HashMap<String, Object>() {{ put("amount", rowAmountSpinner.getValue()); }});
		JPanel rowAmountController = buildController_spinner("Row amount", "Set", rowAmountSpinner, actionListener);
		
		//Row amount
		JSpinner placeAmountSpinner = buildComponent_spinner(layoutModel.getNumberOfPlaces(), 10, 50, 1);
		actionListener = e -> layoutController.performAction(ActionType.LAYOUT_ADD_PLACE, new HashMap<String, Object>() {{ put("amount", placeAmountSpinner.getValue()); }});
		JPanel placeAmountController = buildController_spinner("place amount", "Set", placeAmountSpinner, actionListener);

		//Set the layoutManager of the JPanel that all these controls sit on
		this.setLayout(new FlowLayout());
		
		//Add the components to the panel
		add(floorAmountController);
		add(rowAmountController);
		add(placeAmountController);

		setVisible(true);
	}
}
