package com.family.account.mainProgram;

public class Item {
	private int id;	
	private String name;
	
	public Item(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String toString(){
		return this.name;
	}
}
