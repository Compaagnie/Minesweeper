# AdvUIProject

minesweeper with roguelike elements using Java Swing

start with basic minesweeper

- look how to efficiently place mines
- create basic UI and game ogic

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



Top:
- reveal
    - si bombe fini
    - si 0 propage

Bottom:
- check neighboor flag
    - propage 


propagate:
si no flag on position
si top pas visible
 - getneighbours
 - si 0 -> propagate sur les voisins


 - si valeur -> vérifier nb de flag
    - propagate neighbours


À faire:
- basic menu
- bomb number choice and grid size choice
- restart
- better game visuals
