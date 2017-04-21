// using area to check for intersection: http://stackoverflow.com/questions/16837959/checking-intersection-between-two-path2ds

package edu.virginia.engine.display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.events.IEventListener;

/**
 * A very basic display object for a java based gaming engine
 * 
 * */
public class DisplayObject extends EventDispatcher {

	/* All DisplayObject have a unique id */
	private String id;

	/* The image that is displayed by this object */
	private BufferedImage displayImage;
	private boolean visible = true;
	private boolean vPresent = false;
	protected Point position = new Point(0,0);
	private Point pivotPoint = new Point(0,0);
	private double scaleX = 1.0;
	private double scaleY = 1.0;
	private int rotation = 0;
	private float alpha = 1.0f;
	private int inc = 20;
	private int vCounter = 0;
	private DisplayObjectContainer parent;
	private Point hitBoxPos = new Point(0, 0);
	private int hitBoxWidth = (int)(this.getUnscaledWidth()*this.getScaleX());
	private int hitBoxHeight = (int)((this.getUnscaledHeight())*this.getScaleY());
	Rectangle hitBox = new Rectangle((int)(this.getPosition().getX()*this.getScaleX()), (int)(this.getPosition().getY()*this.getScaleY()), (int)(this.getUnscaledWidth()*this.getScaleX()), (int)((this.getUnscaledHeight())*this.getScaleY()));
	private Path2D pathBox; // can actually rotate and stuff unlike rectangle ~wow~
	private Area areaBox;
	private boolean collidable = true;
	
	/**
	 * Constructors: can pass in the id OR the id and image's file path and
	 * position OR the id and a buffered image and position
	 */
	public DisplayObject(String id) {
		this.setId(id);
		hitBox.setBounds((int)((this.getPosition().getX() + this.getPivotPoint().getX())), (int)((this.getPosition().getY() + this.getPivotPoint().getY())), hitBoxWidth, hitBoxHeight);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
		pathBox = (Path2D) transform.createTransformedShape(hitBox);
		areaBox = new Area(hitBox);
	}

	public DisplayObject(String id, String fileName) {
		this.setId(id);
		this.setImage(fileName);
		Point p = new Point(0,0);
		this.setPivotPoint(p);
		this.setPosition(p);
		hitBox.setBounds((int)((this.getPosition().getX() + this.getPivotPoint().getX())), (int)((this.getPosition().getY() + this.getPivotPoint().getY())), hitBoxWidth, hitBoxHeight);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
		pathBox = (Path2D) transform.createTransformedShape(hitBox);
		areaBox = new Area(hitBox);

	}
	
	public DisplayObject(String id, Point position) {
		this.setId(id);
		this.setPosition(position);
		hitBox.setBounds((int)((this.getPosition().getX() + this.getPivotPoint().getX())), (int)((this.getPosition().getY() + this.getPivotPoint().getY())), hitBoxWidth, hitBoxHeight);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
		pathBox = (Path2D) transform.createTransformedShape(hitBox);
		areaBox = new Area(hitBox);

	}

	public DisplayObject(String id, String fileName, boolean visible, Point position) {
		this.setId(id);
		this.setImage(fileName);
		this.setVisible(visible);
		this.setPosition(position);
		this.pivotPoint.setLocation(0,0);
		this.setScaleX(1.0);
		this.setScaleY(1.0);
		this.setRotation(0);
		this.setAlpha(1.0f);
		hitBox.setBounds((int)((this.getPosition().getX() + this.getPivotPoint().getX())), (int)((this.getPosition().getY() + this.getPivotPoint().getY())), hitBoxWidth, hitBoxHeight);
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
		pathBox = (Path2D) transform.createTransformedShape(hitBox);
		areaBox = new Area(hitBox);

	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}


	/**
	 * Returns the unscaled width and height of this display object
	 * */
	public int getUnscaledWidth() {
		if(displayImage == null) return 0;
		return displayImage.getWidth();
	}

	public int getUnscaledHeight() {
		if(displayImage == null) return 0;
		return displayImage.getHeight();
	}

	public BufferedImage getDisplayImage() {
		return this.displayImage;
	}

	protected void setImage(String imageName) {
		if (imageName == null) {
			return;
		}
		displayImage = readImage(imageName);
		if(hitBoxHeight == 0) {
			hitBoxHeight = displayImage.getHeight();
		}
		if(hitBoxWidth == 0) {
			hitBoxWidth = displayImage.getWidth();
		}
		if (displayImage == null) {
			System.err.println("[DisplayObject.setImage] ERROR: " + imageName + " does not exist!");
		}
	}


	/**
	 * Helper function that simply reads an image from the given image name
	 * (looks in resources\\) and returns the bufferedimage for that filename
	 * */
	public BufferedImage readImage(String imageName) {
		BufferedImage image = null;
		try {
			String file = ("resources" + File.separator + imageName);
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		return image;
	}

	public void setImage(BufferedImage image) {
		if(image == null) return;
		displayImage = image;
	}


	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Point getPosition() {
		return position;
	}
	
/**	public void setHitBox(int x, int y, int width, int height) {
		this.hitBoxPos = new Point(x, y);
		this.hitBoxWidth = width;
		this.hitBoxHeight = height;
	}

	public void setHitBoxP(Point p, int width, int height) {
		this.hitBoxPos = p;
		this.hitBoxWidth = width;
		this.hitBoxHeight = height;
	}**/

	public void setPosition(Point position) {
//		double xDisp = position.getX() - this.getPosition().x;
//		double yDisp = position.getY() - this.getPosition().y;
//		
//		this.hitBox.x += xDisp;
//		this.hitBox.y += yDisp;
//		
//		if(yDisp > 0 || xDisp > 0) {
//			System.out.println(this.getId());
//			System.out.println("original pos: " + this.getPosition().x + ", " + this.getPosition().y + " new pos: " + position.x + ", " + position.y);
//		}
		
		/**AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
		pathBox = (Path2D) transform.createTransformedShape(hitBox);
		Area newArea = new Area(hitBox);
		areaBox.reset();
		areaBox.add(newArea);**/
		
		this.position = position;
	}
	
	public void setPosition(int i, int j) {
		Point p = new Point(i, j);
		
//		double xDisp = p.getX() - this.getPosition().x;
//		double yDisp = p.getY() - this.getPosition().y;
//		
//		if(yDisp > 0 || xDisp > 0) {
//			System.out.println(this.getId());
//			System.out.println("original pos: " + this.getPosition().x + ", " + this.getPosition().y + " new pos: " + i + ", " + j);
//		}
		
	/**	this.hitBox.x += xDisp;
		this.hitBox.y += yDisp;
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
		pathBox = (Path2D) transform.createTransformedShape(hitBox);
		Area newArea = new Area(hitBox);
		areaBox.reset();
		areaBox.add(newArea);**/
		
		this.position = p;
	}
	
	public void move(int x, int y) {
		this.position.x += x;
		this.position.y += y;
		
		this.hitBox.x += x;
		this.hitBox.y += y;
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
		pathBox = (Path2D) transform.createTransformedShape(hitBox);
		Area newArea = new Area(hitBox);
		areaBox.reset();
		areaBox.add(newArea);
	}
	
	public Point getPivotPoint() {
		return pivotPoint;
	}

	public void setPivotPoint(Point pivotPoint) {
		this.pivotPoint = pivotPoint;
	}
	
	public void setPivotPoint(int i, int j) {
		Point p = new Point(i, j);
		this.pivotPoint = p;
	}

	public double getScaleX() {
		return scaleX;
	}

	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}

	public double getScaleY() {
		return scaleY;
	}

	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float f) {
		this.alpha = f;
	}

	public void setDisplayImage(BufferedImage displayImage) {
		this.displayImage = displayImage;
	}

	public DisplayObjectContainer getParent() {
		return parent;
	}

	public void setParent(DisplayObjectContainer parent) {
		this.parent = parent;
	}

	public void setHitBox(int x, int y, int width, int height) {
		this.hitBoxPos = new Point(x, y);
		this.hitBoxWidth = width;
		this.hitBoxHeight = height;
	}

	public void setHitBoxP(Point p, int width, int height) {
		this.hitBoxPos = p;
		this.hitBoxWidth = width;
		this.hitBoxHeight = height;
	}
	
	public Area getHitBox() {
		return this.areaBox;
	}
	
	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public boolean collidesWith(DisplayObject other) {
		if(collidable && other.isCollidable()) {
			Area thisArea = new Area(this.areaBox);
			Area otherArea = new Area(other.getHitBox());
			thisArea.intersect(otherArea);
			return !thisArea.isEmpty();
		} else {
			return false;
		}
	}

	//absolute position - relative is relative to this
	public Point absPos (Point relative) {
		if (this.getParent() == null) {
			return relative;
		}
		else {
			Point ret = new Point();
			ret.setLocation(relative.getX() + this.position.x, relative.getY() + this.position.y);
			return this.getParent().absPos(ret);
		}
	}

	//relative - want relative to this
	public Point relPos (Point absolute) {
	    Point res = new Point();
	    res.setLocation(absolute.getX() - this.absPos(new Point(0,0)).getX(), absolute.getY() - this.absPos(new Point(0,0)).getY() );
		return res;
	}

	/**
	 * Invoked on every frame before drawing. Used to update this display
	 * objects state before the draw occurs. Should be overridden if necessary
	 * to update objects appropriately.
	 * */
	protected void update(ArrayList<Integer> pressedKeys) {
		// update hitbox
		if(hitBox != null && pathBox != null) {
			/*
			double scaleX = this.getScaleX();
			double scaleY = this.getScaleY();
			double xDisplacement = 0;
			if(this.getScaleX() < 0) {
				scaleX *= -1;
				xDisplacement = this.getUnscaledWidth()*this.getScaleX();
			}
			if(this.getScaleY() < 0) {
				scaleY *= -1;
			}
			*/
			double parentX = 0;
			if(parent != null) {
				parentX = parent.getPosition().getX();
			}
			double parentY = 0;
			if(parent != null) {
				parentY = parent.getPosition().getY();
			}
			
			if(parent != null && this.parent.getScaleX() == -1) {
				hitBox.setBounds((int) (((-this.getPosition().getX() + parentX - this.getUnscaledWidth() + hitBoxPos.getX()))), (int)((this.getPosition().getY() - this.getPivotPoint().getY() + parentY + hitBoxPos.getY())), hitBoxWidth, hitBoxHeight);
			} else {
			
				hitBox.setBounds((int) (((this.getPosition().getX() - this.getPivotPoint().getX() + parentX + hitBoxPos.getX()))), (int)((this.getPosition().getY() - this.getPivotPoint().getY() + parentY + hitBoxPos.getY())), hitBoxWidth, hitBoxHeight);
			}
			AffineTransform transform = new AffineTransform();
			transform.rotate(Math.toRadians(this.getRotation()), (int)(this.getPosition().getX()), (int)(this.getPosition().getY()));
			pathBox = (Path2D) transform.createTransformedShape(hitBox);
			Area newArea = new Area(hitBox);
			areaBox.reset();
			areaBox.add(newArea);
		}
	}

	/**
	 * Draws this image. This should be overloaded if a display object should
	 * draw to the screen differently. This method is automatically invoked on
	 * every frame.
	 * */
	public void draw(Graphics g) {
		
		if (displayImage != null) {
			/*
			 * Get the graphics and apply this objects transformations
			 * (rotation, etc.)
			 */
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform old = applyTransformations(g2d);
			
			/* Actually draw the image, perform the pivot point translation here */
			g2d.drawImage(displayImage, -this.pivotPoint.x, -this.pivotPoint.y,
					(getUnscaledWidth()),
					(getUnscaledHeight()), null);
			
			/*
			 * undo the transformations so this doesn't affect other display
			 * objects
			 */
			reverseTransformations(g2d, old);
		}

	}

	/**
	 * Applies transformations for this display object to the given graphics
	 * object
	 * */
	protected AffineTransform applyTransformations(Graphics2D g2d) {
		if(visible) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		} else {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
		}
		AffineTransform old = g2d.getTransform();
		g2d.translate(this.position.getX(), this.position.getY());
		g2d.scale(scaleX, scaleY);
		g2d.rotate(Math.toRadians(this.rotation));
		return old;
	}

	/**
	 * Reverses transformations for this display object to the given graphics
	 * object
	 * */
	protected void reverseTransformations(Graphics2D g2d, AffineTransform old) {
		g2d.setTransform(old);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

}