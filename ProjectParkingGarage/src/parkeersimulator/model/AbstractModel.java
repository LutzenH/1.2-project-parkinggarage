package parkeersimulator.model;

import java.util.ArrayList;
import java.util.List;

import parkeersimulator.handler.ModelHandler;
import parkeersimulator.view.AbstractView;

/**
 * Abstract class of the model.
 */
public abstract class AbstractModel {
	private ModelHandler modelHandler;
	
	///Declaration of the list of views that should be updated by the model.
	private List<AbstractView> views;
	
	public enum ModelType { PARKINGGARAGE, FINANCE, TIME, LAYOUT }
	public abstract ModelType getModelType();
	
	/**
	 * Constructor of AbstractModel
	 */
	public AbstractModel(ModelHandler modelHandler) {
		///Instantiation of the list of views.
		views=new ArrayList<AbstractView>();
		
		this.modelHandler = modelHandler;
		modelHandler.addModel(this);
	}
	
	/**
	 * Adds a new view for the model to update.
	 * @param view A view that the model should update when updateView() is being used.
	 */
	public void addView(AbstractView view) {
		views.add(view);
	}
	
	/**
	 * Notifies all views to update.
	 */
	public void notifyViews() {
		for(AbstractView v: views) v.updateView();
	}
	
	/**
	 * Runs the simulation one iteration
	 */
	public abstract void tick();

	public ModelHandler getModelHandler() {
		return modelHandler;
	}
}