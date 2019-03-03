# FunkyFridge
Welcome to FunkyFridge! The Android Mobile solution for using your food before it goes bad!

## Inspiration
Every year, [**$680 billion worth of food is wasted**](http://www.fao.org/save-food/resources/keyfindings/en/) in industrialized countries. Funky Fridge was started with the goal of helping users minimize their food waste and make the most of their groceries. With so much food in a fridge, it's hard to keep track of which items will soon go bad. By providing users with notifications about which items will soon expire, they can prioritize those ingredients and use their food before their money goes in the trash can. 

## What it does
Funky Fridge will keep a running log of all of your perishable food items. Simply scan the food item's barcode and tell us its expiration date, and you're all set! Funky Fridge will have you covered.
Along with the expiration date, Funky Fridge will tell you important nutritional information for every good in your pantry. With different indicators for food approaching its expiration date and food that has already expired, users will know what food to use and what food to toss. Funky Fridge will help its users stay on track with the food in their fridge, preventing stinky fridges and gross, moldy food. 

## How we built it
The app was developed in Android Studio in Kotlin.

We used barcode detection from [**Google's Mobile Vision API**](https://developers.google.com/vision/) to convert the image of the barcode into its corresponding Universal Product Code (UPC). From here, we sent off the UPC to [**Nutritionix's food database API**](https://developer.nutritionix.com/v1_1/quick-start/upc-scan) to receive the nutritional data of the food item. The nutritional information along with user-inputted values for expiration dates was stored in an [**SQLite database**](https://www.sqlite.org/index.html). Each time a user created a new food item to add to the app, we created a new card for them in the recycle list view.

## Challenges we ran into
We had issues making the barcode detection API from Google to work. The button to launch the camera led to a black screen. Getting the permissions to work took a bit of time. We ran into trouble with the UI, specifically making it pleasing to look at and easy to interact with. Due to lack of time and general inexperience with Android Studio, we were not able to implement some of our desired features. 

Connecting the database with the information provided to us by the Nutritionix's API also posed a challenge, since none of us were experienced with executing a GET request on Android. Once the JSON package was received, it was easy to extract the data we needed. 

## Accomplishments that we're proud of
We are proud that we have a product to show. Coming in with little expectation, simply wanting to learn Android Studio and programming in Java/Kotlin, it has been a major accomplishment that we were able to finish the essential functions for this large project.

## What we learned
Throughout the development process, we learned how to use Google Mobile Vision API as well as Nutritionix's API.
Also, we learned a lot about Android's Recycler and how it can be used to dynamically create lists.

## What's next for Funky Fridge
We would like to implement the following features for Funky Fridge in the future:
* Search capability
* Favorites (simply add starred item to fridge rather than scanning barcode)
* Better UI/user experience with animations
