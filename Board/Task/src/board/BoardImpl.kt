package board

import board.Direction.*

class SquareBoardImpl(override val width: Int) : SquareBoard {

    private val cells: List<Cell> = (0 until  width*width).mapNotNull {Cell((it % width) + 1, (it / width) + 1)}

    override fun getCellOrNull(i: Int, j: Int): Cell? = if (i > width || j > width || i == 0 || j == 0) null
        else  cells[(i - 1) + (j - 1) * width]

    override fun getCell(i: Int, j: Int): Cell = getCellOrNull(i, j) ?: throw IllegalArgumentException("Incorrect arguments of values $i and $j")
    override fun getAllCells(): Collection<Cell> = cells
    override fun getRow(i: Int, jRange: IntProgression): List<Cell> = jRange.takeWhile{ it in 1..width }.map{ getCell(i, it) }
    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> = iRange.takeWhile{ it in 1..width }.map{ getCell(it, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when(direction) {
        UP -> getCellOrNull(i - 1, j)
        LEFT -> getCellOrNull(i, j - 1)
        DOWN -> getCellOrNull(i + 1, j)
        RIGHT -> getCellOrNull(i, j + 1)
    }
}


class GameBoardImpl<T> (private val sb: SquareBoard) : SquareBoard by sb, GameBoard<T> {
    constructor(w: Int) : this(SquareBoardImpl(w))


    private val cells: MutableMap<Cell, T?> = sb.getAllCells().map{ it to null }.toMap().toMutableMap()

    override operator fun get(cell: Cell): T? = cells[cell]
    override operator fun set(cell: Cell, value: T?) { cells += cell to value }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = cells.filterValues{ predicate(it) }.keys
    override fun find(predicate: (T?) -> Boolean): Cell? = cells.filterValues{ predicate(it) }.keys.first()
    override fun any(predicate: (T?) -> Boolean): Boolean = cells.values.any(predicate)
    override fun all(predicate: (T?) -> Boolean): Boolean = cells.values.all(predicate)
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)