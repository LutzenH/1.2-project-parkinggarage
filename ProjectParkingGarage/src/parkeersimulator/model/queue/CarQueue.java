package parkeersimulator.model.queue;
import java.util.LinkedList;
import java.util.Queue;

import parkeersimulator.model.car.Car;

public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    public boolean addCar(Car car) {
        return queue.add(car);
    }

    public Car removeCar() {
        return queue.poll();
    }

    public int carsInQueue() {
    	return queue.size();
    }
}
