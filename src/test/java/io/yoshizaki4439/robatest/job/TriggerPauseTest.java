package io.yoshizaki4439.robatest.job;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import io.yoshizaki4439.robatest.ps.kennel.CatHouse;

@ExtendWith(MockitoExtension.class)
@ExtendWith(WeldJunit5Extension.class)
class TriggerPauseTest {
	
	@InjectMocks
	TriggerPause triggerPause = new TriggerPause();
	
	@Mock(lenient = true)
	MuleEvent muleEvent;

	@Mock(lenient = true)
	JobSchedulerStarter jobSchedulerStarter;

	@Mock(lenient = true)
	Scheduler scheduler;
	
	@WeldSetup
	public WeldInitiator weld = WeldInitiator
			.from(new Weld())
			.build();
	
	@Inject
	CatHouse catHouse;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testProcess() throws MuleException, NoSuchFieldException, SecurityException,
	IllegalArgumentException, IllegalAccessException {
		doReturn("triggerGroupExample")
		.doReturn("triggerGroupExample")
		.doReturn("triggerGroupExample")
		.when(muleEvent).getFlowVariable("triggerGroup");
		
		doReturn("triggerNameExample")
		.doReturn("triggerNameExample")
		.when(muleEvent).getFlowVariable("triggerName");

		doReturn(scheduler).when(jobSchedulerStarter).getScheduler();

		Class<TriggerPause> clazzTP = TriggerPause.class;
		Field jobSchedulerStarterField = clazzTP.getDeclaredField("jobSchedulerStarter");
		jobSchedulerStarterField.setAccessible(true);
		jobSchedulerStarterField.set(triggerPause, jobSchedulerStarter);

		triggerPause.process(muleEvent);
		
		assertEquals("check triggerKey triggerName", "triggerNameExample", triggerPause.getCurrentTriggerKey().getName());
		assertEquals("check triggerKey triggerGroup", "triggerGroupExample", triggerPause.getCurrentTriggerKey().getGroup());
		
		catHouse.makeSounds();
	}
	
	@Test
	public final void testProcess2OccursException() throws MuleException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException, SchedulerException {
		Assertions.assertThrows(IllegalStateException.class,()->{
			doReturn("triggerGroupExample").doReturn("triggerGroupExample").doReturn("triggerGroupExample").when(muleEvent)
			.getFlowVariable("triggerGroup");
			doReturn("triggerNameExample").doReturn("triggerNameExample").when(muleEvent).getFlowVariable("triggerName");

			doThrow(new SchedulerException()).when(scheduler).pauseTrigger(any(TriggerKey.class));

			doReturn(scheduler).when(jobSchedulerStarter).getScheduler();

			TriggerPause triggerPause = new TriggerPause();

			Class<TriggerPause> clazzTP = TriggerPause.class;
			Field jobSchedulerStarterField = clazzTP.getDeclaredField("jobSchedulerStarter");
			jobSchedulerStarterField.setAccessible(true);
			jobSchedulerStarterField.set(triggerPause, jobSchedulerStarter);

			catHouse.makeSounds();
			
			triggerPause.process(muleEvent);
			
			
		});
		

	}

}
