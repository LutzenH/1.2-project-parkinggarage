package parkeersimulator.view.graph;

import java.awt.Color;
import java.time.ZoneOffset;
import java.util.Date;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;

import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.TimeModel;

public class QueueGraphView extends GraphView {

	public QueueGraphView(ParkingGarageModel model, TimeModel timeModel) {
		super(
				model,
				timeModel,
				"Car Queue Size",
				new TimeSeries[] {
						new TimeSeries("Regular Entrance Queue"),
						new TimeSeries("Pass & Reservation Entrance Queue"),
						new TimeSeries("Payment Queue"),
						new TimeSeries("Exit Queue")
				},
				new Color[] {
						Color.GREEN,
						Color.BLUE,
						Color.ORANGE,
						Color.RED
				},
				"time (minutes)",
				"amount (cars)",
				60
		);
	}

	@Override
	protected void updateDataset() {
		ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
				
		graph_data[0].addOrUpdate(minute, parkingGarageModel.getEntranceCarQueue().carsInQueue());
		graph_data[1].addOrUpdate(minute, parkingGarageModel.getEntrancePassQueue().carsInQueue());
		graph_data[2].addOrUpdate(minute, parkingGarageModel.getPaymentCarQueue().carsInQueue());
		graph_data[3].addOrUpdate(minute, parkingGarageModel.getExitCarQueue().carsInQueue());
	}

}
