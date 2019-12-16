package com.example.grocerytool;

import android.util.Log;
import android.widget.LinearLayout;

import java.util.*;

public class ItemsBasket {
	private ArrayList<Item> basket;
	// private float finalPrice = 0;
	
	public ItemsBasket() {
		basket = new ArrayList<Item>();
	}
	
	// remove methods
	public boolean removeByName(String name) {
		int idx = searchByName(name);
		
		if (idx != -1) {
			Item item = basket.get(idx);
//			finalPrice -= item.getAmount() * item.getPricePerUnit();
//			if (finalPrice < 0) finalPrice = 0;
			basket.remove(idx);

			return true;
		}
		
		return false;
	}
	
	// add methods
	public void add(Item item) {
		int num = 0;
		int idx = searchByName(item.getName());

		while (idx != -1) {
			num++;
			idx = searchByName(item.getName() + " " + num);
		}
		
		if (num != 0)
			item.setName(item.getName() + " " + num);
		
		basket.add(item);
		//finalPrice += item.getAmount() * item.getPricePerUnit();
	}
	
	public void add(String name, float amount, float pricePerUnit, LinearLayout ml) {
		int num = 0;
		int idx = searchByName(name);

		while (idx != -1) {
			num++;
			idx = searchByName(name + " " + num);
		}

		Item item = new Item(name, amount, pricePerUnit, ml);
		if (num != 0)
			item.setName(name + " " + num);

		basket.add(item);
		//finalPrice += item.getAmount() * item.getPricePerUnit();
	}
	
	// search methods
	public int searchByName(String name) {
		int idx;
		for (idx = 0; idx < basket.size() && !(basket.get(idx).getName().equals(name)); ++idx);

		return idx >= basket.size() ? -1 : idx;
	}
	
	// length
	public int length() {
		return basket.size();
	}

	// price
	public float finalPrice() {
		float result = 0;
		for (int i = 0; i < basket.size(); ++i) {
			
			result += basket.get(i).calculateTotalPrice();
		}
		
		return result;
	}

	public void updateItemNameInBasket(Item item) {
		int num = 0;
		int idx = searchByName(item.getName());


		while (idx != -1 && basket.get(idx) != item) {
			num++;
			idx = searchByName(item.getName() + " " + num);

		}

		if (num != 0) {
			item.setName(item.getName() + " " + num);
		}

	}

//	public float getFinalPrice() {
//		return finalPrice;
//	}


}
