package parkeersimulator.view.graph;

import org.jfree.data.xy.XYSeries;

import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.ParkingGarageModel.CarType;

public class CarCountGraphView extends GraphView {

	public CarCountGraphView(ParkingGarageModel model) {
		super(
				model,
				"Total Car Count",
				new XYSeries[] {
						new XYSeries("AdHocCar"),
						new XYSeries("ParkingPassCar"),
						new XYSeries("ReservationCar")
				},
				"time (minutes)",
				"amount",
				15
		);
	}

	@Override
	protected void updateDataset() {
		ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
		graph_data[0].addOrUpdate(time, parkingGarageModel.getCarCount(CarType.AD_HOC));
		graph_data[1].addOrUpdate(time, parkingGarageModel.getCarCount(CarType.PASS));
		graph_data[2].addOrUpdate(time, parkingGarageModel.getCarCount(CarType.RESERVERATION_CAR));
	}

}
