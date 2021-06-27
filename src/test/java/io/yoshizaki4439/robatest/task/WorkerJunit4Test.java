package io.yoshizaki4439.robatest.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;



import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class,Worker.class})
public class WorkerJunit4Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOne() {
		mockStatic(System.class);
		when(System.currentTimeMillis()).thenReturn(3L);
		
		Worker worker = new Worker();
		assertEquals("1", 1, worker.getOne());
		assertEquals("3L", 3L, worker.hoge());
	}

}
