package parkeersimulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.controller.ParkingGarageController.ActionType;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.prop.Prop;

public class GarageCustomisationView extends AbstractControllableView implements MouseListener{

	///get screen size
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = screenSize.width;
	int height = screenSize.height;
	
    private Dimension size;
    private Image carParkImage;
    
    private Rectangle[] rectangles;
	
	public GarageCustomisationView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);
		
		size = new Dimension(0, 0);

		addMouseListener(this);
		
		rectangles = new Rectangle[model.getNumberOfFloors() * model.getNumberOfRows()];
	}

    /**
     * Overridden. Tell the GUI manager how big we would like to be.
     */
    public Dimension getPreferredSize() {
       return new Dimension(732, 395);
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
        
        drawProps(graphics, 0, 0);
        
        CarParkView.drawCarPark(graphics, 0, 40, model);
        
        repaint();
    }
    
    private void drawProps(Graphics graphics, int xOffset, int yOffset) {  	   	
    	Prop[] props = model.getProps();
    	
    	int propsPerFloor = model.getNumberOfRows()/2;
    	
    	int count = 0;
    	
    	for(int floor = 0; floor < model.getNumberOfFloors(); floor++) {
        	for(int i = 0; i < propsPerFloor; i++) {
            	int distanceBetweenRows_X = 55;
            	int distanceBetweenFloors_X = 260;
            	int rowLength_Y = 328;
        		
        		Color color = new Color(220, 220, 220);
        		
        		color = props[count] == null ? color : props[count].getColor();
        		graphics.setColor(color);
        		rectangles[count] = new Rectangle(xOffset + 5 + (i * distanceBetweenRows_X) + (floor * distanceBetweenFloors_X), yOffset + 5, 29, 29);
        		drawRectangle(graphics, rectangles[count], color);
        		count++;
        		
        		color = new Color(220, 220, 220);
        		
        		color = props[count] == null ? color : props[count].getColor();
        		rectangles[count] = new Rectangle(xOffset + 5 + (i * distanceBetweenRows_X) + (floor * distanceBetweenFloors_X), yOffset + 10 + (rowLength_Y + 7), 29, 29);
        		drawRectangle(graphics, rectangles[count], color);
        		count++;
        		
        	}
    	}
    }
    
    /**
     * Draws a rectangle on a java.awt.Graphics with a certain color
     * @param graphics the Graphics device the rectangle should be drawn on.
     * @param rectangle the rectangle that should be drawn
     * @param color the color of the rectangle
     */
    private void drawRectangle(Graphics graphics, Rectangle rectangle, Color color) {    	
		graphics.setColor(color);
		graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
    @Override
    public void mouseClicked(MouseEvent e){   	
    	for(int i = 0; i < rectangles.length; i++) {
    		Integer index = i;
    		
    		if(rectangles[i].contains(e.getPoint())) {
    			controller.performAction(ActionType.EVENT_CLICK_PROP, new HashMap<String, Object>() {{ put("index", index); }});
    			updateView();
    		}
    	}
    }

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
