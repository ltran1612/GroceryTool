package com.example.grocerytool;

import android.widget.LinearLayout;
import android.widget.TextView;

public class Item {
	private String name;
	private float amount;
	private float pricePerUnit;
	private boolean isQty;
	private LinearLayout myLayout;
	
	public Item() {
		setName("");
		setAmount(0);
		setPricePerUnit(0);
		setMyLayout(null);
	}
	
	public Item(String n, float a, float p, LinearLayout ml) {
		setName(n);
		setAmount(a);
		setPricePerUnit(p);
		setMyLayout(ml);
	}
	
	// accessors
	public String getName() {
		return name;
	}
	
	public float getAmount() {
		return amount;
	}
	
	
	public float getPricePerUnit() {
		return pricePerUnit;
	}

	public LinearLayout getMyLayout() {return myLayout;}

	public boolean getIsQty() {return isQty;}
	
	// mutators methods
	public void setName(String n) {
		name = n;
	}
	
	public void setAmount(float a) {
		if (a < 0)
			throw new IllegalArgumentException("Amount is negative");
		amount = a;
	}
	
	public void setPricePerUnit(float p) {
		if (p < 0)
			throw new IllegalArgumentException("Price per unit is negative");
		
		pricePerUnit = p;
	}

	public void setIsQty(boolean value) {
		isQty = value;
	}

	public void setMyLayout(LinearLayout ml) {
		myLayout = ml;
	}
	
	// other methods
	public float calculateTotalPrice() {
		return amount * pricePerUnit;
	}
	
	public String toString() {
		String result = String.format("Name: %s - Amount: %.2f - PricePerUnit: %.2f --> Total Price: %f", 
				   name, amount, pricePerUnit, calculateTotalPrice());
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			Item other = (Item) obj;
			return name.equals(other.name) && amount == other.amount && pricePerUnit == other.pricePerUnit;
		}
		
		return false;
	}

	public void updateMyLayout() {
		if (myLayout != null) {
			TextView name = (TextView) myLayout.findViewById(R.id.itemName);
			TextView amount = (TextView) myLayout.findViewById(R.id.itemAmount);
			TextView price = (TextView) myLayout.findViewById(R.id.itemPrice);

			name.setText("Name: "+this.name);
			amount.setText((this.isQty ? "Quantity: " : "Weight: ") + this.amount);
			price.setText((this.isQty ? "Price Per Quantity: " : "Price Per Weight: ") + this.pricePerUnit);
		}
	}


}
