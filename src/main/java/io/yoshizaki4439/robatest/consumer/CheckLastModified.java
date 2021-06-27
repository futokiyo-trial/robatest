package io.yoshizaki4439.robatest.consumer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.processor.MessageProcessor;

public class CheckLastModified implements MessageProcessor  {

	@Override
	public MuleEvent process(MuleEvent event) throws MuleException {
		MuleMessage muleMessage = event.getMessage();
		String dirFile = event.getFlowVariable("dirFile");
		try {
			File file = new File(dirFile);
			Map<String, Object> payload = new HashMap<String, Object>();			
			Date date = new Date(file.lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			System.out.println("file.lastModified() : " + sdf.format(date));
			payload.put("checkLastModified", sdf.format(date));
			muleMessage.setPayload(payload);
		} catch (Exception ie) {
			throw new IllegalStateException("Check LastModified Failed. dirFile=" + dirFile, ie);
		}
		return event;
	}
}
