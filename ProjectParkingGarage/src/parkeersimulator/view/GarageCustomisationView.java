package parkeersimulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import parkeersimulator.controller.AbstractController.ActionType;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.prop.Prop;

public class GarageCustomisationView extends AbstractControllableView implements MouseListener, ComponentListener {
	
	///get screen size
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = screenSize.width;
	int height = screenSize.height;
	
    private Dimension size;
    private Image carParkImage;
    
    private Rectangle[] rectangles;
    
    ///Declaration that checks is used for: if the background has been drawn already
    private boolean hasDrawnBackground;

    /**
     * The constructor of GarageCustomisationView
     * @param model the ParkingGarageModel this view should be based on.
     * @param controller the controller where inputs should be send to.
     */
	public GarageCustomisationView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);
		
		size = getSize();

		addMouseListener(this);
		addComponentListener(this);
		
		rectangles = new Rectangle[model.getNumberOfFloors() * model.getNumberOfRows()];
		
		this.hasDrawnBackground = false;
	}

    /**
     * Overridden. Tell the GUI manager how big we would like to be.
     */
    public Dimension getPreferredSize() {
       return new Dimension(740, 450);
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
    	ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
    	
    	Toolkit.getDefaultToolkit().sync();
    	
        // Create a new car park image if the size has changed.
        if (!size.equals(getSize())) {
        	hasDrawnBackground = false;
            size = getSize();
            carParkImage = createImage(size.width + 1, size.height + 2);
        }
        Graphics graphics = carParkImage.getGraphics();

        drawCarPark(graphics, parkingGarageModel);
        
        repaint();
    }
     /**
      * Draw all the props like Entrance, Exit and TicketMachine
      * @param graphics the graphics the props should be drawn on.
      * @param xOffset the x offset where the props should be drawn.
      * @param yOffset the y offset where the props should be drawn.
      */
    private void drawProps(Graphics graphics, int xOffset, int yOffset, int carParkWidth, int carParkHeight) {  	   	
    	ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
    	
    	Prop[] props = parkingGarageModel.getProps();
    	
    	int distanceBetweenFloors_X = CarParkView.X_OFFSET_FLOORS + CarParkView.X_OFFSET_FLOORS_DEFAULT;
    	int rowLength_Y = carParkHeight + 27;
    	
    	int propsPerFloor = parkingGarageModel.getNumberOfRows()/2;
    	
    	int count = 0;
    	
    	rectangles = new Rectangle[parkingGarageModel.getNumberOfFloors() * parkingGarageModel.getNumberOfRows()];
    	
    	for(int floor = 0; floor < parkingGarageModel.getNumberOfFloors(); floor++) {
        	for(int i = 0; i < propsPerFloor; i++) {
        		Color color = Color.white;
        		
    			color = new Color(220, 220, 220);
        		
        		drawProp(graphics, xOffset, yOffset, props, count, floor, i, distanceBetweenFloors_X, rowLength_Y);
        		rectangles[count] = new Rectangle(xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN_FINAL) + (floor * distanceBetweenFloors_X), yOffset + 5, 29, 29);
        		drawRectangle(graphics, rectangles[count], color);
        		count++;
        		
        		drawProp(graphics, xOffset, yOffset, props, count, floor, i, distanceBetweenFloors_X, rowLength_Y);
        		rectangles[count] = new Rectangle(xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN_FINAL) + (floor * distanceBetweenFloors_X), yOffset + 10 + (rowLength_Y + 7), 29, 29);
        		drawRectangle(graphics, rectangles[count], color);
        		count++;
        	}
    	}
    }
    
    /**
     * Draws the grassy background and parking garage background.
     * @param graphics the graphics this background should  be drawn on
     * @param carpark_topleft_x the topleft x position of the carpark image
     * @param carpark_topleft_y the topleft y position of the carpark image
     * @param carpark_width the total width of the carpark in pixels
     * @param carpark_height the total height of the carpark in pixels
     */
    private void drawBackground(Graphics graphics, int carpark_topleft_x, int carpark_topleft_y, int carpark_width, int carpark_height) {
    	if(!hasDrawnBackground) {
        	graphics.setColor(Color.WHITE);
        	graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
        	
        	Image img_background = createImage("resources/img_background.png");
        	graphics.drawImage(img_background, 0, 0, this.getWidth(), this.getHeight(), this);
        	
    		int[] shadow_x = {	carpark_topleft_x - 35,
        			carpark_topleft_x + carpark_width + 35,
        			carpark_topleft_x + carpark_width + 35,
        			carpark_topleft_x + carpark_width,
        			carpark_topleft_x - 70,
        			carpark_topleft_x - 70
        		 };

        	int[] shadow_y = {
        			carpark_topleft_y - 55,
        			carpark_topleft_y - 55,
        			carpark_topleft_y + carpark_height + 55,
        			carpark_topleft_y + carpark_height + 80,
        			carpark_topleft_y + carpark_height + 80,
        			carpark_topleft_y - 30
        		 };

        	graphics.setColor(new Color(5,5,5, 50));
        	graphics.fillPolygon(shadow_x, shadow_y, 6);
            	
        	graphics.setColor(new Color(153,153,153));
        	graphics.fillRect(carpark_topleft_x-35, carpark_topleft_y-55, carpark_width+70, carpark_height+110);
        	graphics.setColor(new Color(128,128,128));
        	graphics.fillRect(carpark_topleft_x-30, carpark_topleft_y-50, carpark_width+60, carpark_height+100);
    	
        	drawProps(graphics, carpark_topleft_x, carpark_topleft_y - 40, carpark_width, carpark_height);
        	
        	hasDrawnBackground = true;
    	}
    }
    
    /**
     * Draws an individual prop at a certain location.
     * @param graphics the graphics this props should be drawn on
      * @param xOffset the x offset where the prop should be drawn.
      * @param yOffset the y offset where the prop should be drawn.
     * @param props an array of the props that should be drawn.
     * @param count the current count of the prop that should be drawn, (if its uneven it will be drawn on the bottom of the garage)
     * @param floor the floor where the prop should be drawn on.
     * @param i the current counted position
     * @param distanceBetweenFloors_X the distance in pixels between two floors.
     * @param rowLength_Y the length of a full row.
     */
    private void drawProp(Graphics graphics, int xOffset, int yOffset, Prop[] props, int count, int floor, int i, int distanceBetweenFloors_X, int rowLength_Y) {
		if(props[count] != null) {
			int xPos = xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN_FINAL) + (floor * distanceBetweenFloors_X);
			int yPos = (count % 2 == 0) ? (yOffset + 5) : yOffset + 10 + (rowLength_Y + 7);
			
    		switch(props[count].getType()) {
				case PROP_ENTRANCE:
			    	
					graphics.setColor(new Color(200, 200, 200));
					
					if(count % 2 == 0) {
						Image entrance = createImage("resources/entrance.png");
						graphics.fillRect(xPos - 4, 0, entrance.getWidth(this) + 8, yPos-20);
						graphics.drawImage(entrance, xPos, yPos - 30, entrance.getWidth(this), entrance.getHeight(this), this);
					}
					else {
						Image entrance = createImage("resources/entrance_bottom.png");
			    		graphics.fillRect(xPos - 4, yPos+49, entrance.getWidth(this) + 8, this.getHeight());
			    		graphics.drawImage(entrance, xPos, yPos + 30, entrance.getWidth(this), entrance.getHeight(this), this);
					}
			    	
					break;
				case PROP_EXIT:					
					graphics.setColor(new Color(200, 200, 200));
					
					if(count % 2 == 0) {
						Image exit = createImage("resources/exit.png");
						graphics.fillRect(xPos - 4, 0, exit.getWidth(this) + 8, yPos-20);
						graphics.drawImage(exit, xPos, yPos - 30, exit.getWidth(this), exit.getHeight(this), this);
					}
					else {
						Image exit = createImage("resources/exit_bottom.png");
			    		graphics.fillRect(xPos - 4, yPos+49, exit.getWidth(this) + 8, this.getHeight());
			    		graphics.drawImage(exit, xPos, yPos + 30, exit.getWidth(this), exit.getHeight(this), this);
					}
					
					break;
				case PROP_TICKETMACHINE:
					Image ticketMachine;
					
					if(count % 2 == 0) {
						ticketMachine = createImage("resources/ticketmachine_top.png");
						yPos -= 15;
					}
					else {
						ticketMachine = createImage("resources/ticketmachine_bottom.png");
						yPos += 18;
					}
					
			    	graphics.drawImage(ticketMachine,
			    			xPos,
			    			yPos,
			    			ticketMachine.getWidth(this),
			    			ticketMachine.getHeight(this),
			    			this);
					break;
				default:
					break;
    		}
		}
    }

    /**
     * Draws the carpark
     * @param graphics the graphics the carpark should be drawn on
     * @param parkingGarageModel the model which the carpark should be based on
     */
    private void drawCarPark(Graphics graphics, ParkingGarageModel parkingGarageModel) {    	
    	int width = this.getWidth();
    	int height = this.getHeight();
    	
    	int carpark_width = CarParkView.getGarageSize(parkingGarageModel)[0];
    	int carpark_height = CarParkView.getGarageSize(parkingGarageModel)[1];
    	
    	
    	int carpark_topleft_x = (width - carpark_width)/2;
    	int carpark_topleft_y = (height - carpark_height)/2;
    	
    	drawBackground(graphics, carpark_topleft_x, carpark_topleft_y, carpark_width, carpark_height);
        CarParkView.drawCarPark(graphics, carpark_topleft_x, carpark_topleft_y, parkingGarageModel);
    }
    
    /**
     * Creates an image and gives an exception if the path doesn't exist
     * @param path the path of the image.
     * @return returns an image.
     */
    private Image createImage(String path) {
    	Image img = null;
    	
        try {
        	img = ImageIO.read(new File(path));
         } catch (IOException e) {
            e.printStackTrace();
         }

        return img;
    }
    
    /**
     * Draws a rectangle on a java.awt.Graphics with a certain color
     * @param graphics the Graphics device the rectangle should be drawn on.
     * @param rectangle the rectangle that should be drawn
     * @param color the color of the rectangle
     */
    private void drawRectangle(Graphics graphics, Rectangle rectangle, Color color) {    	
		graphics.setColor(color);
		graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
    /**
     * An event that triggers when the mouse has been clicked. This will put a prop at the clicked index.
     */
    @Override
    public void mouseClicked(MouseEvent e){
    	hasDrawnBackground = false;
		updateView();
    	for(int i = 0; i < rectangles.length; i++) {
    		if(rectangles[i] != null) {
        		Integer index = i;
        		
        		if(rectangles[i].contains(e.getPoint())) {
        			controller.performAction(ActionType.PARKINGGARAGE_CLICK_PROP, new HashMap<String, Object>() {{ put("index", index); }});
        			hasDrawnBackground = false;
        			updateView();
    			}
    		}
    	}
    }
    
    /**
     * An event that triggers when the component has been resized. This will update the view.
     */
	@Override
	public void componentResized(ComponentEvent e) {
		updateView();
	}
	
	/**
	 * @return true if the background has already been drawn
	 */
	public boolean isHasDrawnBackground() {
		return hasDrawnBackground;
	}

	/**
	 * Set the background to drawn
	 * @param hasDrawnBackground true or false depending if the background should be redrawn.
	 */
	public void setHasDrawnBackground(boolean hasDrawnBackground) {
		this.hasDrawnBackground = hasDrawnBackground;
	}
	
    //Unused Listeners
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
}
