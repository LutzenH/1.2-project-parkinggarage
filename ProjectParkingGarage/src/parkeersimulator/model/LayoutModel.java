package parkeersimulator.model;

import parkeersimulator.handler.ModelHandler;

/**
 * The model that handles the layout of the parking garage.
 * @author ThowV
 *
 */
public class LayoutModel extends AbstractModel {
	///Other Models required for this model to work
	ParkingGarageModel parkingGarageModel;
	
	/**
	 * The Constructor for LayoutModel
	 * @param modelHandler the modelHandler this LayoutModel should be added to.
	 */
	public LayoutModel(ModelHandler modelHandler) {
		super(modelHandler);
		parkingGarageModel = (ParkingGarageModel) modelHandler.getModel(ModelType.PARKINGGARAGE);
	}

	@Override
	public ModelType getModelType() {
		return ModelType.LAYOUT;
	}

	@Override
	public void tick() {}
	
	/**
	 * @return the number of floors in the Parking Garage
	 */
	public int getNumberOfFloors() { return parkingGarageModel.getNumberOfFloors(); }
	
	/**
	 * Sets the number of floors in the Parking Garage.
	 * @param amount the number of floors
	 */
	public void setNumberOfFloors(int amount) { parkingGarageModel.setNumberOfFloors(amount); }
	
	/**
	 * @return the number of rows in the garage per floor
	 */
	public int getNumberOfRows() { return parkingGarageModel.getNumberOfRows(); }
	
	/**
	 * Sets the number of rows per floor in the Parking Garage.
	 * @param amount the number of rows
	 */
	public void setNumberOfRows(int amount) { parkingGarageModel.setNumberOfRows(amount); }
	
	/**
	 * @return the number of places in the garage per row
	 */
	public int getNumberOfPlaces() { return parkingGarageModel.getNumberOfPlaces(); }
	
	/**
	 * Sets the number of places per row in the Parking Garage.
	 * @param amount the number of rows
	 */
	public void setNumberOfPlaces(int amount) { parkingGarageModel.setNumberOfPlaces(amount); }
	
	/**
	 * @return the number of passHolderSpots.
	 */
	public int getNumberOfPassHolderSpots() { return parkingGarageModel.getNumberOfPassHolderSpots(); }
	
	/**
	 * Sets the number of passholderspots in the Parking Garage.
	 * @param amount the number of passholderspots
	 */
	public void setNumberOfPassHolderSpots(int amount) { parkingGarageModel.setNumberOfPassHolderSpots(amount); }
}
