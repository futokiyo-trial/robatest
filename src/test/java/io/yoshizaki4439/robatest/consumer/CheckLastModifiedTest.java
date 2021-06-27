package io.yoshizaki4439.robatest.consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
import org.mule.DefaultMuleEvent;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.construct.FlowConstruct;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.transport.DefaultMuleMessageFactory;




class CheckLastModifiedTest {
	
	private CheckLastModified checkLastModified = new CheckLastModified();

	private MuleContext context;
	private MuleMessage message;
	private MuleEvent event;

	private static final String TEST_FILE = "/tmp/test.txt";

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		context = new DefaultMuleContextFactory().createMuleContext();
		message = new DefaultMuleMessageFactory().create(null, "UTF-8", context);
		event = new DefaultMuleEvent(message, MessageExchangePattern.REQUEST_RESPONSE, (FlowConstruct) null);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@SuppressWarnings("deprecation")
	@Test
	void testProcess()  {

		File file = new File(TEST_FILE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(file.lastModified());
		String expected = sdf.format(date);
		
		event.setFlowVariable("dirFile", TEST_FILE);
		try {
			checkLastModified.process(event);
		} catch (MuleException e) {
			e.printStackTrace();
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> payload = (Map) event.getMessage().getPayload();
		assertEquals(expected, payload.get("checkLastModified").toString());
	}

}
