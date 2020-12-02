package com.spidergodd607.sudokusolver.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    private var selectedRow = -1
    private var selectedCol = -1

    val board: Board

    init {
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, 0) }
        board = Board(9, cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return

        board.getCell(selectedRow, selectedCol).value = number
        cellsLiveData.postValue(board.cells)
    }

    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }

    fun delete() {
        val cell = board.getCell(selectedRow, selectedCol)
        cell.value = 0
        cellsLiveData.postValue(board.cells)
    }

    fun insertval(row: Int, col: Int, valuew: Int) {
        board.getCell(row, col).value = valuew
        cellsLiveData.postValue(board.cells)
    }
}