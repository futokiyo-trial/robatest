package io.yoshizaki4439.robatest.job;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

public class TriggerPause implements MessageProcessor {

	private JobSchedulerStarter jobSchedulerStarter;
	
	private TriggerKey currentTriggerKey;
	
	@Override
	public MuleEvent process(MuleEvent event) throws MuleException {
		try {
			System.out.println(getClass().getSimpleName() + "-0000"+ "triggerName: "
					+ event.getFlowVariable("triggerName") + ", triggerGroup: " + event.getFlowVariable("triggerGroup"));
			TriggerKey triggerKey = new TriggerKey(event.getFlowVariable("triggerName"),
					event.getFlowVariable("triggerGroup"));
			jobSchedulerStarter.getScheduler().pauseTrigger(triggerKey);
			this.currentTriggerKey = triggerKey;
			System.out.println(getClass().getSimpleName() + "-0001"+ "Trigger paused");
		} catch (SchedulerException se) {
			System.err.println(getClass().getSimpleName() + "-8001"+ "Trigger pause failed "+ se);
			throw new IllegalStateException(se.getMessage(), se);
		}
		return event;
	}
	
	public TriggerKey getCurrentTriggerKey() {
		return this.currentTriggerKey;
	}

}
