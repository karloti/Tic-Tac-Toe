package tictactoe

import kotlin.math.abs

class TicTacToe {

    class DataGame {
        val data = IntArray(9) { -1 }       // All data of game with each moves
        var str = " ".repeat(9)
        var countX = 0
        var countO = 0
        var checkX = 0
        var checkO = 0
        var countFree = 9
    }

    private var dataGame = DataGame()            // init data

    private val statusGameLabels = listOf(
            "Game not finished",    // 0
            "Impossible",           // 1
            "Draw",                 // 2
            "X wins",               // 3
            "O wins",               // 4
            "Incorrect input",      // 5
    )

    private val statusPositionLabeled = listOf(
            "Correct",                                    // 0
            "You should enter numbers!",                  // 1
            "Coordinates should be from 1 to 3!",         // 2
            "This cell is occupied! Choose another one!", // 3
    )

    private fun stringToData(s: String): DataGame {
        val dataGame = DataGame()
        for (index in s.indices) when (s[index]) {
            'X' -> dataGame.data[dataGame.countX++ * 2] = index
            'O' -> dataGame.data[dataGame.countO++ * 2 + 1] = index
        }

        val checks = listOf(
                0, 4, 8,    // diagonal 1
                2, 4, 6,    // diagonal 2
                0, 1, 2,    // horizontal 1
                3, 4, 5,    // horizontal 2
                6, 7, 8,    // horizontal 3
                0, 3, 6,    // vertical 1
                1, 4, 7,    // vertical 2
                2, 5, 8,    // vertical 3
        ).chunked(3)

        dataGame.countFree = 9 - dataGame.countX - dataGame.countO
        for (triplePos in checks) {
            var tripleStr = ""
            for (j in triplePos) tripleStr += s[j]
            when (tripleStr) {
                "XXX" -> dataGame.checkX++
                "OOO" -> dataGame.checkO++
            }
        }
        dataGame.str = dataToString(dataGame)
        return dataGame
    }

    private fun dataToString(dataGame: DataGame): String {
        val s = " ".repeat(9).toCharArray()
        for ((index, value) in dataGame.data.withIndex()) {
            if (value != -1) s[value] = if (index % 2 == 0) 'X' else 'O'
        }
        return s.joinToString("")
    }

    fun getStatus(d: DataGame): Int {
        return with(d) {
            when {
                abs(countX - countO) > 1 -> 1                     // Impossible
                checkX > 0 && checkO > 0 -> 1                        // Impossible
                checkX == 0 && checkO == 0 && countFree > 0 -> 0     // Game not finished
                checkX == 0 && checkO == 0 && countFree == 0 -> 2    // Draw
                checkX == 1 && checkO == 0 -> 3                      // X wins
                checkX == 0 && checkO == 1 -> 4                      // O wins
                else -> 5                                            // Incorrect input
            }
        }
    }

    fun getStatus() = getStatus(dataGame)

    fun getStatusLabeled(): String = statusGameLabels[getStatus()]

    fun printGame() {
        with(dataGame) {
            println("---------\n" +
                    "| ${str[0]} ${str[1]} ${str[2]} |\n" +
                    "| ${str[3]} ${str[4]} ${str[5]} |\n" +
                    "| ${str[6]} ${str[7]} ${str[8]} |\n" +
                    "---------")
        }
    }

    private fun getNumberFromXY(point: Pair<Int?, Int?>) =
            (point.first!! - 1) * 3 + point.second!! - 1

    private fun getPosition(s: String): Pair<Int?, Int?> =
            s.split(" ").map(String::toIntOrNull).let { it[0] to it[1] }

    fun getStatusPosition(s: String): Int {
        val (x, y) = getPosition(s)
        if (x == null || y == null) return 1                          // You should enter numbers!
        if (x !in 1..3 || y !in 1..3) return 2                        // Coordinates should be from 1 to 3!
        if (dataGame.str[getNumberFromXY(x to y)] != ' ') return 3    // This cell is occupied! Choose another one!
        return 0                                                      // Correct
    }

    fun getStatusPositionLabeled(s: String): String {
        return statusPositionLabeled[getStatusPosition(s)]
    }

    fun setPosition(s: String) {
        val n = getNumberFromXY(getPosition(s))
        val index = dataGame.data.indexOfFirst { it == -1 }
        dataGame.data[index] = n
        dataGame = stringToData(dataToString(dataGame))
    }
}

fun main() {
    val t = TicTacToe()
    t.printGame()
    do {
        do {
            print("Enter the coordinates: ")
            val s = readLine()!!
            if (t.getStatusPosition(s) != 0) {                             // it != Correct
                println(t.getStatusPositionLabeled(s))
            } else {
                t.setPosition(s)
                t.printGame()
                break
            }
        } while (t.getStatusPosition(s) != 0)
    } while (t.getStatus() == 0)
    println(t.getStatusLabeled())
}