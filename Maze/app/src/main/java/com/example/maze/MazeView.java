package com.example.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import androidx.annotation.Nullable;

import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MazeView extends View {
    Maze maze;
    private Paint wallPaint, adventurerPaint, exitPaint, minotaurPaint;

    private float cellSize, hMargin, vMargin;

    public MazeView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        maze = new MonsterModeMaze();

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(Maze.WALL_THICKNESS);

        adventurerPaint = new Paint();
        adventurerPaint.setColor(Color.BLUE);

        exitPaint = new Paint();
        exitPaint.setColor(Color.BLACK);

        minotaurPaint = new Paint();
        minotaurPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height = getHeight();

        // Decide the size of cells based on the screen.
        if (width / height < Maze.COLS / Maze.ROWS)
            cellSize = width / (Maze.COLS + 1);
        else
            cellSize = height / (Maze.ROWS + 1);

        hMargin = (width - Maze.COLS * cellSize) / 2;
        vMargin = (height - Maze.ROWS * cellSize) / 2;

        // Draw the walls.

        // Translate the origin from (0, 0) to (hMargin, vMargin).
        canvas.translate(hMargin, vMargin);

        for (int x = 0; x < Maze.COLS; x++)
        {
            for (int y = 0; y < Maze.ROWS; y++)
            {
                if (maze.cells[x][y].topWall)
                    canvas.drawLine(x * cellSize, y * cellSize,
                            (x + 1) * cellSize, y * cellSize,
                            wallPaint);
                if (maze.cells[x][y].bottomWall)
                    canvas.drawLine(x * cellSize, (y + 1) * cellSize,
                            (x + 1) * cellSize, (y + 1) * cellSize,
                            wallPaint);
                if (maze.cells[x][y].leftWall)
                    canvas.drawLine(x * cellSize, y * cellSize,
                            x * cellSize, (y + 1) * cellSize,
                            wallPaint);
                if (maze.cells[x][y].rightWall)
                    canvas.drawLine((x + 1) * cellSize, y * cellSize,
                            (x + 1) * cellSize, (y + 1) * cellSize,
                            wallPaint);
            }
        }

        // Draw adventurer and exit squares.

        float margin = cellSize / 7;

        canvas.drawRect(maze.adventurer.col * cellSize + margin,
                maze.adventurer.row * cellSize + margin,
                (maze.adventurer.col + 1) * cellSize - margin,
                (maze.adventurer.row + 1) * cellSize - margin,
                adventurerPaint);

        canvas.drawRect(maze.exit.col * cellSize + margin,
                maze.exit.row * cellSize + margin,
                (maze.exit.col + 1) * cellSize - margin,
                (maze.exit.row + 1) * cellSize - margin,
                exitPaint);

        if (ModeActivity.modeString.equals("MonsterMode")) {
            monsterModeDraw(canvas, margin);
        } else {
            System.out.println("Error: No such maze mode");
        }
    }

    private void monsterModeDraw(Canvas canvas, float margin) {
        MonsterModeMaze monsterModeMaze = (MonsterModeMaze) maze;
        for (int i = 0; i < monsterModeMaze.numberOfMonsters; i++) {
            canvas.drawRect(monsterModeMaze.monsters[i].col * cellSize + margin,
                    monsterModeMaze.monsters[i].row * cellSize + margin,
                    (monsterModeMaze.monsters[i].col + 1) * cellSize - margin,
                    (monsterModeMaze.monsters[i].row + 1) * cellSize - margin,
                    minotaurPaint);
        }
    }

    void updateMaze(int direction) {
        maze.moveAdventurer(direction);
        invalidate();
    }
}