# YouTubeExtractor
Simple library to help with keyboard operations

[![Build Status](https://travis-ci.org/Commit451/Teleprinter.svg?branch=master)](https://travis-ci.org/Commit451/Teleprinter)

# Gradle Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Then, add the library to your project `build.gradle`
```gradle
dependencies {
    compile 'com.github.Commit451:YouTubeExtractor:0.0.1'
}
```

# Usage
```java
//this == activity
mTeleprinter = new Teleprinter(this);
mTeleprinter.hideKeyboard();
//later...
mTeleprinter.showKeyboard(edittext);
```

License
--------

    Copyright 2016 Commit 451

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
