package parkeersimulator.model;

import parkeersimulator.handler.ModelHandler;

public class TimeModel extends AbstractModel{

	///Declaration of the time.
    private int year = 0;
    private int month = 0;
    private int week = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    
    //Tells us if it's the first day of the month
    private boolean isFirstDayOfMonth;
    
    //Keeps track of the current day in the month
    private int currentDayInMonth;
    
    //Declaration of the amount of days in each month
    private int daysInFeb = 28;
    private int[] dayAmountMonth = new int[] {31, daysInFeb, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
	public TimeModel(ModelHandler handler) {
		super(handler);
	}

	@Override
	public ModelType getModelType() {
		return ModelType.TIME;
	}

	@Override
	public void tick() {
		advanceTime();
	}
	
	/**
     * Starts the simulation
     */
	public void start() {
		getModelHandler().start();
	}
	
	/**
	 * Stops the currently running simulation
	 */
	public void stop() {
		getModelHandler().stop();
	}
	
	/**
	 * Sets the pause amount between ticks
	 */
	public void setTickPause(int amount) {
		getModelHandler().setTickPause(amount);
	}
	/**
	 * Gets the pause amount between ticks
	 */
	public int getTickPause() { return getModelHandler().getTickPause(); }
	
	/**
	 * Sets the pause amount between ticks
	 */
	public void tick(int amount) {
		getModelHandler().tick(amount);
	}
	
	/**
     * A method that is used for incrementing the current time.
     */
    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        
        while (minute > 59) { //Reset minutes and set hours
            minute -= minute;
            hour++;
        }
        while (hour > 23) { //Reset hours and set days
            hour -= hour;
            day++;
            isFirstDayOfMonth = false;
        }
        while (day > 6) { //Reset days and set weeks
            day -= day;
            week++;
        }
        
        currentDayInMonth = (week * 7) + day;
        while(currentDayInMonth > dayAmountMonth[month] - 1) { //Reset weeks and set months
        	currentDayInMonth = day;
        	week -= week;
        	month++;
        	isFirstDayOfMonth = true;
        }
        
        while(month > 11) { //Reset months and set years
        	month -= month;
        	year++;
        	
        	if((year + 1) % 4 == 0) //leap year
        		daysInFeb = 29;
        	else
        		daysInFeb = 28;
        }
    }
    
    
    /**
     * @return The current year.
     */
    public int getYear() { return year; }
    
    /**
     * @return The current month of a year.
     */
    public int getMonth() { return month; }
    
    /**
     * @return The current week of a month.
     */
    public int getWeek() { return week; }
    
    /**
     * @return The current day of the week.
     */
    public int getDay() { return day; }
    
    /**
     * @return The current hour of the day.
     */
    public int getHour() { return hour; }
    
    /**
     * @return The current minute of the hour.
     */
    public int getMinute() { return minute; }
    
    /**
     * @return The current time in the format: int[day][hour][minute].
     */
    public int[] getTime() { return new int[] { minute, hour, day }; }
    
    /**
     * @return The bool that tells the program if it is the first day of the month
     */
    public boolean getIsFirstDayOfMonth() { return isFirstDayOfMonth; }
    
}
