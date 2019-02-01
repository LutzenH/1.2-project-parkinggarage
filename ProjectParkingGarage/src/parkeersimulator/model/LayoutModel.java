package parkeersimulator.model;

import parkeersimulator.handler.ModelHandler;

public class LayoutModel extends AbstractModel {
	ParkingGarageModel parkingGarageModel;
	
	public LayoutModel(ModelHandler modelHandler) {
		super(modelHandler);
		parkingGarageModel = (ParkingGarageModel) modelHandler.getModel(ModelType.PARKINGGARAGE);
	}

	@Override
	public ModelType getModelType() {
		return ModelType.LAYOUT;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}
	
	public int getNumberOfFloors() { return parkingGarageModel.getNumberOfFloors(); }
	public void setNumberOfFloors(int amount) { parkingGarageModel.setNumberOfFloors(amount); }
	
	public int getNumberOfRows() { return parkingGarageModel.getNumberOfRows(); }
	public void setNumberOfRows(int amount) { parkingGarageModel.setNumberOfRows(amount); }
	
	public int getNumberOfPlaces() { return parkingGarageModel.getNumberOfPlaces(); }
	public void setNumberOfPlaces(int amount) { parkingGarageModel.setNumberOfPlaces(amount); }
	
	public int getNumberOfPassHolderSpots() { return parkingGarageModel.getNumberOfPassHolderSpots(); }
	public void setNumberOfPassHolderSpots(int amount) { parkingGarageModel.setNumberOfPassHolderSpots(amount); }
}
