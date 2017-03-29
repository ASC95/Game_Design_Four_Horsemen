package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class PickedUpEvent extends Event {

	public static final String BOX_PICKED_UP = "BOX_PICKED_UP";

	public PickedUpEvent(String eventType, IEventDispatcher source) {
		super(eventType, source);
	}

}
