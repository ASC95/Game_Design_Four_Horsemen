package edu.virginia.engine.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;

//public class DisplayObjectContainer extends DisplayObject{
public class DisplayObjectContainer<T extends DisplayObject> extends DisplayObject {
	
	//private ArrayList<DisplayObject> children;
    private ArrayList<T> children;
	private boolean invisible;

	public DisplayObjectContainer(String id) {
		super(id);
		//children = new ArrayList<DisplayObject>();
        children = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}
	
	public DisplayObjectContainer(String id, String imageFileName) {
		super(id, imageFileName);
		//children = new ArrayList<DisplayObject>();
        children = new ArrayList<>();

		// TODO Auto-generated constructor stub
	}

	public DisplayObjectContainer(String id, Point position) {
		super(id, position);
		//children = new ArrayList<DisplayObject>();
        children = new ArrayList<>();

	}
	
	public DisplayObjectContainer(String id, String fileName, boolean visible, Point position) {
		super(id, fileName, visible, position);
		//children = new ArrayList<DisplayObject>();
        children = new ArrayList<>();

	}

	//public ArrayList<DisplayObject> getChildren() {
	//	return children;
	//}

	public Collection<T> getChildren() {
		return children;
	}

	//public void setChildren(ArrayList<DisplayObject> children) {
	//	this.children = children;
	//}
	
	public DisplayObject getChild(String child) {
		for(int i = 0; i < this.children.size(); i++) {
			DisplayObject current = this.children.get(i);
			if(current.getId().equals(child)) {
				return current;
			}
		}
		return null;
	}
	
	public DisplayObject getChildAtIndex(int i) {
		return this.children.get(i);
	}

	public boolean isInvisible() {
		return invisible;
	}

	//public void setInvisible(boolean invisible) {
	//	if(invisible) {
	//		for(int i = 0; i < this.children.size(); i++) {
	//			this.getChildren().get(i).setVisible(false);
	//		}
	//	} else {
	//		for(int i = 0; i < this.children.size(); i++) {
	//			this.getChildren().get(i).setVisible(true);
	//		}
	//	}
	//}

	//public void addChild(DisplayObject child) {
    public void addChild(T child) {
		child.setParent(this);
		this.children.add(child);
	}
	
	//public void addChildAtIndex(int index, DisplayObject child) {
    public void addChildAtIndex(int index, T child) {
		this.children.add(index, child);
	}
	
	public void removeChild(String id) {
		for(int i = 0; i < this.children.size(); i++) {
			String currentId = this.children.get(i).getId();
			if(currentId.equals(id)) {
				this.children.remove(i);
				// ONLY REMOVE ONE CHILD
				break;
			}
		}
	}
	
	public void removeByIndex(int i) {
		this.children.remove(i);
	}
	
	public void removeAll() {
		this.children.clear();
	}
	
	public boolean contains(DisplayObject child) {
		if(this.children.contains(child)) {
			return true;
		}
		return false;
	}
	
	@Override
	protected void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
		for (DisplayObject child : children) {
			child.update(pressedKeys);
			
		}
	}
	
	@Override
	public void draw(Graphics g){
		super.draw(g);
		Graphics2D g2d = (Graphics2D)g;

		AffineTransform at = applyTransformations(g2d);
		
		for(int i = 0; i < this.children.size(); i++) {
			this.children.get(i).draw(g);
		}
		
		reverseTransformations(g2d, at);
	}
	
}
