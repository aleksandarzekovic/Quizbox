# Quizbox

Which this project, I have covered some areas brought by [Android Jetpack](https://developer.android.com/jetpack). 
Primarily, I wanted to include in addition to Firebase and the Room storage library, although we know that Firebase allows enabling [offline capabilities on android](https://firebase.google.com/docs/database/android/offline-capabilities). I wanted to improvise a little. 🚀

The application works on the principle of taking quiz types / results from Firebase and then loading them locally. If there is no internet access, the application takes the data locally.
<br />
In the application, I plan to add that the results are updated when the user reinstalls the application and logs in with the existing account.
Of course, I also plan to add unit tests. I know, unit tests were supposed to go first, but it doesn’t matter now. :sweat_smile: :joy:

<br />

<div align="center">
<img src="https://img.shields.io/badge/madeby-aleksandarzek-green"/>
</div>
<br />

![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `To use Firebase in application you need the google-services.json file in your project.`
<br />

## Application layouts
![Quizbox view1](/images/quizview.png)
<br />
![Quizbox view2](/images/quizview2.png)
<br />
![Quizbox view3](/images/quizview3.png)


## Tech stack & Libraries 
- [MVVM](https://developer.android.com/jetpack/guide?gclid=CjwKCAjwq_D7BRADEiwAVMDdHvfBvr-S0K0zYp7kDOAvDxQoJRe6O0NMZ4gBoekFQeqOJ9ER_ilkPhoCXd4QAvD_BwE&gclsrc=aw.ds) Architecture
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
- [Dagger Android](https://dagger.dev/dev-guide/android.html)
- [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Firebase Auth and Firestore](https://firebase.google.com/)


