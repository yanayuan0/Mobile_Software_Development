import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel

enum class Player{
    X, O;
}

data class BoardCell(val row: Int, val col: Int)

class TicTacToeModel : ViewModel() {

//    var points by mutableStateOf<List<Offset>>(emptyList())

    var board by mutableStateOf(Array(3) { Array(3) { null as Player? } })

    var currentPlayer by mutableStateOf(Player.X)

    fun onCellClicked(cell: BoardCell){
        if (board[cell.row][cell.col] == null){
//            val newBoard = board.copyOf()
//            newBoard[cell.row][cell.col] = currentPlayer
//            board = newBoard

            // better version with state hosting in the composable
            board[cell.row][cell.col] = currentPlayer
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        }
    }

    fun resetGame() {
        board = Array(3){ Array(3) {null as Player?} }
        currentPlayer = Player.X
    }

    fun checkWinner(): Player? {
        for (row in 0..2) {
            if (board[row][0] != null &&
                board[row][0] == board[row][1] &&
                board[row][1] == board[row][2]) {
                return board[row][0]
            }
        }

        for (col in 0..2) {
            if (board[0][col] != null &&
                board[0][col] == board[1][col] &&
                board[1][col] == board[2][col]) {
                return board[0][col]
            }
        }

        if (board[0][0] != null &&
            board[0][0] == board[1][1] &&
            board[1][1] == board[2][2]) {
            return board[0][0]
        }

        if (board[0][2] != null &&
            board[0][2] == board[1][1] &&
            board[1][1] == board[2][0]) {
            return board[0][2]
        }

        // No winner found
        return null
    }

}