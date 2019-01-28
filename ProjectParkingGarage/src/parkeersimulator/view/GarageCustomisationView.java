package parkeersimulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

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
            size = getSize();
            carParkImage = createImage(size.width + 1, size.height + 2);
        }
        Graphics graphics = carParkImage.getGraphics();

        drawBackground(graphics, parkingGarageModel.getDrawCheap());
        
        drawCarPark(graphics, parkingGarageModel.getDrawCheap());
        
        repaint();
    }
    
    private void drawProps(Graphics graphics, int xOffset, int yOffset, boolean drawCheap) {  	   	
    	ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
    	
    	Prop[] props = parkingGarageModel.getProps();
    	
    	int distanceBetweenFloors_X = CarParkView.Y_OFFSET_FLOORS + CarParkView.Y_OFFSET_FLOORS_DEFAULT;
    	int rowLength_Y = 328;
    	
    	int propsPerFloor = parkingGarageModel.getNumberOfRows()/2;
    	
    	int count = 0;
    	
    	for(int floor = 0; floor < parkingGarageModel.getNumberOfFloors(); floor++) {
        	for(int i = 0; i < propsPerFloor; i++) {
        		
        		Color color = Color.white;
        		
        		if(!drawCheap)
        			color = new Color(220, 220, 220);
        		
        		if(!drawCheap) {
            		drawProp(graphics, xOffset, yOffset, props, count, floor, i, distanceBetweenFloors_X, rowLength_Y);
            		rectangles[count] = new Rectangle(xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN) + (floor * distanceBetweenFloors_X), yOffset + 5, 29, 29);
            		drawRectangle(graphics, rectangles[count], color);
            		count++;
            		
            		drawProp(graphics, xOffset, yOffset, props, count, floor, i, distanceBetweenFloors_X, rowLength_Y);
            		rectangles[count] = new Rectangle(xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN) + (floor * distanceBetweenFloors_X), yOffset + 10 + (rowLength_Y + 7), 29, 29);
            		drawRectangle(graphics, rectangles[count], color);
            		count++;
        		}
        		else {
        			color = props[count] == null ? new Color(100, 100, 100) : props[count].getColor();
        			graphics.setColor(color);
        			rectangles[count] = new Rectangle(xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN) + (floor * distanceBetweenFloors_X), yOffset + 5, 29, 29);
        			graphics.fillRect(rectangles[count].x, rectangles[count].y, rectangles[count].width, rectangles[count].height);
            		count++;
            		
            		color = props[count] == null ? new Color(100, 100, 100) : props[count].getColor();
        			graphics.setColor(color);
            		rectangles[count] = new Rectangle(xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN) + (floor * distanceBetweenFloors_X), yOffset + 10 + (rowLength_Y + 7), 29, 29);
        			graphics.fillRect(rectangles[count].x, rectangles[count].y, rectangles[count].width, rectangles[count].height);
            		count++;
        		}
        		
        	}
    	}
    }
    
    private void drawBackground(Graphics graphics, boolean drawCheap) {
    	graphics.setColor(Color.WHITE);
    	
    	graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
    	
    	if(!drawCheap) {
        	Image img_background = createImage("resources/img_background.png");
        	graphics.drawImage(img_background, 0, 0, this.getWidth(), this.getHeight(), this);
    	}
    }
    
    private void drawProp(Graphics graphics, int xOffset, int yOffset, Prop[] props, int count, int floor, int i, int distanceBetweenFloors_X, int rowLength_Y) {
		if(props[count] != null) {
			int xPos = xOffset + 5 + (i * CarParkView.X_OFFSET_COLUMN) + (floor * distanceBetweenFloors_X);
			int yPos = (count % 2 == 0) ? (yOffset + 5) : yOffset + 10 + (rowLength_Y + 7);
			
    		switch(props[count].getType()) {
				case PROP_ENTRANCE:
					Image entrance = createImage("resources/entrance.png");
			    	
					graphics.setColor(new Color(200, 200, 200));
					
					if(count % 2 == 0) {
						graphics.fillRect(xPos - 4, 0, entrance.getWidth(this) + 8, yPos-20);
						graphics.drawImage(entrance, xPos, yPos - 30, entrance.getWidth(this), entrance.getHeight(this), this);
					}
					else {
			    		graphics.fillRect(xPos - 4, yPos+49, entrance.getWidth(this) + 8, this.getHeight());
			    		graphics.drawImage(entrance, xPos, yPos + 30, entrance.getWidth(this), entrance.getHeight(this), this);
					}
			    	
					break;
				case PROP_EXIT:
					Image exit = createImage("resources/exit.png");
					
					graphics.setColor(new Color(200, 200, 200));
					
					if(count % 2 == 0) {
						graphics.fillRect(xPos - 4, 0, exit.getWidth(this) + 8, yPos-20);
						graphics.drawImage(exit, xPos, yPos - 30, exit.getWidth(this), exit.getHeight(this), this);
					}
					else {
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
    
    private void drawCarPark(Graphics graphics, boolean drawCheap) {
    	ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
    	
    	int width = this.getWidth();
    	int height = this.getHeight();
    	
    	//TODO Calculate these based on real size
    	int carpark_width = 724;
    	int carpark_height = 299;
    	
    	int carpark_topleft_x = (width - carpark_width)/2;
    	int carpark_topleft_y = (height - carpark_height)/2;
    	
    	if(!drawCheap) {
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
    	}
    	
        drawProps(graphics, carpark_topleft_x, carpark_topleft_y - 40, parkingGarageModel.getDrawCheap());
    	
        CarParkView.drawCarPark(graphics,
				        		carpark_topleft_x,
				        		carpark_topleft_y,
				        		parkingGarageModel);
    }
    
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
    
    @Override
    public void mouseClicked(MouseEvent e){
		updateView();
    	for(int i = 0; i < rectangles.length; i++) {
    		Integer index = i;
    		
    		if(rectangles[i].contains(e.getPoint())) {
    			controller.performAction(ActionType.EVENT_CLICK_PROP, new HashMap<String, Object>() {{ put("index", index); }});
    			model.notifyViews();
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
