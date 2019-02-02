package parkeersimulator.view.graph;

import java.awt.Color;

import org.jfree.data.time.TimeSeries;

import parkeersimulator.model.FinanceModel;
import parkeersimulator.model.TimeModel;
/**
 * A graph that displays the amount income and total money
 * @author ThowV
 */
public class FinanceGraphView extends GraphView {

	/**
	 * Constructor for FinanceGraphView
	 * @param model the financemodel data should be retrieved from.
	 * @param timeModel the timeModel the current time is retrieved from.
	 */
	public FinanceGraphView(FinanceModel model, TimeModel timeModel) {
		super(
				model,
				timeModel,
				"Money amount",
				new TimeSeries[] {
						new TimeSeries("Total Money"),
						new TimeSeries("Money this month"),
						new TimeSeries("Money Day")
				},
				new Color[] {
						Color.GREEN,
						Color.BLUE,
						Color.RED
				},
				"time (minutes)",
				"amount (€)",
				15
		);
	}

	@Override
	protected void updateDataset() {
		FinanceModel financeModel = (FinanceModel) model;
		
		graph_data[0].addOrUpdate(minute, financeModel.getMoneyTotal());
		graph_data[1].addOrUpdate(minute, financeModel.getMoneyMonth());
		graph_data[2].addOrUpdate(minute, financeModel.getMoneyDay());
	}
	
}
