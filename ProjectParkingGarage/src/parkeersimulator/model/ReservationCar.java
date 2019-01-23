package parkeersimulator.model;
import java.awt.Color;
import java.util.Random;
import parkeersimulator.model.car.Car;

public class ReservationCar extends Car{
	
	private static final Color COLOR=Color.YELLOW;
	
	public ReservationCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
    }
    
    public Color getColor(){
    	return COLOR;
    }
}
