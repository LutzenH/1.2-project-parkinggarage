package parkeersimulator.view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;

import parkeersimulator.controller.AbstractController.ActionType;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.ParkingGarageModel.CustomiseErrorMessages;

/**
 * The view that is used for opening and closing the garage
 * @author LutzenH
 *
 */
public class OpenGarageView extends AbstractControllableView {
	
	private JButton openGarageButton;
	private JLabel openGarageHint;
	
	private CustomiseErrorMessages errorMessage = CustomiseErrorMessages.ERROR_CUSTOMISE_NOTREADY;
	
	/**
	 * Constructor for this view
	 * @param model the ParkingGarageModel this view gets data from.
	 * @param controller the controller this view sends instructions.
	 */
	public OpenGarageView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);
		
		openGarageButton = new JButton("Open Garage");
		openGarageButton.addActionListener(e -> clickOpenButton());
		add(openGarageButton);	
		
		openGarageHint = new JLabel();
		setHintMessage(errorMessage);
		openGarageHint.setForeground(new Color(255, 0, 0));
		add(openGarageHint);
	}
	
    @Override
    public void updateView() {
    	ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
    	
    	if(parkingGarageModel.isGarageOpen()) {
    		openGarageButton.setText("Close Garage");
    	}
    	else {
    		openGarageButton.setText("Open Garage");
    	}
    	
    	setHintMessage(parkingGarageModel.getCustomisationErrorMessages());
    	
    	repaint();
    }
    
    /**
     * Will perform these actions when the button is pressed.
     */
    private void clickOpenButton() {
    	controller.performAction(ActionType.PARKINGGARAGE_OPEN_GARAGE);
    	updateView();
    }
    
    /**
     * Sets the hint message for this view.
     * @param errorMessage the Enum message that should set the hint message.
     */
    private void setHintMessage(CustomiseErrorMessages errorMessage) {
    	if(errorMessage != null) {
        	switch(errorMessage) {
    			case ERROR_CUSTOMISE_NOTCLOSED:
    				openGarageHint.setText("The Garage has not been closed yet.");
    				break;
    			case ERROR_CUSTOMISE_NOTEMPTY:
    				openGarageHint.setText("The Garage is not empty yet.");
    				break;
    			case ERROR_CUSTOMISE_NOTREADY:
    				openGarageHint.setText("The Garage requires at least 1 Ticket Machine, 1 Entrance and 1 Exit.");
    				break;
    			default:
    				openGarageHint.setText("");
    				break;
        	}
    	} else {
        	openGarageHint.setText("");
    	}
    }
}
