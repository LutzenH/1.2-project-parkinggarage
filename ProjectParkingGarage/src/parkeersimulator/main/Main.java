package parkeersimulator.main;

import javax.swing.JPanel;

import parkeersimulator.controller.FinanceController;
import parkeersimulator.controller.LayoutController;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.controller.TimeController;
import parkeersimulator.handler.ControllerHandler;
import parkeersimulator.handler.ModelHandler;
import parkeersimulator.model.FinanceModel;
import parkeersimulator.model.LayoutModel;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.TimeModel;
import parkeersimulator.view.AdviceView;
import parkeersimulator.view.CarParkArrivalControlView;
import parkeersimulator.view.CarParkFinanceControlView;
import parkeersimulator.view.CarParkLayoutControlView;
import parkeersimulator.view.CarParkSimulationControlView;
import parkeersimulator.view.MonthlyReportView;
import parkeersimulator.view.frame.MainFrame;
import parkeersimulator.view.frame.GarageDesignFrame;
import parkeersimulator.view.graph.CarCountGraphView;
import parkeersimulator.view.graph.QueueGraphView;
import parkeersimulator.view.graph.FinanceGraphView;

/**
 * The Entry point for launching the program.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 *
 */
public class Main {
	
	///Declaration of the main handler that initiates the models this program should use.
	private ModelHandler modelHandler;
	private TimeModel timeModel;
	private ParkingGarageModel parkingGarageModel;
	private FinanceModel financeModel;
	private LayoutModel layoutModel;
	
	///Declaration of the controllers this program uses.
	private ControllerHandler controllerHandler;
	private ParkingGarageController parkingGarageController;
	private FinanceController financeController;
	private TimeController timeController;
	private LayoutController layoutController;
	
	///Declaration of the views this program uses.
	private GarageDesignFrame carParkView;
	private JPanel[] tabbedViews_ControlView;
	private JPanel[] tabbedViews_GraphView;

	private MainFrame screen;
	
	/**
	 * Constructor of the class Main.
	 */
	public Main() {
		//Instantiation of this programs' models
		modelHandler = new ModelHandler();
		timeModel = new TimeModel(modelHandler);
		financeModel = new FinanceModel(modelHandler);
		parkingGarageModel = new ParkingGarageModel(modelHandler);
		layoutModel = new LayoutModel(modelHandler);
		
		///Instantiation of this programs' controllers.
		controllerHandler = new ControllerHandler();
		timeController = new TimeController(timeModel);
		financeController = new FinanceController(financeModel);
		parkingGarageController = new ParkingGarageController(parkingGarageModel);
		layoutController = new LayoutController(layoutModel);
		
		///Instantiation of this programs' views.
		carParkView = new GarageDesignFrame(parkingGarageModel, parkingGarageController);
		
		tabbedViews_GraphView = new JPanel[5];
		tabbedViews_GraphView[0] = new CarCountGraphView(parkingGarageModel, timeModel);
		tabbedViews_GraphView[0].setName("Car Count Graph");
		tabbedViews_GraphView[1] = new QueueGraphView(parkingGarageModel, timeModel);
		tabbedViews_GraphView[1].setName("Queue Graph");
		tabbedViews_GraphView[2] = new FinanceGraphView(financeModel, timeModel);
		tabbedViews_GraphView[2].setName("Revenue Graph");
		tabbedViews_GraphView[3] = new AdviceView(parkingGarageModel);
		tabbedViews_GraphView[3].setName("Advice Panel");
		tabbedViews_GraphView[4] = new MonthlyReportView(financeModel, timeModel);
		tabbedViews_GraphView[4].setName("Monthly Financial Report");
		
		tabbedViews_ControlView = new JPanel[4];
		tabbedViews_ControlView[0] = new CarParkSimulationControlView(timeModel, timeController);
		tabbedViews_ControlView[0].setName("Simulation");
		tabbedViews_ControlView[1] = new CarParkArrivalControlView(parkingGarageModel, parkingGarageController);
		tabbedViews_ControlView[1].setName("Arrival");
		tabbedViews_ControlView[2] = new CarParkFinanceControlView(financeModel, financeController);
		tabbedViews_ControlView[2].setName("Finance");
		tabbedViews_ControlView[3] = new CarParkLayoutControlView(layoutModel, layoutController);
		tabbedViews_ControlView[3].setName("Layout");
		
		///Layout and instantiation of the JFrame.
		screen = new MainFrame("Parking Garage Simulator", tabbedViews_ControlView, tabbedViews_GraphView, carParkView);
		
		///Makes this JFrame visible.
		screen.setVisible(true);
	}
	
	/**
	 * The Entry-point for this program to start running.
	 */
	public static void main(String[] args) {
		Main main = new Main();
	}

}