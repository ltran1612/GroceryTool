package com.example.grocerytool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AsyncPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    LinearLayout scrollView;
    int i = 0;
    ItemsBasket basket;
    PopupWindow pw;
    float tax = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set background color
        findViewById(R.id.main).setBackgroundColor(getColor(R.color.background));

        // initializing new basket
        basket = new ItemsBasket();

        // get the scrollView to items box
        scrollView = findViewById(R.id.itemsBox);

        // initializing new pop up window
        pw = new PopupWindow(MainActivity.this);

        // do this to make edit text in the pop up window to by typable
        pw.setFocusable(true);
        pw.update();

        // change tax button
        findViewById(R.id.changeTaxButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // let the content view have the layout for tax
                pw.setContentView(LayoutInflater.from(MainActivity.this).inflate(R.layout.tax, null));

                // when done
                pw.getContentView().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pw.dismiss();

                        // update new tax
                        String s = ((EditText) pw.getContentView().findViewById(R.id.taxText)).getText().toString();
                        if (!s.isEmpty())
                            tax = Float.parseFloat(s);

                        // update new price
                        updateTotalPrice();
                    }
                });

                // displaying the change tax window
                pw.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
            }
        });

        // add item button
        findViewById(R.id.addItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                // make new item
                Item item = new Item();

                // new layout for item
                final LinearLayout layout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.item, null);
                scrollView.addView(layout, i - 1);
                basket.add(item);

                // set this item to the layout
                item.setMyLayout(layout);

                // remove item button
                layout.findViewById(R.id.itemRemove).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textView = ((TextView) layout.findViewById(R.id.itemName));
                        String name = "";

                        name = textView.getText().toString();
                        if (name.indexOf(' ') != -1) {
                            name = name.substring(name.indexOf(' '));
                            if (name.length() > 1)
                                name = name.substring(1);
                            basket.removeByName(name);
                        }

                        // update price after removing the item
                        updateTotalPrice();

                        // remove item from the item box
                        scrollView.removeView(layout);
                        i--;
                    }
                });

                // pop up window to determine if the item is measured by quantity or weight
                determineIsQuantity(item, true);
            }
        });

        // initializing the final price
        updateTotalPrice();

    } // end OnCreate

    // Change name, quantity/weight or price of an item
    // name determines the field I want to change in item
    // later - true then change the next fiend in the order (name -> quantity/weight -> price)
    private void changeItemInfo(final String name, final Item item, final boolean later) {
        pw.setContentView(LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_up, null));

        final EditText input = pw.getContentView().findViewById(R.id.Input);
        if (name.equals(getString(R.string.Name))) {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else if (name.equals(getString(R.string.Qty))) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else if (name.equals(getString(R.string.Weight))) {
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        }
        else if (name.equals(getString(R.string.PricePerQty)) || name.equals(getString(R.string.PricePerWeight))) {
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        }
        else
            throw new IllegalArgumentException("The type of pop up window is not valid: " + name);

        pw.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
        ((TextView) pw.getContentView().findViewById(R.id.Type)).setText(name);
        item.updateMyLayout();

        pw.getContentView().findViewById(R.id.Done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean noInput = input.getText().toString().isEmpty();
                String inputText = input.getText().toString();

                if (name.equals(getString(R.string.Name))) {
                    if (later)
                        changePopUpDismiss(getString(item.getIsQty() ? R.string.Qty : R.string.Weight), item, later);
                    if (!noInput) {
                        item.setName(inputText.trim());
                        basket.updateItemNameInBasket(item);
                    }

                } else {

                    if (name.equals(getString(R.string.Qty))) {
                        if (later)
                            changePopUpDismiss(getString(R.string.PricePerQty), item, later);
                        if (!noInput)
                            item.setAmount(Float.parseFloat(inputText));
                    } else if (name.equals(getString(R.string.Weight))) {
                        if (later)
                            changePopUpDismiss(getString(R.string.PricePerWeight), item, later);
                        if (!noInput)
                            item.setAmount(Float.parseFloat(inputText));

                    } else if (name.equals(getString(R.string.PricePerQty)) || name.equals(getString(R.string.PricePerWeight))) {
                        if (later)
                            changePopUpDismiss("", item, false);
                        if (!noInput)
                            item.setPricePerUnit(Float.parseFloat(inputText));
                    } else
                        throw new IllegalArgumentException("The type of pop up window is not valid: " + name);
                }

                item.updateMyLayout();

                input.setText("");
                updateTotalPrice();

                pw.dismiss();

            }
        });
    }

    private void changePopUpDismiss(final String name, final Item item, final boolean later) {
       pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
           @Override
           public void onDismiss() {
               if (name.equals(getString(R.string.Name)) || name.equals(getString(R.string.PricePerWeight))
                    || name.equals(getString(R.string.PricePerQty)) || name.equals(getString(R.string.Qty))
                    || name.equals(getString(R.string.Weight))) {
                   changeItemInfo(name, item, later);
               }
           }
       });
    }

    private void determineIsQuantity(final Item item, final boolean later) {
        pw.setContentView(LayoutInflater.from(MainActivity.this).inflate(R.layout.quanity_weight, null));

        pw.getContentView().findViewById(R.id.weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIsQty(false);
                pw.dismiss();
               if (later)
                   changeItemInfo(getString(R.string.Name), item, true);

            }
        });

        pw.getContentView().findViewById(R.id.quantity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIsQty(true);
                pw.dismiss();
                if (later)
                    changeItemInfo(getString(R.string.Name), item, true);

            }
        });

        pw.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
    }

    private void updateTotalPrice() {
        float finalPrice = basket.finalPrice();
        ((TextView) findViewById(R.id.FinalPrice)).setText(String.format("%s(no tax included): %.2f", getString(R.string.FinalPrice), finalPrice));
        ((TextView) findViewById(R.id.PriceWithTax)).setText(String.format("%s(%.2f%% tax included): %.2f", getString(R.string.FinalPrice), tax, finalPrice * (1 + tax / 100)));
    }

}
