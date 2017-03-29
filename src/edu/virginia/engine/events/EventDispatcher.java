package edu.virginia.engine.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventDispatcher implements IEventDispatcher {

	private HashMap<IEventListener, ArrayList<String>> events = new HashMap<IEventListener, ArrayList<String>>();
	
	@Override
	public void addEventListener(IEventListener listener, String eventType) {
		if(events.get(listener) == null) {
			events.put(listener, new ArrayList<String>());
		}
		events.get(listener).add(eventType);
	}

	@Override
	public void removeEventListener(IEventListener listener, String eventType) {
		events.get(listener).remove(eventType);
	}

	@Override
	public void dispatchEvent(Event event) {
		for(Map.Entry<IEventListener, ArrayList<String>> listen : events.entrySet()) {
			if(listen.getValue().contains(event.getEventType())) {
				listen.getKey().handleEvent(event);
			}
		}
	}
	
	@Override
	public boolean hasEventListener(IEventListener listener, String eventType) {
		if(events.get(listener).contains(eventType)) {
			return true;
		}
		return false;
	}
	
}
