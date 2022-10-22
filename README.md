# AdvUIProject

minesweeper with roguelike elements using Java Swing

start with basic minesweeper

## Project Architecture
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

then add other stuff

- currency to buy powerups (1 for each bomb cleared or depending on staged cleared)

to give powerups, some are placed on the grid

Powerups:
- battleships on Steam power ups 
- Scan (with animation)
- Bomb remover
- Double coin
- Second life
- More bombs, more coins
- end game bonus minigame
- buy skins/themes
- easy mode
- endless mode
- candy crush map

MVC/PAC for system structure button for interaction and reveal with game logic why not use keyboard

#Speech
-point and speech

#Multiplayer:
- keep talking and nobody explodes
- just collaborative
- drawing
- turn by turn

Top:
- reveal
    - si bombe, fini
    - si 0 propagate

Bottom:
- check neighbour flag
    - propagate 


propagate:
si no flag on position
si top not visible
 - getNeighbours
 - si 0 -> propagate to neighbours


 - si value -> check flag nb
    - propagate neighbours


À faire:
- shop visuals
  - Center components, Better layout j

  

- Advanced visuals
  - Zooming in/out

