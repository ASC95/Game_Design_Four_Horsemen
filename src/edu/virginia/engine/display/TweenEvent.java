package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class TweenEvent extends Event {
	public final static String TWEEN_START_EVENT = TweenEvent.TWEEN_START_EVENT;
	public final static String TWEEN_UPDATE_EVENT = TweenEvent.TWEEN_UPDATE_EVENT;
	public final static String TWEEN_COMPLETE_EVENT = TweenEvent.TWEEN_COMPLETE_EVENT;
	
	public TweenEvent(String eventType, Tween tween) {
		super(eventType, tween);
	}

	public Tween getTween() {
		return (Tween) this.getSource();
	}
}
