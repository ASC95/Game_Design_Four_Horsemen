package edu.virginia.engine.display;

import java.util.ArrayList;

public class TweenJuggler {

	private static TweenJuggler instance;
	public static ArrayList<Tween> allTweens = new ArrayList<Tween>();
	
	public TweenJuggler() {
		if(instance != null) {
			System.out.println("ERROR: Cannot re-initialize singleton class!");
		}
		instance = this;
	}
	
	public static TweenJuggler getInstance() {
		return instance;
	}
	
	public static void add(Tween tween) {
		allTweens.add(tween);
	}
	
	public void nextFrame() {
		for(int i = 0; i < allTweens.size(); i++) {
			Tween thisTween = allTweens.get(i);
			thisTween.update();
			if(thisTween.isComplete()) {
				allTweens.remove(thisTween);
			}
		}
	}
}
