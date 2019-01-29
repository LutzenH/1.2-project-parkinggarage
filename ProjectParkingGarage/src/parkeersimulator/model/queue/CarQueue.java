package parkeersimulator.model.queue;
import java.util.LinkedList;
import java.util.Queue;

import parkeersimulator.model.car.Car;

/**
 * Class used for queues in the parking garage simulation.
 */
public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    /**
     * Add a car to the queue.
     * @param car the car that should be added to the queue.
     * @return true if a car could be added.
     */
    public boolean addCar(Car car) {
        return queue.add(car);
    }

    /**
     * poll a car from the queue.
     * @return the removed car from the queue.
     */
    public Car removeCar() {
        return queue.poll();
    }
    
    /**
     * clear all the cars from the queue.
     */
    public void clear() {
        queue.clear();
    }

    /**
     * @return The size of the queue.
     */
    public int carsInQueue() {
    	return queue.size();
    }
}
