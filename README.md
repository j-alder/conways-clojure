# Conway's Clojure

Conway's Game of Life written in Clojure, using [Quil](https://github.com/quil/quil) for graphics.

## Starting a game

Run `core.main` to start a new game of random initial state.

## Configuration

### Framerate
Adjust `q/frame-rate` in `core.setup`

### Number of cells
Set with `core.side-length`. This number squared is the total amount of cells.

### Color
The color of live and dead cells is configured in `core.fill` using rgb codes.
