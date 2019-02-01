package parkeersimulator.view.graph;

import java.awt.Color;

import org.jfree.data.time.TimeSeries;

import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.TimeModel;
import parkeersimulator.model.car.AdHocCar;
import parkeersimulator.model.car.Car.CarType;
import parkeersimulator.model.car.ParkingPassCar;
import parkeersimulator.model.car.ReservationCar;

public class CarCountGraphView extends GraphView {

	public CarCountGraphView(ParkingGarageModel model, TimeModel timeModel) {
		super(
				model,
				timeModel,
				"Total Car Count",
				new TimeSeries[] {
						new TimeSeries("Regular Cars"),
						new TimeSeries("Parking Pass Cars"),
						new TimeSeries("Reservation Cars")
				},
				new Color[] {
						AdHocCar.COLOR,
						ParkingPassCar.COLOR,
						ReservationCar.COLOR,
				},
				"time (minutes)",
				"amount (cars)",
				15
		);
	}

	@Override
	protected void updateDataset() {
		ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
		
		graph_data[0].addOrUpdate(minute, parkingGarageModel.getCarCount(CarType.AD_HOC));
		graph_data[1].addOrUpdate(minute, parkingGarageModel.getCarCount(CarType.PASS));
		graph_data[2].addOrUpdate(minute, parkingGarageModel.getCarCount(CarType.RESERVERATION_CAR));
	}

}
