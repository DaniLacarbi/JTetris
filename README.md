# About this repository

Premise: this is my first Java project. I decided I wanted to learn a bit of Java coding (as someone who only used C++) while having fun!

JTetris is based on RyiSnow's Youtube tutorial, "How to code Tetris in Java" ([link here](https://www.youtube.com/watch?v=N1ktYfszqnM)). 
All game's core mechanics are his, while I mostly changed minor things I wanted to behave differently.

### Differences from his tutorial include:
- added option to mute the game independently from system's sound settings
- added option to choose between a dark and light mode
- added a "pause" sound
- added on-screen controls guide while the game is paused
- changed Minos' speed and sliding timings
- game is entirely localized in Italian🇮🇹 (comments/gameplay)

## How to run
You will need:
- JVM (Java Virtual Machine), you can download Oracle's or an open source implementation;
- A Java IDE, such as IntelliJ or Eclipse

To download this project, simply run
```
git clone https://github.com/danilacarbi/JTetris.git
```
Then, open your preferred IDE, import this project and then run the "Tetris.java" file.

Alternatively, a .jar file is available in the "Releases" tab (updated as of Feb 16th, 2025). To run it, install a JVM and run it from the terminal by navigating to the .jar file's directory and typing
```
java -jar JTetris.jar
```

## How to play
Once started, you must use the keyboard to play the game. Here is the list of all possible commands:
- UP arrow or W: rotate the Mino
- DOWN arrow or S: push the Mino down
- SX or DX arrows/A or D: move the Mino left or right
- ESCAPE or SPACE: pause the game, show controls (in Italian)
- C: switch between light and dark theme
- M: disable all music effects

Once the game is over, you have to close and rerun the program to play again.

## Possible future updates
Despite this being a not-that-serious project, I might consider updating the code to add/change the following:
- an English🇬🇧 version (comments/commands), maybe with option to switch within the game
- option to restart the game while running
- option to disable background music/sound effects only
- add comments to the single Mino classes
- add extra points, e.g. when 4 rows are deleted or when the screen is cleared

## Acknowledgments
I sincerly want to thank RyiSnow for making such a detailled yet simple-to-follow tutorial. When I have a lot of free time again, I will surely make another project like this!

I have also used some music effects, all of them are available in his video description. The only audio file you will not find there is the pause sound, 
which I have downloaded from [Pixabay](https://pixabay.com/sound-effects/search/pause/).
I do not claim ownership over any of the audio files nor code snippets I have not personally edited.

I also want to thank in advance anyone who plays the game and/or notifies me of any problems in the code.

## Important note
As stated above, this project is entirely localized in Italian🇮🇹. You may still play the game even if you don't understand Italian (of course), 
but you have to consider that all instructions and comments in the code are NOT in English. I may will work on that in the future. 

Also there might be some stupid jokes here and there, but after all, I made all of this for fun, so... GO AND PLAY TETRIS (or whatever you want).
