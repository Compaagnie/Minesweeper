# Advanced UI project : a rogue minesweeper 

## 1 - Instructions
### Install and launch
Either open the project with IntelliJ and run the main class located in the src folder

or

Download the jar file in this drive folder https://drive.google.com/file/d/1KN7mJmS6UWv3afl5zopSNGvcvE1WzaRr/view?usp=sharing
Allow them to run as programs and run them using Java 18 JRE

If the librairies leopard and jlayer are not working, please download them there:
http://www.java2s.com/Code/Jar/j/Downloadjlayer101jar.htm#google_vignette

https://repo1.maven.org/maven2/ai/picovoice/leopard-java/1.1.1/leopard-java-1.1.1.jar
And add them as project librairies.

### How to play:
Basic minesweeper rules:
- click a cell to reveal it
- You won a grid once you revealed all the cells that are not bombs
- right-click a cell to place a flag, flags prevent their cells from revealing


Rogue-like rules:
In rogue-like mode, every time you finish a grid, the next one becomes harder and bigger.

After completing a grid, you are given a choice between three powerups, you can hover them to look at the tooltip for a better description but the name should be explicit enough

There are some active and some passive power ups, described later in this document.
When you unlock an active power up you can use it by pressing the number key associated with it. From 1 to 4, if the number key alone doesn't work, you should try ALT+Number key.
Each active power up cost 1 energy to use so use them sparingly, you have 5 energy per grid.

You gain coins at the end of each grid that can be used in a shop to purchase active or passive for 10 or 5 coins respectively


Speech Recognition:
- to activate voice press ENTER or ALT+ENTER
  It will start recording your voice, when you release the key, it'll try to understand what you said and will execute the first instruction you said

Reveal keywords:
- "reveal", "propagate", "clear"

Flag keywords:
- "place", "flag", "flagged", "leg"

Random bomb reveal:
- "bomb", "random"

Column reveal keywords
- "column", "vertical"

Line reveal keywords:
- "line", "horizontal", "row"

Radar keywords:
- "radar", "zone", "scan", "area", "can", "skin"

We cheated a bit with some keywords for a better recognition

If you want some feedback for the recognition, I recommend launching through IntelliJ or a terminal



## 2 - Implemented power ups

- Actives :
  - Radar reveal (3x3 area)
  - Line reveal
  - Column reveal
  - Random bomb reveal (3 random bombs are flagged)
- Passives :
  - Double coin (x2 coins)
  - Double-edged sword (x3 coins, x1.5 bombs)
  - Revive
  - Easier next grid
  - Spawn a shop
  - First skill of each grid is free
  -
## 3 - Project Architecture
- src/
  - CustomComponents/
    - VSlider.java : vertical slider JComponent
    - BackgroundPanel.java : JPanel with custom background, and auto transparent components
    - Buttons/
      - ButtonTextures.java : loads the textures at startup and static get of specific textures
      - CellButton.java : Button used to represent a cell in a grid
      - MenuButton(UI).java : Definition of a instantiable JButton to match our app style
  - GridPAC/
    - CellChangeEvent.java : Event to communicate from grid model to grid
    - CellContent.java : final values treated as enums to help with grid content and make more readable code
    - Grid(Model).java : The grid, array of the game cells and its logic
    - GridEvent.java : Event used to communicate grid updates to the view.
    - Roguelike/
      - ..Grid(Model).java : grid with specific roguelike implementations (power ups, and overriding some basic grid behaviour)
  - GamePAC/
    - GameImages.java : enum constants defining useful game images (here : energy image)
    - GameMenu.java : Game menu, used to select mode (and preset)
    - GameView.java : Used to manage the games Input/Output system
    - Minesweeper.java : General Game class for the (rogue) minesweeper, initialised by main, creates the menu at startup and called to start a mode
    - Roguelike/
      - Roguelike(MVC).java : defines the roguelike general behaviour, succession of grids, power ups, shops, and managing effects of the different power ups that the player has.
      - PowerUps/
        - PowerUp.java : interface definition of a powerup (image, desctiption, cost, isActive?)
        - (Active/Passive)PowerUp.java : definition of a(n) (active/passive) powerup enumeration of constant Power Up implementations
        - PowerUpComponent.java : UI element to represent a power up, with shortcut (if active) and tooltip
  - Shop/
    - Shop.java : defines an instance of a shop, can be free or not (end stage shop or power up shop)
    - ShopButton.java : defines an item in a shop, with an optional shop price and behaviour on click (buy/selection)
  - SpeechRecognition/
    - Recorder.java : Object used to record audio from an audio device
    - SpeechRecognition.java : Object used to control a recorder and translate voice commands to inputs in the game view.