# GroceryTool
This is one of my first apps that I have built for Android platform to learn how to make an Android app during my summer 2019.
The purpose of the app is to help the user calculate the amount of money they would need to pay when doing groceries.


## Technology
This project was built using Android Studio IDE. It was built and tested for Android 6.0. 

## Features
The user can add and remove the item that they want to buy, which will have the following properties:
+ Name
+ Quantity/Weight
+ Price per quantity/Weight

The user can know the price (no tax and with tax in percent) of all of the items added. The tax is changeable (the default is 8%). 
### Notes
The reason to distinguish between quantity and weight is to enforce input checking. For example, user can only input integer number for quantity, while they can input float number for weight.

The app will check for duplicate names, if there is a duplication, it will add the number of the duplication. For example, if an item is first duplication, it will have a 1 added to the name.

## How to try out the app.

There would be many ways to try the app, but the best way is to import this folder as an Android Studio Project, and run it from the application.
