# AdvUIProject

minesweeper with roguelike elements using Java Swing

start with basic minesweeper

- look how to efficiently place mines
- create basic UI and game logic

then add other stuff

- currency to buy powerups (1 for each bomb cleared or depending on staged cleared)

to give powerups, some are placed on the grid

Powerups:
- battle ships on Steam power ups 
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
    - si bombe fini
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
  - Center components, Better layout
  - Background (little market)


- textures          Ju
  - bomb
  - losing bomb
  - losing flag


- Info visuals
  - Energy
  - Column info (passive/active)
  - Coin icon
  - keys and power name (1-Line)
  - PowerUps
  - Progress visualisation
  

- Advanced visuals
  - Background
  - Menu Layout
  - Zooming in/out
  - ! Aspect ratio, always square grid

