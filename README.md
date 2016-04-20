# YouTubeExtractor
A helper to extract the streaming URL from a YouTube video

[![Build Status](https://travis-ci.org/Commit451/YouTubeExtractor.svg?branch=master)](https://travis-ci.org/Commit451/YouTubeExtractor)

This library was originally found [here](https://github.com/flipstudio/YouTubeExtractor) in a project by [flipstudio](https://github.com/flipstudio). It has since been modified and cleaned up a bit to make it more user friendly.

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
This library is only responsible for getting the information needed from a YouTube video given its video id (which can be found in the url of any youtube video. It is not responsible for playback of the video. However, you can see in the sample app how this can be done using `MediaPlayer` and a `SurfaceView`
Typical usage looks like this:
```java
YouTubeExtractor extractor = new YouTubeExtractor(GRID_YOUTUBE_ID);
    extractor.extract(new YouTubeExtractor.Callback() {
        @Override
        public void onSuccess(YouTubeExtractor.Result result) {
            Uri hdUri = result.getHd1080VideoUri();
            //See the sample for more
        }

        @Override
        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    });
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
