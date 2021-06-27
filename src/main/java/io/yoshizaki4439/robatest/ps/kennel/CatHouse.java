package io.yoshizaki4439.robatest.ps.kennel;


import io.yoshizaki4439.robatest.ps.animal.Cat;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Named("CatHouse")
@Dependent
public class CatHouse implements Serializable {
	
	private static final long serialVersionUID = 2179787307454475471L;
	

	
	@Inject
	private Cat cat;
	
	public void makeSounds(){
		cat.makeSound();
	}
	
}