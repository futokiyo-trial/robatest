package io.yoshizaki4439.robatest.job;

import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.mule.api.config.MuleProperties;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import io.yoshizaki4439.robatest.utils.Constants;
import io.yoshizaki4439.robatest.utils.ExcelSheetData;
import io.yoshizaki4439.robatest.utils.ExcelUtils;

public class JobSchedulerStarter implements MuleContextAware {
	
	public static final String JOB_LIST_BOOK = "joblist.xlsx";
	public static final String JOB_LIST_SHEET_NAME = "joblist";
	
	private MuleContext muleContext;
	private Scheduler scheduler;

	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public void startupScheduler(Object payload) throws Exception {
		try {
			String url = "postgresql.jdbc.url.dummy";
			url = url.replaceAll("\\$\\{mule.home\\}", muleContext.getConfiguration().getMuleHomeDirectory());
			url = url.replaceAll("\\$\\{domain\\}", muleContext.getRegistry().get("domain"));

			Properties props = new Properties();
			props.setProperty("org.quartz.scheduler.instanceName", muleContext.getConfiguration().getId());
			props.setProperty("org.quartz.scheduler.instanceId", "AUTO");
			props.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
			props.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
			props.setProperty("org.quartz.threadPool.threadCount", "4");
			props.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
			props.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
			props.setProperty("org.quartz.jobStore.driverDelegateClass",
					"org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
			props.setProperty("org.quartz.jobStore.useProperties", "false");
			props.setProperty("org.quartz.jobStore.isClustered", "true");
			props.setProperty("org.quartz.jobStore.dataSource", "AppDS");
			props.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
			props.setProperty("org.quartz.dataSource.AppDS.connectionProvider.class",
					"compass.mule.job.QuartzConnectionProvider");
			props.setProperty("org.quartz.dataSource.AppDS.driver",
					"postgresql.jdbc.driver.dummy");
			props.setProperty("org.quartz.dataSource.AppDS.URL", url);
			props.setProperty("org.quartz.dataSource.AppDS.maxConnections", "4");
			props.setProperty("org.quartz.dataSource.AppDS.validationQuery", "select 0");
			StdSchedulerFactory schedulerFactory = new StdSchedulerFactory(props);
			scheduler = schedulerFactory.getScheduler();
		} catch (SchedulerException se) {
			System.err.println(getClass().getSimpleName() + "-8000" + "Job scheduler starter constructing failed" + se);
			throw new Exception();
		}
		try {
			List<ExcelSheetData> sheetsData = ExcelUtils.readSheets(JOB_LIST_BOOK);
			ExcelSheetData sheetData = null;
			for (ExcelSheetData sheet : sheetsData) {
				if (JOB_LIST_SHEET_NAME.equals(sheet.getSheetName())) {
					sheetData = sheet;
					break;
				}
			}
			if (sheetData == null) {
				throw new IllegalStateException("[" + JOB_LIST_SHEET_NAME + "] sheet cannot found in [" + JOB_LIST_BOOK + "].");
			}
			String[][] sheetArrayData = sheetData.getSheetArrayData();
			boolean flg = false;
			for (int i = 0; i < sheetArrayData.length; i++) {
				if (StringUtils.equalsIgnoreCase("END", sheetArrayData[i][0])) {
					break;
				} else if (StringUtils.equalsIgnoreCase("JOB DETAIL NAME", sheetArrayData[i][0])) {
					flg = true;
				} else if (flg && StringUtils.isNotBlank(sheetArrayData[i][0])) {
					JobParameter jobParameter = new JobParameter();
					jobParameter.setJobDetailName(sheetArrayData[i][0]);
					jobParameter.setJobDetailGroup(sheetArrayData[i][1]);
					jobParameter.setJobDescription(sheetArrayData[i][2]);
					jobParameter.setFlowName(sheetArrayData[i][3]);
					jobParameter.setTriggerName(sheetArrayData[i][4]);
					jobParameter.setTriggerGroup(sheetArrayData[i][5]);
					jobParameter.setCronExpression(sheetArrayData[i][6]);
					System.out.println(getClass().getSimpleName() + "-0000" +
							"jobDetailName: " + jobParameter.getJobDetailName() + ", jobDetailGroup: "
									+ jobParameter.getJobDetailGroup() + ", jobDescription: "
									+ jobParameter.getJobDescription() + ", flowName: " + jobParameter.getFlowName()
									+ ", triggerName: " + jobParameter.getTriggerName() + ", triggerGroup: "
									+ jobParameter.getTriggerGroup() + ", cronExpression: "
									+ jobParameter.getCronExpression());
					JobDetail jobDetail = JobBuilder.newJob(JobExecutionEngine.class)
							.withIdentity(jobParameter.getJobDetailName(), jobParameter.getJobDetailGroup())
							.withDescription(jobParameter.getJobDescription()).build();
					JobDataMap jobDataMap = jobDetail.getJobDataMap();
					jobDataMap.put(Constants.PROPERTY_KEY_FLOWNAME, jobParameter.getFlowName());
					CronTrigger trigger = TriggerBuilder.newTrigger()
							.withIdentity(jobParameter.getTriggerName(), jobParameter.getTriggerGroup())
							.withSchedule(CronScheduleBuilder.cronSchedule(jobParameter.getCronExpression())
									.inTimeZone(TimeZone.getTimeZone("Asia/Tokyo")))
							.startNow().build();
					scheduler.getContext().put(MuleProperties.MULE_CONTEXT_PROPERTY, muleContext);
					scheduler.deleteJob(jobDetail.getKey());
					scheduler.scheduleJob(jobDetail, trigger);
				}
			}
			scheduler.start();
			System.out.println(getClass().getSimpleName() + "-0001"+ "Scheduler started");
		} catch (SchedulerException se) {
			System.out.println(getClass().getSimpleName() + "-8001"+ "Job scheduler startup failed"+ se);
		} catch (Exception e) {
			System.out.println(getClass().getSimpleName() + "-8002"+ "Job scheduler startup failed"+ e);
		}
	}

	@Override
	public void setMuleContext(MuleContext arg0) {
		this.muleContext = muleContext;
	}

}
