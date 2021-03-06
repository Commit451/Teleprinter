# Teleprinter
The missing Android keyboard API

[![Build Status](https://travis-ci.org/Commit451/Teleprinter.svg?branch=master)](https://travis-ci.org/Commit451/Teleprinter)
[![](https://jitpack.io/v/Commit451/Teleprinter.svg)](https://jitpack.io/#Commit451/Teleprinter)

## Usage
```kotlin
//this == AppCompatActivity
val teleprinter = Teleprinter(this)
teleprinter.showKeyboard(edittext)
//later...
teleprinter.hideKeyboard()
//alternatively...
teleprinter.toggleKeyboard()
```
You can add listeners too:
```kotlin
teleprinter.addOnKeyboardOpenedListener {
    Toast.makeText(this, "Keyboard opened", Toast.LENGTH_SHORT)
            .show()
}
```
and
```kotlin
teleprinter.addOnKeyboardClosedListener {
    Toast.makeText(this, "Keyboard closed", Toast.LENGTH_SHORT)
            .show()
}
```

## Dependency
```
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
	}
}
```
then: 
```
implementation("com.github.Commit451:Teleprinter:2.2.0")
```
Teleprinter also requires Java 8 bytecode. To enable, add the following to your build.gradle:
```
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
```

## Note
It is worth noting that this library is a "hack" around a lack of an official keyboard API from Google,
therefore it is potentially breakable with each Android version, and does not work with certain keyboard
configurations, such as "floating keyboards" or very small keyboards, or even hardware keyboards. Keep
these things in mind when using this library.

License
--------

    Copyright 2019 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
