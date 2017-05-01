package edu.virginia.engine.display;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import edu.virginia.engine.events.*;

import javax.imageio.ImageIO;

public class AnimatedSprite extends Sprite{
	
	private HashMap<String, ArrayList<BufferedImage>> spriteFrames;
	private int currentFrame = 0;
	private boolean playing = false;
	private String animate;
	private int speed;
	private int timer;
	
	public AnimatedSprite(String id) {
		super(id);
		spriteFrames = new HashMap<String, ArrayList<BufferedImage>>();
		this.setPlaying(false);
		this.setSpeed(7);
		this.setTimer(0);
	}
	
	public AnimatedSprite(String id, String key, String imageFileName) {
		super(id);
		spriteFrames = new HashMap<String, ArrayList<BufferedImage>>();
		this.addImage(key, imageFileName, 1, 1);
		this.setImage(imageFileName);
		this.setCurrentFrame(0);
		this.setPlaying(false);
		this.setSpeed(7);
		this.setTimer(0);
		this.animate = key;
	}
	
	private void setPlaying(boolean b) {
		// TODO Auto-generated method stub
		this.playing = b;
	}

	/* how to extract part of an image: http://stackoverflow.com/questions/621835/how-to-extract-part-of-this-image-in-java */
	public AnimatedSprite(String id, String key, String imageFileName, int rows, int col) {
		super(id);
		spriteFrames = new HashMap<String, ArrayList<BufferedImage>>();
		this.addImage(key, imageFileName, rows, col);
		this.setImage(spriteFrames.get(key).get(0));
		this.setCurrentFrame(0);
		this.setPlaying(false);
		this.setSpeed(7);
		this.animate = key;
	}
	
	public void addImage(String key, String imageName, int rows, int col) {
		ArrayList list = spriteFrames.get(key);
		if(list == null) {
			list = new ArrayList<BufferedImage>();
		}
		list.clear(); //if list already exists, clear list to put in new images
		
		BufferedImage img = null;
		try {
			String file = ("resources" + File.separator + imageName);
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		
		int height = img.getHeight()/rows;
		int width = img.getWidth()/col;
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < col; j++) {
				list.add((i * col) + j, img.getSubimage(j * width, i * height, width, height));
			}
		}
		spriteFrames.put(key, list);
		createFlipped(key);
	}

	public void addImageWithoutSheet(String key, String imageName) {
		ArrayList list = spriteFrames.get(key);
		if(list == null) {
			list = new ArrayList<BufferedImage>();
		}
//		list.clear(); //if list already exists, clear list to put in new images

		BufferedImage img = null;
		try {
			String file = ("resources" + File.separator + imageName);
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:readImage] Could not read image " + imageName);
			e.printStackTrace();
		}
		list.add(list.size(), img);
		spriteFrames.put(key, list);
//		createFlipped(key);
	}
	
	public void setAnimatedImage(String key) {
		if(spriteFrames.containsKey(key)) {
			this.setDisplayImage(spriteFrames.get(key).get(0));
			this.currentFrame = 0;
			this.animate = key;
		}
	}
	
	public HashMap<String, ArrayList<BufferedImage>> getSpriteFrames() {
		return spriteFrames;
	}

	public void setSpriteFrames(HashMap<String, ArrayList<BufferedImage>> spriteFrames) {
		this.spriteFrames = spriteFrames;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public boolean isPlaying() {
		return playing;
	}
	
	public void stop() {
		if(playing) {
			playing = false;
		}
	};
	
	public void start() {
		if(!playing) {
			playing = true;
		}
		timer = 0;
	};

	public String getAnimate() {
		return animate;
	}

	public void animate(String currentAnim) {
		this.animate = currentAnim;
		this.setAnimatedImage(currentAnim);
		this.setCurrentFrame(0);
		start();
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getNumFrames(String key) {
		if (spriteFrames.get(key) != null) {
			return spriteFrames.get(key).size() - 1;
		}
		return -1;
	}

	// flip image: http://stackoverflow.com/questions/23457754/how-to-flip-bufferedimage-in-java
	public void createFlipped(String key) {
		ArrayList<BufferedImage> toFlip = spriteFrames.get(key);
		ArrayList<BufferedImage> flipped = new ArrayList<BufferedImage>();
		for(int i = 0; i < toFlip.size(); i++ ) {
			BufferedImage img = toFlip.get(i);
			AffineTransform at = new AffineTransform();
			at.concatenate(AffineTransform.getScaleInstance(-1, 1));
			at.concatenate(AffineTransform.getTranslateInstance(-img.getWidth(), 0));
			BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = newImg.createGraphics();
			g.transform(at);
			g.drawImage(img, 0, 0, null);
			g.dispose();
			flipped.add(img);
		}
		spriteFrames.put(key + "Flip", flipped);
	}

	@Override
	public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
		if(this.getDisplayImage() != null) {
			if(playing) {
				timer++;
				if(timer == speed) {
					if(currentFrame < spriteFrames.get(animate).size() - 1) {
						currentFrame++;
					} else {
						currentFrame = 0;
						// throw animation complete event to prevent repeating animation
						// lazy solution
						this.dispatchEvent(new Event("ANIM_COMPLETE", this));
					}
					this.setDisplayImage(spriteFrames.get(animate).get(currentFrame));
					timer = 0;
				}
			}
		}
	}
}
