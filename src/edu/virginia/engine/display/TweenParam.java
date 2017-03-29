package edu.virginia.engine.display;
	
public class TweenParam {
	
	private TweenableParams param;
	private double startVal;
	private double endVal;
	private double time;
	private double percentage = 0;
	
	public TweenParam(TweenableParams paramToTween, double startVal, double endVal, double time) {
		this.param = paramToTween;
		this.startVal = startVal;
		this.endVal = endVal;
		this.time = time;
	}
	
	public TweenableParams getParam() {
		return this.param;
	}
	
	public double getStartVal() {
		return this.startVal;
	}
	
	public double getEndVal() {
		return this.endVal;
	}
	
	public double getTweenTime() {
		return this.time;
	}
	
	public double getPercentage() {
		return this.percentage;
	}
	
	public void setPercentage(double newPercent) {
		this.percentage = newPercent;
	}
}
