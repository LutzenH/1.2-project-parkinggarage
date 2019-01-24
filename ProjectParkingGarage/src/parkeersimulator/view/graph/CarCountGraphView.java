package parkeersimulator.view.graph;

import org.jfree.data.xy.XYSeries;

import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.Car.CarType;

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
		graph_data[0].addOrUpdate(time, model.getCarCount(Car.CarType.AD_HOC));
		graph_data[1].addOrUpdate(time, model.getCarCount(Car.CarType.PASS));
		graph_data[2].addOrUpdate(time, model.getCarCount(Car.CarType.RESERVERATION_CAR));
	}

}
