package parkeersimulator.view.graph;

import java.awt.Color;

import org.jfree.data.xy.XYSeries;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.FinanceModel;

public class FinanceGraphView extends GraphView {

	public FinanceGraphView(AbstractModel model) {
		super(
				model,
				"Money amount",
				new XYSeries[] {
						new XYSeries("MoneyTotal"),
						new XYSeries("MoneyMonth"),
						new XYSeries("MoneyDay"),
				},
				new Color[] {
						Color.GREEN,
						Color.BLUE,
				},
				"time (minutes)",
				"amount",
				15
		);
	}

	@Override
	protected void updateDataset() {
		FinanceModel financeModel = (FinanceModel) model;
		graph_data[0].addOrUpdate(time, financeModel.getMoneyTotal());
		graph_data[1].addOrUpdate(time, financeModel.getMoneyMonth());
		graph_data[2].addOrUpdate(time, financeModel.getMoneyDay());
	}
	
}
