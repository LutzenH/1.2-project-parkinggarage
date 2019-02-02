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
						new TimeSeries("Money Day"),
						new TimeSeries("Money Missed out on")
				},
				new Color[] {
						Color.GREEN,
						Color.BLUE,
						Color.CYAN,
						Color.RED
				},
				"time (minutes)",
				"amount (€)",
				1
		);
	}

	@Override
	protected void updateDataset() {
		FinanceModel financeModel = (FinanceModel) model;

		if(timeModel.getIsFirstDayOfMonth() && timeModel.getHour() == 8 && timeModel.getMinute() == 31) {
			graph_data[0].addOrUpdate(minute, financeModel.getMoneyTotal());
        }
		
		if(timeModel.getIsFirstDayOfMonth() && timeModel.getHour() == 8 && timeModel.getMinute() == 31) {
			graph_data[1].addOrUpdate(minute, financeModel.getMoneyLastMonth());
        }
		
		if(timeModel.getHour() == 23 && timeModel.getMinute() == 59) {
			graph_data[2].addOrUpdate(minute, financeModel.getMoneyDay());
        }
		
		if(timeModel.getHour() == 23 && timeModel.getMinute() == 59) {
			graph_data[3].addOrUpdate(minute, financeModel.getMoneyMissedOutOn());
        }
	}
	
}
