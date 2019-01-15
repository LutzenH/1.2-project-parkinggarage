package parkeersimulator.view;

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

/**
 * GraphView is a View used for displaying GUI Graphs using the JFreeChart API.
 * @author LutzenH
 * 
 * JFreeChart is released under the LGPL-license,
 * site: http://www.jfree.org/jfreechart/
 */
public class GraphView extends AbstractView {
	///Time between ticks for collecting data.
	private static final int DATA_COLLECT_FREQUENTY = 60;
	
	//TODO Temporary placeholder until time has been properly implemented.
	private int time = 0;
	
	///Declaration of dataset collection of the data that needs to be displayed.
	private XYSeriesCollection dataset;
	///Declaration of an Array of each individual line represented in the graph.
	private XYSeries[] graph_data;
	
	///Declaration of JFreeChart Swing panels.
	private JFreeChart chart;
	private ChartPanel panel;
	
	/**
	 * Constructor of GraphView
	 * @param model The ParkingGarageModel this view uses to display data.
	 */
    public GraphView(ParkingGarageModel model) {
        super(model);
        
        setName("Graph #1");
        
        dataset = new XYSeriesCollection();
        
        graph_data = new XYSeries[2];
        graph_data[0] = new XYSeries("AdHocCar");
        graph_data[1] = new XYSeries("ParkingPassCar");
        
        for(XYSeries data : graph_data)
        {
            dataset.addSeries(data);
        }
        
        chart = createChart(dataset);
        panel = new ChartPanel(chart);
        
        ///Allow the mouse wheel to scroll.
        panel.setMouseWheelEnabled(true);
        
        this.setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }
    
    /**
     * Updates the data in the data-set and redisplays it. if the time (x) is already in the data-set it will be overwritten.
     */
    private void updateDataset() {
    	graph_data[0].addOrUpdate(time, model.getEntranceCarQueue().carsInQueue());
    	graph_data[1].addOrUpdate(time, model.getEntrancePassQueue().carsInQueue());  	
    	
    	///Refreshes the UI element to display the new data in the UI.
    	panel.updateUI();
    }

    /**
     * Creates a JFreeChart which can be used to display data.
     * 
     * @param dataset The data-set that should be displayed in this chart
     * @return A JFreeChart which can be used to display data.
     */
    private JFreeChart createChart(XYDataset dataset) {
        ///Initiates the chart.
        JFreeChart chart = ChartFactory.createXYLineChart("Cars in queue", "time", "amount", dataset, PlotOrientation.VERTICAL, true, true, false);

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
     * Repaints this view, will only update the data-set every 60th tick.
     */
    @Override
    public void updateView() {
    	if(time % DATA_COLLECT_FREQUENTY == 0)
    		updateDataset();
    	
    	time++;
    	
    	repaint();
    }
}