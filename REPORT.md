# A Rogue Minesweeper

minesweeper with roguelike elements using Java Swing

## Instructions
### Install and launch
Either open the project with IntelliJ and run the main class located in the src folder

or

Download the jar file in this drive folder https://drive.google.com/file/d/1KN7mJmS6UWv3afl5zopSNGvcvE1WzaRr/view?usp=sharing
Allow them to run as programs and run them using Java 18 JRE

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

## Project Architecture
- src/
    - CustomComponents/
        - VSlider.java : vertical slider JComponent
        - BackgroundPanel.java : JPanel with custom background, and auto transparent child components
        - Buttons/
            - ButtonTextures.java : loads the textures at startup and static get of specific textures
            - CellButton.java : Button used to represent a cell in a grid
            - MenuButton(UI).java : Definition of a instantiable JButton to match our app style
    - GridPAC/
        - CellChangeEvent.java : Event to communicate from grid model to grid
