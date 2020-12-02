package com.spidergodd607.sudokusolver.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spidergodd607.sudokusolver.game.SudokuGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlin.math.floor

class SudokuViewModel : ViewModel() {

    val sudokuGame = SudokuGame()

    val suc: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun startSolving() {
        CoroutineScope(Default).launch {
            if (validate()) {
                suc.postValue(solve())
            }
            else{
                suc.postValue(false)
            }
        }

    }

    fun deleteall(){

        for(i in 0..8){
            for (j in 0..8){
                sudokuGame.insertval(i, j, 0)
            }
        }

    }

    //backtraking algo
    private suspend fun solve(): Boolean {
        val row: Int
        val col: Int
        val find = findEmpty()
        if (find == Pair(99, 99)) {

            return true
        } else {
            row = find.first
            col = find.second
        }
        for (i in 1..9) {
            if (valid(i, Pair(row, col))) {
                sudokuGame.insertval(row, col, i)
                if (solve()) {
                    return true
                }
                sudokuGame.insertval(row, col, 0)
            }
        }
        return false
    }

    //find the empty sells in the board
    private fun findEmpty(): Pair<Int, Int> {

        for (i in 0..8) {
            for (j in 0..8) {
                if (sudokuGame.board.getCell(i, j).value == 0) {
                    return Pair(i, j)
                }
            }
        }
        return Pair(99, 99)
    }

    private fun valid(number: Int, position: Pair<Int, Int>): Boolean {

        //check the row
        for (i in 0..8) {
            if (sudokuGame.board.getCell(
                    position.first,
                    i
                ).value == number && position.second != i
            ) {
                return false
            }
        }

        //check the col
        for (i in 0..8) {
            if (sudokuGame.board.getCell(
                    i,
                    position.second
                ).value == number && position.first != i
            ) {
                return false
            }
        }

        //check box
        val boxX = floor((position.second.toDouble() / 3.toDouble())).toInt()
        val boxY = floor((position.first.toDouble() / 3.toDouble())).toInt()

        for (i in (boxY * 3)..((boxY * 3) + 2)) {
            for (j in (boxX * 3)..((boxX * 3) + 2)) {
                if (sudokuGame.board.getCell(i, j).value == number && Pair(
                        i,
                        j
                    ) != position
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun validate(): Boolean {
        var ans: Boolean?
        for (i in 0..8){
            for (j in 0..8){
                if (sudokuGame.board.getCell(i, j).value != 0) {
                    ans = sudokuGame.board.getCell(i, j).value?.let { valid(it, Pair(i,j)) }
                    if(!ans!!){
                        return false
                    }
                }
            }
        }
        return true
    }
}