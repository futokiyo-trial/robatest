package io.yoshizaki4439.robatest.job;

import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.construct.Flow;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import io.yoshizaki4439.robatest.utils.Constants;



@DisallowConcurrentExecution
public class JobExecutionEngine implements Job {

	private static final String FLOW_NAME_DISPATCHJOB = "dispatchJob";

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			String inputPayload = null;
			MuleContext muleContext = (MuleContext) jobExecutionContext.getScheduler().getContext()
					.get(MuleProperties.MULE_CONTEXT_PROPERTY);
			String jobDetailName = jobExecutionContext.getJobDetail().getKey().getName();
			String jobDetailGroup = jobExecutionContext.getJobDetail().getKey().getGroup();
			String jobDescription = jobExecutionContext.getJobDetail().getDescription();
			String flowName = (String) jobExecutionContext.getJobDetail().getJobDataMap()
					.get(Constants.PROPERTY_KEY_FLOWNAME);
			System.out.println(getClass().getSimpleName() + "-0001"+ "jobDetailName: " + jobDetailName + ", jobDetailGroup: "
							+ jobDetailGroup + ", jobDescription: " + jobDescription + ", flowName: " + flowName);
			Flow flow = (Flow) muleContext.getRegistry().lookupFlowConstruct(FLOW_NAME_DISPATCHJOB);
			MuleMessage muleMessage = new DefaultMuleMessage(inputPayload, muleContext);
			MuleEvent inputEvent = new DefaultMuleEvent(muleMessage, MessageExchangePattern.REQUEST_RESPONSE, flow);
			inputEvent.setFlowVariable(Constants.PROPERTY_KEY_JOBDETAILNAME, jobDetailName);
			inputEvent.setFlowVariable(Constants.PROPERTY_KEY_JOBDETAILGROUP, jobDetailGroup);
			inputEvent.setFlowVariable(Constants.PROPERTY_KEY_JOBDESCRIPTION, jobDescription);
			inputEvent.setFlowVariable(Constants.PROPERTY_KEY_FLOWNAME, flowName);
			MuleEvent outputEvent = flow.process(inputEvent);
			System.out.println(outputEvent + getClass().getSimpleName() + "-0002"+ "outputEvent: " + outputEvent);
			Object outputPayload = outputEvent.getMessage().getPayload();
			System.out.println(outputEvent + getClass().getSimpleName() + "-0003"+ "outputPayload: " + outputPayload);
		} catch (Exception e) {
			System.err.println(getClass().getSimpleName() + "-8000"+ "Job execution failed"+ e);
		}
	}
}