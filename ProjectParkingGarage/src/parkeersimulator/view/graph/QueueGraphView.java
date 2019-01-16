package parkeersimulator.view.graph;

import org.jfree.data.xy.XYSeries;

import parkeersimulator.model.ParkingGarageModel;

public class QueueGraphView extends GraphView {

	public QueueGraphView(ParkingGarageModel model) {
		super(
				model,
				"Car Queue Size",
				new XYSeries[] {
						new XYSeries("AdHocCar"),
						new XYSeries("ParkingPassCar")
				},
				"time (minutes)",
				"amount",
				60
		);
	}

	@Override
	protected void updateDataset() {
		graph_data[0].addOrUpdate(time, model.getEntranceCarQueue().carsInQueue());
		graph_data[1].addOrUpdate(time, model.getEntrancePassQueue().carsInQueue());
	}

}
