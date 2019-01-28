package parkeersimulator.view.graph;

import org.jfree.data.xy.XYSeries;

import parkeersimulator.model.ParkingGarageModel;

public class QueueGraphView extends GraphView {

	public QueueGraphView(ParkingGarageModel model) {
		super(
				model,
				"Car Queue Size",
				new XYSeries[] {
						new XYSeries("EntranceCarQueue"),
						new XYSeries("EntrancePassQueue"),
						new XYSeries("PaymentQueue"),
						new XYSeries("ExitQueue")
				},
				"time (minutes)",
				"amount",
				60
		);
	}

	@Override
	protected void updateDataset() {
		ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
		graph_data[0].addOrUpdate(time, parkingGarageModel.getEntranceCarQueue().carsInQueue());
		graph_data[1].addOrUpdate(time, parkingGarageModel.getEntrancePassQueue().carsInQueue());
		graph_data[2].addOrUpdate(time, parkingGarageModel.getPaymentCarQueue().carsInQueue());
		graph_data[3].addOrUpdate(time, parkingGarageModel.getExitCarQueue().carsInQueue());
	}

}
