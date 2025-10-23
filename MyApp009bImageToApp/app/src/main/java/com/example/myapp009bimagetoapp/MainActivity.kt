package com.example.myapp009bimagetoapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp009bimagetoapp.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var originalBitmap: Bitmap? = null
    private val imageViews = mutableListOf<ImageView>()
    private val tileBitmaps = mutableListOf<Bitmap?>() // Store all tile bitmaps including null for empty
    private val currentState = mutableListOf<Int>() // Current arrangement of tiles (indices to tileBitmaps)
    private val gridSize = 3
    private var moveCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                loadImageAndCreatePuzzle(it)
            }
        }

        binding.btnTakeImage.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btnShuffle.setOnClickListener {
            if (tileBitmaps.isNotEmpty()) {
                shufflePuzzle()
            } else {
                Toast.makeText(this, "Please select an image first!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadImageAndCreatePuzzle(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            originalBitmap?.let { bitmap ->
                binding.gridPuzzle.post {
                    createPuzzle(bitmap)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun createPuzzle(bitmap: Bitmap) {
        // Clear previous data
        tileBitmaps.clear()
        currentState.clear()
        imageViews.clear()
        binding.gridPuzzle.removeAllViews()
        moveCount = 0
        updateMoveCounter()

        // Make bitmap square
        val size = minOf(bitmap.width, bitmap.height)
        val squareBitmap = Bitmap.createBitmap(
            bitmap,
            (bitmap.width - size) / 2,
            (bitmap.height - size) / 2,
            size,
            size
        )

        val tileSize = size / gridSize

        // Calculate tile size for display
        val gridWidth = binding.gridPuzzle.width
        val gridHeight = binding.gridPuzzle.height
        val availableSize = minOf(gridWidth, gridHeight)
        val tileSizeInGrid = availableSize / gridSize

        // Create tile bitmaps
        for (i in 0 until gridSize * gridSize) {
            val row = i / gridSize
            val col = i % gridSize

            if (i == gridSize * gridSize - 1) {
                // Last tile is empty
                tileBitmaps.add(null)
            } else {
                val tileBitmap = Bitmap.createBitmap(
                    squareBitmap,
                    col * tileSize,
                    row * tileSize,
                    tileSize,
                    tileSize
                )
                tileBitmaps.add(tileBitmap)
            }
            currentState.add(i) // Initially in correct order
        }

        // Create ImageViews in grid
        for (i in 0 until gridSize * gridSize) {
            val row = i / gridSize
            val col = i % gridSize

            val imageView = ImageView(this)

            val params = android.widget.GridLayout.LayoutParams()
            params.width = tileSizeInGrid - 4
            params.height = tileSizeInGrid - 4
            params.rowSpec = android.widget.GridLayout.spec(row)
            params.columnSpec = android.widget.GridLayout.spec(col)
            params.setMargins(2, 2, 2, 2)

            imageView.layoutParams = params
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.tag = i // Store position

            imageView.setOnClickListener {
                onTileClicked(i)
            }

            imageViews.add(imageView)
            binding.gridPuzzle.addView(imageView)
        }

        // Update display
        updateDisplay()

        // Shuffle after a short delay
        binding.gridPuzzle.postDelayed({
            shufflePuzzle()
        }, 200)
    }

    private fun updateDisplay() {
        for (i in currentState.indices) {
            val tileIndex = currentState[i]
            val bitmap = tileBitmaps[tileIndex]

            if (bitmap != null) {
                imageViews[i].setImageBitmap(bitmap)
                imageViews[i].setBackgroundColor(Color.WHITE)
            } else {
                imageViews[i].setImageBitmap(null)
                imageViews[i].setBackgroundColor(Color.DKGRAY)
            }
        }
    }

    private fun onTileClicked(position: Int) {
        val emptyPos = currentState.indexOf(gridSize * gridSize - 1) // Find empty tile position

        val tileRow = position / gridSize
        val tileCol = position % gridSize
        val emptyRow = emptyPos / gridSize
        val emptyCol = emptyPos % gridSize

        val isAdjacent = (tileRow == emptyRow && Math.abs(tileCol - emptyCol) == 1) ||
                (tileCol == emptyCol && Math.abs(tileRow - emptyRow) == 1)

        if (isAdjacent) {
            // Swap tiles in current state
            val temp = currentState[position]
            currentState[position] = currentState[emptyPos]
            currentState[emptyPos] = temp

            updateDisplay()
            moveCount++
            updateMoveCounter()
            checkWin()
        }
    }

    private fun shufflePuzzle() {
        moveCount = 0
        updateMoveCounter()

        // Perform random valid moves to shuffle
        val moves = 200
        for (i in 0 until moves) {
            val emptyPos = currentState.indexOf(gridSize * gridSize - 1)
            val validMoves = getValidMoves(emptyPos)

            if (validMoves.isNotEmpty()) {
                val randomMove = validMoves.random()

                // Swap
                val temp = currentState[emptyPos]
                currentState[emptyPos] = currentState[randomMove]
                currentState[randomMove] = temp
            }
        }

        updateDisplay()
    }

    private fun getValidMoves(position: Int): List<Int> {
        val row = position / gridSize
        val col = position % gridSize
        val validMoves = mutableListOf<Int>()

        if (row > 0) validMoves.add(position - gridSize) // Up
        if (row < gridSize - 1) validMoves.add(position + gridSize) // Down
        if (col > 0) validMoves.add(position - 1) // Left
        if (col < gridSize - 1) validMoves.add(position + 1) // Right

        return validMoves
    }

    private fun checkWin() {
        // Check if current state matches the solved state (0,1,2,3,4,5,6,7,8)
        val isSolved = currentState.indices.all { currentState[it] == it }

        if (isSolved) {
            Toast.makeText(this, "🎉 Congratulations! You solved it in $moveCount moves!", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateMoveCounter() {
        binding.tvMoves.text = "Moves: $moveCount"
    }
}