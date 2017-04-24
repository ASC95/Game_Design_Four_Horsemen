package edu.virginia.engine.display;

import java.util.ArrayList;

import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.util.GameClock;

public class Tween extends EventDispatcher {

	private DisplayObject tween;
	private String transition = "";
	private boolean complete;
	private ArrayList<TweenParam> allParams = new ArrayList<TweenParam>();
	private GameClock timer = new GameClock();

	public Tween(DisplayObject object) {
		this.tween = object;
	}

	public Tween(DisplayObject object, String transition) {
		this.tween = object;
		this.transition = transition;
	}

	public void animate(TweenableParams fieldToAnimate, double startVal, double endVal, double time) {
		TweenParam param = new TweenParam(fieldToAnimate, startVal, endVal, time);
		allParams.add(param);
		this.timer.resetGameClock();
	}

	public void update() {
		for (int i = 0; i < allParams.size(); i++) {
			TweenParam thisParam = allParams.get(i);
			TweenableParams param = thisParam.getParam();
			double percentTime = timer.getElapsedTime() / thisParam.getTweenTime() ;
			if (percentTime > 1) {
				percentTime = 1;
				allParams.remove(i);
			}

			double newVal = thisParam.getStartVal() + (thisParam.getEndVal() - thisParam.getStartVal()) * percentTime;


			/*
			double start = thisParam.getStartVal();
			double end = thisParam.getEndVal();
			double animTime = thisParam.getTweenTime() * 60;
			double diff;
			if(end > start) {
				diff = end - start;
			} else {
				diff = start - end;
			}
			double percentInc = diff / animTime;
			double newVal = 0;
			double percentage = thisParam.getPercentage();
			
			if (percentage == 0) {
				newVal = start;
			} else {
				if (this.transition.equals("")) {
					double num = diff * percentage;
					if(end > start) {
						newVal = start + num;						
					} else {
						newVal = start - num;
					}
				} else if (this.transition.equals("easeInOut")) {
					double easeNum = diff * (3 * Math.pow(percentage, 2) - 2 * Math.pow(percentage, 3));
					if(end > start) {
						newVal = start + easeNum;
					} else {
						newVal = start - easeNum;
					}
				}
			}
			
			percentage += percentInc;
			if(percentage >= 1 || (end > start && newVal > end) || (start > end && newVal < end)) {
				percentage = 1;
				newVal = end;
			}
			*/
						
			if (param.equals(TweenableParams.X)) {
				tween.setPosition((int) newVal, tween.getPosition().y);
			} else if (param.equals(TweenableParams.Y)) {
				tween.setPosition(tween.getPosition().x, (int) newVal);
			} else if (param.equals(TweenableParams.SCALE_X)) {
				tween.setScaleX(newVal);
			} else if (param.equals(TweenableParams.SCALE_Y)) {
				tween.setScaleY(newVal);
			} else if (param.equals(TweenableParams.ALPHA)) {
				tween.setAlpha((float) newVal);
			} else { // rotation
				tween.setRotation((int) newVal);
			}
			// thisParam.setPercentage(percentage);
		}
		
		complete = true;
		for (int i = 0; i < allParams.size(); i++) {
			if(allParams.get(i).getPercentage() != 1) {
				complete = false;
			}
		}
	}

	public boolean isComplete() {
		return this.complete;
	}

	public ArrayList<TweenParam> getAllParams() {
		return this.allParams;
	}
}
