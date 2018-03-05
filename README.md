# ExpandableCardView
Simple expandable CardView for Android.
Please note that this library is still in early development stage!

This readme is WIP

## Description

WIP

## Demo

Soon available on the Google Play Store.

# Setup

## Getting the Library

First of all, get the library by cloning this repo (soon it will be available by Jar and by Maven Central)

## Declaring the view

After you have the Library correctly setup, just declare the ExpandableCardView in your xml:

```xml
<com.alespero.expandablecardview.ExpandableCardView
    android:id="@+id/mycard"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:title="Item"
    app:inner_view="@layout/mycustomview"/>
```

You can specify a custom title on the header on the card by setting the attribute ```app:title```.
After you created the base xml, just create your custom layout and place it inside your ```layout``` folder. Now just set pass your newly created layout resource to the ```app:inner_view``` attribute. Done! Now your ExpandableCardView is ready to roll. (More attributes soon to come)

## Usage

All you need to do now is to just declare your ExpandableCardView in your activity and you're ready to use its methods.

### Java
Too lazy to write Java example right now, sorry :P

### Kotlin

```kotlin
val card : ExpandableCardView = findViewById(R.id.mycard)

        card.setOnClickListener({
            if(card.isExpanded) card.collapse()
            else card.expand()
        })
```

You can use ```expand()``` and ```collapse()``` to respectively expand and collapse the card, and use ```isExpanded()``` to check if the card is expanded or not.

You can also set an OnExpandedListener to the card:

### Java
Too lazy to write Java example right now, sorry :P

### Kotlin

```kotlin
 card.setOnExpandedListener(object : ExpandableCardView.OnExpandedListener {
            override fun onCollapsed(v: View) {
                Toast.makeText(applicationContext, "Collapsed!", Toast.LENGTH_SHORT).show()
            }

            override fun onExpanded(v: View) {
                Toast.makeText(applicationContext, "Expanded!", Toast.LENGTH_SHORT).show()
            }
        })
```
## Contribute

This library is still in its early stages, so feel free to contribute. I will review any Pull Request in 24-48 hours.

## License

```
  Copyright (c) 2018 Alessandro Sperotti
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
```

## Author
Made By [Alessandro Sperotti](www.alessandrosperotti.com). 

alessandrosperotti at gmail dot com

If you liked this library, why don't you offer me a coffee (or a beer? :D)

ETH: 0x70b6b9eaCDf6f76ECb92bE3fb81d72B3971BA1Bc
