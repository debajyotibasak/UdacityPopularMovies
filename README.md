<p align="center"> 
<img src="https://github.com/debo1994/UdacityPopularMovies/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png">
</p>

<h1 align="center">Popular Movies App</h1>
<p align="center">This is an App made for Udacity Android Nanodegree Project 2. Its an android app to browse the Top Rated and Popular Movies. This app uses an API provided by https://www.themoviedb.org/. It is made with latest Android Architecture Components like LiveData, ViewModel and Room. It uses Retrofit with LiveDataCallAdapter for Network Calls and uses Dagger2 for dependency Injection.</p>

<h2>Features</h2>

- Browse through the Top Rated and Popular Movies
- Offline access to movies list
- Caches data from Room Database (Refreshes every 60 minutes from Network)
- Design inspired from Pinterest
- UI optimized for potrait and landscape.

<h2>Screenshots</h2>
<img src="https://github.com/debo1994/UdacityPopularMovies/blob/master/screenshots/screener_1529345854758.png" width="450" height="800"></br>
<img src="https://github.com/debo1994/UdacityPopularMovies/blob/master/screenshots/screener_1529345887129.png" width="450" height="800"></br>
<img src="https://github.com/debo1994/UdacityPopularMovies/blob/master/screenshots/screener_1529345905882.png" width="450" height="800"></br>
<img src="https://github.com/debo1994/UdacityPopularMovies/blob/master/screenshots/screener_1529345923626.png" width="800" height="450"><br/>

<h2>Screen Video</h2>
<img src="https://github.com/debo1994/UdacityPopularMovies/blob/master/gifs/stage1.gif" width="450" height="800"></br>

<h2>Steps to run the app</h2>
<p>The app uses themoviedb.org API to get movie information and posters. You must provide your own API key in order to build the app.</p>
<p>If you do not have a gradle.properties file, create one</p>
<ol>
<li>Change you Android view to Project file in the directory</li>
<li>Right click > New > File</li>
<li>Put the name as gradle.properties</li>
</ol>
<p>Now paste THE_MOVIE_DB_API_KEY="your-api-key-here" in the gradle.properties file</p>
<p>Build the project and Run</p>

## Download APK
[Release v1.0](https://github.com/debo1994/UdacityPopularMovies/releases/download/v1.0/app-debug.apk)

## Libraries

* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/)
* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Dagger2](https://github.com/google/dagger)
* [Retrofit2](https://github.com/square/retrofit)
* [Glide](https://github.com/bumptech/glide)
* [Glide Transformations](https://github.com/wasabeef/glide-transformations)
* [Android Debug Database](https://github.com/amitshekhariitbhu/Android-Debug-Database)
* [FlowLayoutManager](https://github.com/xiaofeng-han/AndroidLibs/tree/master/flowlayoutmanager)

## License
Copyright 2018 Debajyoti Basak
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
