package parkeersimulator.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.time.Month;

import parkeersimulator.model.FinanceModel;
import parkeersimulator.model.TimeModel;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;

public class MonthlyReportView extends AbstractView {

	private Box ReportBox;
	
	TimeModel timeModel;
	
	/**
	 * Create the panel.
	 */
	public MonthlyReportView(FinanceModel model, TimeModel timeModel) {
		super(model);
		
		this.timeModel = timeModel;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		ReportBox = Box.createVerticalBox();
		
		JScrollPane scrollPane = new JScrollPane(ReportBox);
		add(scrollPane);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}

	public void updateView() {
		FinanceModel modelFinance = (FinanceModel) model;
		
		if(timeModel.getIsFirstDayOfMonth() && timeModel.getHour() == 8 && timeModel.getMinute() == 31) {
			GenerateReport(modelFinance.getMonthlyReport());
		}
		
		repaint();
	}
	
	private class MonthlyReport extends JPanel {
		public MonthlyReport(String Title, String TextFieldString) {
			this.setLayout(new BorderLayout(0, 0));
			
			JLabel TitleLabel = new JLabel(Title);
			TitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(TitleLabel, BorderLayout.NORTH);
			
			JEditorPane TextPane = new JEditorPane("text/html", "");;
			TextPane.setText(TextFieldString);
			this.add(TextPane, BorderLayout.CENTER);
		}
	}
	
	public void GenerateReport(float[] values) {
		String title = "";
		String textField = "";
		
		title += getMonthString(timeModel.getTime().getMonth().minus(1));
		title += " " + timeModel.getYear() + " - Financial Report";
		
		textField += "<div></div>";
		textField += "<center><div><b>Income</b></div>";
		textField += "<div>Income Regular Cars: " + values[0] + "  </div>";
		textField += "<div>Income Reservation Cars: " + values[2] + " </div>";
		textField += "<div>Income PassHolders: " + values[1] + "  </div>";
		textField += "<div></div>";
		textField += "<hr>";
		textField += "<div><b>Cost</b></div>";
		textField += "<div>Maintenance: " + values[3] + " </div>";
		textField += "<div>Taxes: " + values[4] + " </div>";
		textField += "<div></div>";
		textField += "<hr>";
		
		float totalIncome = values[0] + values[1] + values[2];
		float totalCosts = values[3] + values[4];
		
		textField += "<div>Total Income: <b>+" + totalIncome + "</b> </div>";
		textField += "<div>Total Costs: <b>" + totalCosts + "</b> </div>";
		textField += "<div></div>";
		textField += "<div>Revenue: <b>" + (totalIncome + totalCosts) + "</b> \n";
		textField += "<hr>";
		textField += "<div></div></center>";
		
		ReportBox.add(new MonthlyReport(title, textField), 0);
		
		}

	private static String getMonthString(Month month) {
		switch(month) {
			case APRIL:
				return "April";
			case AUGUST:
				return "August";
			case DECEMBER:
				return "December";
			case FEBRUARY:
				return "February";
			case JANUARY:
				return "January";
			case JULY:
				return "July";
			case JUNE:
				return "June";
			case MARCH:
				return "March";
			case MAY:
				return "May";
			case NOVEMBER:
				return "November";
			case OCTOBER:
				return "October";
			case SEPTEMBER:
				return "September";
				
			default:
				return "INVALID MONTH";
		}
	}
	
}
