package io.yoshizaki4439.robatest.job;

import lombok.Data;

@Data
public class JobParameter {
	private String jobDetailName;
	private String jobDetailGroup;
	private String jobDescription;
	private String flowName;
	private String triggerName;
	private String triggerGroup;
	private String cronExpression;
}
