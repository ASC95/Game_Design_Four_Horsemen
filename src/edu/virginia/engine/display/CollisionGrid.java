// basic grid class : http://www.java-gaming.org/index.php?topic=29244.0
package edu.virginia.engine.display;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollisionGrid {
	
	private int rows, cols;
	private int cellX, cellY;

	@SuppressWarnings("unchecked")
	private ArrayList<PhysicsSprite>[][] collisionGrid;

	// list of sprites that are close to one sprite
	private List<PhysicsSprite> retrieveList = new ArrayList<PhysicsSprite>();
	// stores all collidable sprites and an arraylist of int[] that stores the grid squares it occupies
	private Map<PhysicsSprite, ArrayList<int[]>> allCollidables = new HashMap<>();
	
	public CollisionGrid(int screenWidth, int screenHeight, int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		cellX = screenWidth/cols;
		cellY = screenHeight/rows;
		
		collisionGrid = (ArrayList<PhysicsSprite>[][]) new ArrayList[cols][rows];
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				collisionGrid[i][j] = new ArrayList<PhysicsSprite>();
			}
		}
		int row = collisionGrid[0].length;
		int col = collisionGrid.length;
	}
	
	public ArrayList<PhysicsSprite>[][] getCollisionGrid() {
		return collisionGrid;
	}

	public void setCollisionGrid(ArrayList<PhysicsSprite>[][] collisionGrid) {
		this.collisionGrid = collisionGrid;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getCellX() {
		return cellX;
	}

	public void setCellX(int cellX) {
		this.cellX = cellX;
	}

	public int getCellY() {
		return cellY;
	}

	public void setCellY(int cellY) {
		this.cellY = cellY;
	}

	public List<PhysicsSprite> getRetrieveList() {
		return retrieveList;
	}

	public void setRetrieveList(List<PhysicsSprite> retrieveList) {
		this.retrieveList = retrieveList;
	}

	public Map<PhysicsSprite, ArrayList<int[]>> getAllCollidables() {
		return allCollidables;
	}

	public void setAllCollidables(Map<PhysicsSprite, ArrayList<int[]>> allCollidables) {
		this.allCollidables = allCollidables;
	}

	public void clear() {
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				collisionGrid[i][j].clear();
				allCollidables.clear();
			}
		}
	}
	
	public void addSprite(PhysicsSprite s) {
		int topLeftX = (int) Math.max(0, (s.getPosition().x - s.getPivotPoint().x)/cellX);
		int topLeftY = (int) Math.max(0, (s.getPosition().y - s.getPivotPoint().y)/cellY);
		int bottomRightX = (int) Math.min(cols - 1, (s.getUnscaledWidth() + s.getPosition().x - s.getPivotPoint().x) / cellX);
		int bottomRightY = (int) Math.min(rows - 1, (s.getUnscaledHeight() + s.getPosition().y - s.getPivotPoint().y) / cellY);
		ArrayList<int[]> list = new ArrayList<>();

		for(int x = topLeftX; x <= bottomRightX; x++) {
			for(int y = topLeftY; y <= bottomRightY; y++) {
				collisionGrid[x][y].add(s);
				int[] arr = {x, y};
				list.add(arr);
			}
		}
		
		allCollidables.put(s, list);
		
	}
	
	public void addSprites(ArrayList<PhysicsSprite> list) {
		for(int i = 0; i < list.size(); i++) {
			PhysicsSprite s = list.get(i);
			
			int topLeftX = (int) Math.max(0, (s.getPosition().x - s.getPivotPoint().x)/cellX);
			int topLeftY = (int) Math.max(0, (s.getPosition().y - s.getPivotPoint().y)/cellY);
			int bottomRightX = (int) Math.min(cols - 1, (s.getUnscaledWidth() + s.getPosition().x - s.getPivotPoint().x) / cellX);
			int bottomRightY = (int) Math.min(rows - 1, (s.getUnscaledHeight() + s.getPosition().y - s.getPivotPoint().y) / cellY);
			
			ArrayList<int[]> newList = new ArrayList<>();
			for(int x = topLeftX; x <= bottomRightX; x++) {
				for(int y = topLeftY; y <= bottomRightY; y++) {
					collisionGrid[x][y].add(s);
					int[] arr = {x, y};
					newList.add(arr);
				}
			}
			
			allCollidables.put(s, newList);

		}
	}
	
	public void removeSprite(PhysicsSprite s) {
		ArrayList<int[]> list = allCollidables.get(s);
		
		for(int i = 0; i < list.size(); i++) {
			int[] current = list.get(i);
			int x = current[0];
			int y = current[1];
			
			collisionGrid[x][y].remove(s);
		}
		
		allCollidables.remove(s);
	}
	
	// retrieves list of sprites that occupy the same cells as sprite s
	public List<PhysicsSprite> retrieve(PhysicsSprite s) {
		
		retrieveList.clear();
		
		int topLeftX = (int) Math.max(0, (s.getPosition().x - s.getPivotPoint().x)/cellX);
		int topLeftY = (int) Math.max(0, (s.getPosition().y - s.getPivotPoint().y)/cellY);
		int bottomRightX = (int) Math.min(cols - 1, (s.getUnscaledWidth() + s.getPosition().x - s.getPivotPoint().x) / cellX);
		int bottomRightY = (int) Math.min(rows - 1, (s.getUnscaledHeight() + s.getPosition().y - s.getPivotPoint().y) / cellY);
		
		for(int x = topLeftX; x <= bottomRightX; x++) {
			for(int y = topLeftY; y <= bottomRightY; y++) {
				List<PhysicsSprite> cell = collisionGrid[x][y];
				
				for(int i = 0; i < cell.size(); i++) {
					PhysicsSprite retrieved = cell.get(i);
					if(!retrieveList.contains(retrieved)) {
						retrieveList.add(retrieved);
					}
				}
			}
		}
		
		return retrieveList;
	}
	
	//automatically update grid
	public void update(ArrayList<Integer> pressedKeys) {
		// arraylist of all sprites
		ArrayList<PhysicsSprite> list = new ArrayList<PhysicsSprite>(allCollidables.keySet());
		// iterate through sprites 
		for(int i = 0; i < list.size(); i++) {
			PhysicsSprite s= list.get(i);
			// currently stored occupied cells of s
			ArrayList<int[]> occupiedCells = allCollidables.get(s);
			// goes through each sprite's currently occupied cells and removes them from collisionGrid to update
			for(int p = 0; p < occupiedCells.size(); p++) {
				int[] lmao = occupiedCells.get(p);
				int x = lmao[0];
				int y = lmao[1];
				
				if (collisionGrid[x][y].contains(s)) {
					collisionGrid[x][y].remove(s);
				}
			}
			
			allCollidables.get(s).clear();

			// calculates the new cells s occupies
			int topLeftX = (int) Math.max(0, (s.getPosition().x - s.getPivotPoint().x)/cellX);
			int topLeftY = (int) Math.max(0, (s.getPosition().y - s.getPivotPoint().y)/cellY);
			int bottomRightX = (int) Math.min(cols - 1, (s.getUnscaledWidth() + s.getPosition().x - s.getPivotPoint().x) / cellX);
			int bottomRightY = (int) Math.min(rows - 1, (s.getUnscaledHeight() + s.getPosition().y - s.getPivotPoint().y) / cellY);
			
			ArrayList<int[]> newList = new ArrayList<>();
			for(int x = topLeftX; x <= bottomRightX; x++) {
				for(int y = topLeftY; y <= bottomRightY; y++) {
					collisionGrid[x][y].add(s);
					int[] arr = {x, y};
					newList.add(arr);
				}
			}
			
			allCollidables.put(s, newList);
//			System.out.println(s.getId());
//			for(int p = 0; p < newList.size(); p++) {
//				int[] t = newList.get(p);
//				System.out.println("[" + t[0] + ", " + t[1] + "]");
//			}			
		}	
		
		for(int q = 0; q < retrieveList.size(); q++) {
			PhysicsSprite pp = retrieveList.get(q);
			System.out.println(pp.getId());
		}

	}
	
	// returns sprite nearest to s
	public PhysicsSprite getNearest(PhysicsSprite s) {
		PhysicsSprite nearest = null;
		long distance = Long.MAX_VALUE;
		
		List<PhysicsSprite> collidables = retrieve(s);
		for(int i = 0; i < collidables.size(); i++) {
			PhysicsSprite toCheck = collidables.get(i);
			long dist =  (long) (Math.pow(toCheck.getPosition().x - s.getPosition().x, 2) + Math.pow(toCheck.getPosition().y - s.getPosition().y, 2));
			if (dist < distance)
	        {
	            nearest = toCheck;
	            distance = dist;
	        }
		}
		return nearest;
	}
}
