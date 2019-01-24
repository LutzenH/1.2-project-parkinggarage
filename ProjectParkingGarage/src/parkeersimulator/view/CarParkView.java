package parkeersimulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;

import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.location.Location;
import parkeersimulator.model.location.Place;

/**
 * class of the view of the simulation.
 */
public class CarParkView extends AbstractView {
	
	///get screen size
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = screenSize.width;
	int height = screenSize.height;
	
	///The x and y offset used for positioning the entire parking garage on the window
    private static int X_OFFSET = 50; 
    private static int Y_OFFSET = 10;
    
    ///The x and y offset used for spacing between parking places
    private static int X_OFFSET_PLACE = 20;
    private static int Y_OFFSET_PLACE = 10;
    
  ///The x and y values used for sizing parking places
    private static int X_WIDTH_PLACE = 20;
    private static int Y_WIDTH_PLACE = 10;
    
    ///The t offset used for spacing between each level of the parking garage
    private static int Y_OFFSET_FLOORS = 240;
    private static int Y_OFFSET_FLOORS_DEFAULT = 20;
    
    ///The x offset used for spacing between each column of parking placed
    private static int X_OFFSET_COLUMN = 55;
    
    ///The x factor used for calculating which row each parking space goes in
    private static float X_ROWPOS_FACTOR = 0.5f;
    
  ///The factor used sizing the entire parking garage
    private static int SIZE_FACTOR = 1;
    
    private Dimension size;
    private Image carParkImage;    
    
    /**
     * Constructor for objects of class CarPark
     */
    public CarParkView(ParkingGarageModel model) {
    	super(model);
    	    	
        size = new Dimension(0, 0);
        
		this.setBorder(BorderFactory.createLineBorder(Color.black));

    }

    /**
     * Overridden. Tell the GUI manager how big we would like to be.
     */
    public Dimension getPreferredSize() {
       return new Dimension(800, 500);
    }

    /**
     * Overriden. The car park view component needs to be redisplayed. Copy the
     * internal image to screen.
     */
    public void paintComponent(Graphics g) {
        if (carParkImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(carParkImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
        }
    }

    /**
     * Repaints this view.
     */
    @Override
    public void updateView() {
        // Create a new car park image if the size has changed.
        if (!size.equals(getSize())) {
            size = getSize();
            carParkImage = createImage(size.width + 1, size.height + 2);
        }
        Graphics graphics = carParkImage.getGraphics();
        for(int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for(int row = 0; row < model.getNumberOfRows(); row++) {
                for(int places = 0; places < model.getNumberOfPlaces(); places++) {
                    Location location = new Location(floor, row, places);
                    
                    Place place = model.getPlaces()[floor][row][places];
                    Car car = place.getCar();

                    Color color = car == null ? Color.white : car.getColor();
                    drawPlace(graphics, location, color, place.getReserved());
                }
            }
        }
        repaint();
    }

    /**
     * Paint a place on this car park view in a given color.
     */
    private void drawPlace(Graphics graphics, Location location, Color color, boolean isReserved) {
        graphics.setColor(color);
        graphics.fillRect(
                ((int)Math.floor(location.getRow() * X_ROWPOS_FACTOR) * X_OFFSET_COLUMN + (location.getRow() % 2) * X_OFFSET_PLACE + X_OFFSET) + location.getFloor() * (Y_OFFSET_FLOORS + Y_OFFSET_FLOORS_DEFAULT) * SIZE_FACTOR,
                (location.getPlace() * Y_OFFSET_PLACE + Y_OFFSET) * SIZE_FACTOR,
                (X_WIDTH_PLACE - 1) * SIZE_FACTOR,
                (Y_WIDTH_PLACE - 1) * SIZE_FACTOR); // TODO use dynamic size or constants
        if(color == Color.white &&  !isReserved) {
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.fillRect(
            		((int)Math.floor(location.getRow() * X_ROWPOS_FACTOR) * X_OFFSET_COLUMN + (location.getRow() % 2) * X_OFFSET_PLACE + X_OFFSET + 2) * SIZE_FACTOR,
                    (location.getPlace() * Y_OFFSET_PLACE + location.getFloor() * (Y_OFFSET_FLOORS + Y_OFFSET_FLOORS_DEFAULT) + Y_OFFSET + 2) * SIZE_FACTOR,
                    (X_WIDTH_PLACE - 5) * SIZE_FACTOR,
                    (Y_WIDTH_PLACE - 5) * SIZE_FACTOR);
        }
    }
}
