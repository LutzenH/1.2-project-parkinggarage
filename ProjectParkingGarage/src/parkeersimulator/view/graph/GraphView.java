package parkeersimulator.view.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import parkeersimulator.model.ParkingGarageModel;
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
	
	//TODO Temporary placeholder until time has been properly implemented.
	protected int time = 0;
	
	///Declaration of dataset collection of the data that needs to be displayed.
	protected XYSeriesCollection dataset;
	
	///Declaration of an Array of each individual line represented in the graph.
	protected XYSeries[] graph_data;
	
	///Declaration of JFreeChart Swing panels.
	protected JFreeChart chart;
	protected ChartPanel panel;
	
	/**
	 * Constructor of GraphView
	 * @param model The ParkingGarageModel this view uses to display data.
	 */
    public GraphView(ParkingGarageModel model, String tableName, XYSeries[] dataSeries, String xAxisName, String yAxisName, int dataCollectFrequency) {
        super(model);
        
        this.dataCollectFrequency = dataCollectFrequency;
        
        dataset = new XYSeriesCollection();
        
        this.graph_data = dataSeries;
        
        for(int i = 0; i < graph_data.length; i++) {
            dataset.addSeries(graph_data[i]);
        }
        
        chart = createChart(dataset, tableName, xAxisName, yAxisName);
        panel = new ChartPanel(chart);
        
        ///Allow the mouse wheel to scroll.
        panel.setMouseWheelEnabled(true);
        
        this.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }
    
    /**
     * Updates the data in the data-set and redisplays it. if the time (x) is already in the data-set it will be overwritten.
     */
    protected abstract void updateDataset();
    /*
	{
    	graph_data[0].addOrUpdate(time, model.getEntranceCarQueue().carsInQueue());
    	graph_data[1].addOrUpdate(time, model.getEntrancePassQueue().carsInQueue());  	
    	
    	///Refreshes the UI element to display the new data in the UI.
    }
     */

    /**
     * Creates a JFreeChart which can be used to display data.
     * 
     * @param dataset The data-set that should be displayed in this chart
     * @return A JFreeChart which can be used to display data.
     */
    private JFreeChart createChart(XYDataset dataset, String tableName, String xAxisName, String yAxisName) {
        ///Initiates the chart.
        JFreeChart chart = ChartFactory.createXYLineChart(tableName, xAxisName, yAxisName, dataset, PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        XYLineAndShapeRenderer renderer
                = (XYLineAndShapeRenderer) plot.getRenderer();
        
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
    	if(time % dataCollectFrequency == 0)
    		updateDataset();
    	
    	panel.updateUI();
    	
    	time++;
    	
    	repaint();
    }
}