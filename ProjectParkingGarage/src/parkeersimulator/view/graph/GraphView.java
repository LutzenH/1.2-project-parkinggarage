package parkeersimulator.view.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.ZoneOffset;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.TimeModel;
import parkeersimulator.view.AbstractView;

/**
 * GraphView is a View used for displaying GUI Graphs using the JFreeChart API.
 * @author LutzenH
 * 
 * JFreeChart is released under the LGPL-license,
 * site: http://www.jfree.org/jfreechart/
 */
public abstract class GraphView extends AbstractView {
	///Time between ticks for collecting data.
	protected int dataCollectFrequency;
	
	///Declaration of dataset collection of the data that needs to be displayed.
	protected TimeSeriesCollection dataset;
	
	///Declaration of an Array of each individual line represented in the graph.
	protected TimeSeries[] graph_data;
	
	///Declaration of JFreeChart Swing panels.
	protected JFreeChart chart;
	protected ChartPanel panel;
	
	protected TimeModel timeModel;
	protected Minute minute;
	
	/**
	 * Constructor of GraphView
	 * @param model The ParkingGarageModel this view uses to display data.
	 */
    public GraphView(AbstractModel model, TimeModel timeModel, String tableName, TimeSeries[] dataSeries, Color[] colors, String xAxisName, String yAxisName, int dataCollectFrequency) {
        super(model);
        
        this.timeModel = timeModel;
        
        this.minute = new Minute(timeModel.getMinute(), timeModel.getHour(), timeModel.getDayOfTheMonth(), timeModel.getMonth(), timeModel.getYear());
        
        this.dataCollectFrequency = dataCollectFrequency;
        
        dataset = new TimeSeriesCollection();
        
        this.graph_data = dataSeries;
        
        for(int i = 0; i < graph_data.length; i++) {
            dataset.addSeries(graph_data[i]);
        }
        
        chart = createChart(dataset, tableName, xAxisName, yAxisName, colors);
        panel = new ChartPanel(chart);
        
        ///Allow the mouse wheel to scroll.
        panel.setMouseWheelEnabled(true);
        panel.setPopupMenu(null);
        
        this.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }
    
    /**
     * Updates the data in the data-set and redisplays it. if the time (x) is already in the data-set it will be overwritten.
     */
    protected abstract void updateDataset();

    /**
     * Creates a JFreeChart which can be used to display data.
     * 
     * @param dataset The data-set that should be displayed in this chart
     * @return A JFreeChart which can be used to display data.
     */
    private JFreeChart createChart(XYDataset dataset, String tableName, String xAxisName, String yAxisName, Color[] lineColors) {
        ///Initiates the chart.
        JFreeChart chart = ChartFactory.createTimeSeriesChart(tableName, xAxisName, yAxisName, dataset, true, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        XYLineAndShapeRenderer renderer
                = (XYLineAndShapeRenderer) plot.getRenderer();
        
        for(int i = 0; i < lineColors.length; i++) {
        	renderer.setSeriesPaint(i, lineColors[i]);
        }
        
        ///Can be used to display shapes around each individual data-point.
        renderer.setBaseShapesVisible(false);
        renderer.setBaseShapesFilled(true);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }
    
    /**
     * Repaints this view, will only update the data-set every DATA_COLLECT_FREQUENTY'th tick.
     */
    @Override
    public void updateView() {
    	if(timeModel.getMinute() % dataCollectFrequency == 0)
    	{
    		minute = new Minute(timeModel.getMinute(), timeModel.getHour(), timeModel.getDayOfTheMonth(), timeModel.getMonth(), timeModel.getYear());
    		updateDataset();
    		panel.updateUI();
    	}
    	
    	long time = timeModel.getTime().toEpochSecond(ZoneOffset.UTC) * 1000;
    	
    	chart.getXYPlot().getDomainAxis().setRange(time-259200000, time+259200000);
    		
    }
}