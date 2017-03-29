package edu.virginia.engine.events;

import edu.virginia.engine.display.PickedUpEvent;

public class QuestManager implements IEventListener {

	public boolean questCompleted = false;
	
	@Override
	public void handleEvent(Event event) {
		if(event.getEventType().equals("BOX_PICKED_UP") && event.getSource().hasEventListener(this, "BOX_PICKED_UP")) {
//			System.out.println("Quest Completed!\n\nLove,\nQuest Manager");
			questCompleted = true;
		}
	}

}
