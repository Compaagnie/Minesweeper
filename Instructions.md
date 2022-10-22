# Advanced UI project : a rogue minesweeper 

## 1 - Instructions

How to play:
- click a cell to reveal it
- right click to place a flag
- When you unlock an active power up you can use it by pressing the number key associated with it ||if just the number doesn't work, press ALT+number||

In rogue like mode, you have 5 energy per grid with each power up use costing one energy
You gain coins at the end of each grid that can be used in a shop to purchase active or passive in the shop for 10 or 5 coins

- to activate voice press ENTER or ALT+ENTER
  Reveal key words:
- "reveal" "propagate" "clear"
  Flag key words:
- "place" "flag" "flagged"

Random bomb reveal:
- "bomb", "random"
  Column reveal key words
- "column", "vertical"
  Line reveal key words:
- "line", "horizontal", "row"
  Radar key words:
- "radar", "zone", "scan", "area"});

There isn't much feedback but it should work


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