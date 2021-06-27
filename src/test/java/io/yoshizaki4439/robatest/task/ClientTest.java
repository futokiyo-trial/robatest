package io.yoshizaki4439.robatest.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import io.yoshizaki4439.robatest.ps.kennel.CatHouse;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import javax.inject.Inject;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;

@ExtendWith(MockitoExtension.class) // Mockito による JUnit 5 エクステンション
@ExtendWith(WeldJunit5Extension.class)
class ClientTest {
	
	// モックを注入するオブジェクト
	@InjectMocks
	private Client client = new Client();
	// モック化するオブジェクト 
	@Mock
	private Worker mockedWorker;
	
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
	void testCallAriari() {
		// モックの振る舞い: Worker#ariari に 2 が渡されたときに 6 を返す
		doReturn(6).when(mockedWorker).ariari(2);
		// テスト: Client#callAriari に 1 を渡すとモックの Worker#ariari に 2 を渡して 6 が返ってくる
		assertEquals(6, client.callAriari(1));
		
		catHouse.makeSounds();
	}

	@Test
	void testCallNasinasi() {
		// モックの振る舞い: Worker#nasinasi を呼び出したときに何もしない
		doNothing().when(mockedWorker).nasinasi();
		// テスト: Client#callNasinasi に 1 を渡すとモックの Worker#nasinasi を実行して 2 が返ってくる
		assertEquals(2, client.callNasinasi(1));
		catHouse.makeSounds();
	}
	
	@Test
	void occureException() {
		// モックの振る舞い: Worker#ariari に 4 が渡されたときに例外を投げる
		doThrow(new IllegalArgumentException("モック例外")).when(mockedWorker).ariari(4);
		// テスト: Client#callAriari に 2 を渡すとモックの Worker#ariari に 4 を渡して例外が投げられる
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> client.callAriari(2));
		// テスト: 例外に想定したメッセージが含まれている
		assertEquals("モック例外", e.getMessage());
		
		catHouse.makeSounds();
	}
	
	@Test
	void callSequence() {
		// モックの振る舞い: Worker#ariari に 6 が渡されたときに例外を2回投げたあと 18 を返す
		doThrow(new IllegalArgumentException("モック例外1回目"))
	      .doThrow(new IllegalArgumentException("モック例外2回目"))
	      .doReturn(18)
	      .when(mockedWorker).ariari(6);
		// テスト: Client#callAriari に 3 を渡すとモックの Worker#ariari に 6 を渡して例外が投げられる
	    IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> client.callAriari(3));
	    assertEquals("モック例外1回目", e1.getMessage());
	    // テスト: Client#callAriari に 3 を渡すとモックの Worker#ariari に 6 を渡して例外が投げられる
	    IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> client.callAriari(3));
	    assertEquals("モック例外2回目", e2.getMessage());
	    // テスト: Client#callAriari に 3 を渡すとモックの Worker#ariari に 6 を渡して 18 が返ってくる
	    assertEquals(18, client.callAriari(3));
	    
	    catHouse.makeSounds();
	}
	
	

}
