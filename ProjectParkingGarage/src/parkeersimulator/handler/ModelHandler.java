package parkeersimulator.handler;

import java.util.ArrayList;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.AbstractModel.ModelType;

public class ModelHandler implements Runnable{
	
	///boolean that is used for when threads need to stop running.
	private boolean run;
	
	///The amount of time the thread should wait before executing the next tick().
    private int tickPause = 5;
	
	private ArrayList<AbstractModel> modelList;

	
	public ModelHandler() {
		modelList = new ArrayList<>();
	}
	
	
	/**
     * Starts the simulation on a different thread.
     */
	public void start() {
		if(!run)
			new Thread(this).start();
	}
	
	/**
	 * Stops the currently running simulation on a different thread.
	 */
	public void stop() {
		run = false;
	}
	
	/**
	 * Simulation that runs on a different thread when started.
	 */
	@Override
	public void run() {
		run=true;
		while(run) {
            for(AbstractModel model : modelList) {
        		model.tick();
    		}
            
			try {
				Thread.sleep(tickPause);
			} 
			catch (Exception e) {
			}
		}
	}

	/**
     * Runs a certain amount of simulation iterations
     * 
     * @param amount The amount of ticks that should run.
     */
    public void tick(int amount) {
    	for(int i = 0; i < amount; i++)
    	{
    		for(AbstractModel model : modelList) {
        		model.tick();
    		}
    	}
    }

	public void addModel(AbstractModel model) {
		modelList.add(model);
	}
	

    /**
     * @return The current pause between ticks.
     */
    public int getTickPause() { return tickPause; }
    /**
     * Sets the pause between ticks.
     */
    public void setTickPause(int amount) { tickPause = amount; }
    
    public AbstractModel getModel(ModelType type) {
    	for(AbstractModel model : modelList) {
    		if(model.getModelType() == type)
    			return model;
    	}
    	
    	return null;
    }
}
