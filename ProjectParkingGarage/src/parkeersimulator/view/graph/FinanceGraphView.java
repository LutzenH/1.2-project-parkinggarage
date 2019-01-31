package parkeersimulator.view.graph;

import java.awt.Color;

import org.jfree.data.xy.XYSeries;

import parkeersimulator.model.FinanceModel;
import parkeersimulator.model.ParkingGarageModel;

public class FinanceGraphView extends GraphView {
	
	private FinanceModel financeModel;

	public FinanceGraphView(ParkingGarageModel parkingGarageModel, FinanceModel financeModel) {
		super(
				parkingGarageModel,
				"Money amount",
				new XYSeries[] {
						new XYSeries("MoneyTotal"),
						new XYSeries("MoneyMonth"),
				},
				new Color[] {
						Color.GREEN,
						Color.BLUE,
				},
				"time (minutes)",
				"amount",
				15
		);
		this.financeModel = financeModel;
	}

	@Override
	protected void updateDataset() {
		//FinanceModel financeModel = (FinanceModel) model;
		graph_data[0].addOrUpdate(time, financeModel.getMoneyTotal());
		graph_data[1].addOrUpdate(time, financeModel.getMoneyMonth());
	}
	
}
