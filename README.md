## NANODEGREE APP : Popular Movies

App allows users to discover the most popular movies playing and sort them by popularity and rating with the complete
blend of android support design libraries and adaptive UI for both phone and tablet.

#### ScreenShots

![Movie List Phone](/screenshots/landing_phone.png?raw=true "Movie List Phone")
![Movie Details Phone](/screenshots/landing_detail_phone.png?raw=true "Movie Details Phone")
![Movie Tablet Portrait](/screenshots/landing_tablet.png?raw=true "Movie Tablet Portrait")
![Movie Tablet Landscape](/screenshots/landing_landscape_tablet.png?raw=true "Movie Tablet Landscape")

#### Tech

This App uses a number of open source projects to work properly:

* [RETROFIT] - Retrofit 2.0 by Square
* [Butterknife] - To bind the views with annotations.
* [Picasso] - Picasso allows for hassle-free image loading in your application—often in one line of code!
* [Google GSON] - To convert Json to java pojos or vice versa.
* [MOVIE DB API] - To Fetch Popular Movies and its Description, Reviews, Ratings etc.
* Material Design Support, RecyclerView Cards and CoordinatorLayout.


### Features
* Get the list of popular Movies
* Sort them based on Voting and Popularity
* Choose and save favorite movie(with Content Provider) and view them even offline
* View the Synopsis, Rating of Movie, Reviews and Trailer

### Build and Run Requirements

#### Generate API Key for Movie DB API

To fetch popular movies, you have to use the API from themoviedb.org.
If you don’t already have an account, you will need to create one in order to request an API Key.
URL : https://www.themoviedb.org/account/signup


* Oracle JDK 1.7
* Gradle 2.8
* Support Android 4.1 and Above (API 16)


### Tools used to develop
* Android Studio 1.4

[RETROFIT]: <http://square.github.io/retrofit/>
[Google GSON]: <https://github.com/google/gson>
[MOVIE DB API]: <https://www.themoviedb.org/>
[Picasso]: <http://square.github.io/picasso>
[Butterknife]: <http://jakewharton.github.io/butterknife/>
