package step.learning.course;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final int N = 4;
    private final int[][] cells = new int[N][N];
    private final int[][] saves = new int[N][N];
    private final TextView[][] tvCells = new TextView[N][N];

    private int score;
    private int bestScore;
    private TextView tvScore;
    private TextView tvBestScore;

    private Random random;

    private Animation spawnAnimation;
    private Animation collapseAnimation;

    @SuppressLint({"DiscouragedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvScore = findViewById(R.id.game_tv_score);
        tvBestScore = findViewById(R.id.game_tv_best_score);
        tvScore.setText(getString(R.string.game_score, "69.6k"));
        tvBestScore.setText(getString(R.string.game_best_score, "69.6k"));
        random = new Random();

        spawnAnimation = AnimationUtils.loadAnimation( this, R.anim.cell_spawn );
        collapseAnimation = AnimationUtils.loadAnimation( this, R.anim.cell_collapse );
        spawnAnimation.reset();
        collapseAnimation.reset();

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                tvCells[i][j] = findViewById(
                        getResources().getIdentifier(
                                "game_cell_" + i + j,
                                "id",
                                getPackageName()
                        )
                );
            }
        }
        findViewById(R.id.game_field)
                .setOnTouchListener(
                        new OnSwipeTouchListener(GameActivity.this) {
                            @Override
                            public void onSwipeRight() {
                                if( canMoveRight() ) {
                                    saveField();
                                    moveRight();
                                    spawnCell(1);
                                    showField();
                                } else {
                                    Toast.makeText(
                                                    GameActivity.this,
                                                    "No Right Move",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                            @Override
                            public void onSwipeLeft() {
                                if( canMoveLeft() ) {
                                    saveField();
                                    moveLeft();
                                    spawnCell(1);
                                    showField();
                                } else {
                                    Toast.makeText(
                                                    GameActivity.this,
                                                    "No Left Move",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                            @Override
                            public void onSwipeTop() {
                                if( canMoveTop() ) {
                                    saveField();
                                    moveTop();
                                    spawnCell(1);
                                    showField();
                                } else {
                                    Toast.makeText(
                                                    GameActivity.this,
                                                    "No Top Move",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                            @Override
                            public void onSwipeBottom() {
                                if( canMoveBottom() ) {
                                    saveField();
                                    moveBottom();
                                    spawnCell(1);
                                    showField();
                                } else {
                                    Toast.makeText(
                                                    GameActivity.this,
                                                    "No Bottom Move",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                );
        newGame();
        findViewById( R.id.new_game_btn ).setOnClickListener( this::newGameClick );
    }

    private void newGameClick(View view) {
        newGame();
    }

    /*
    for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {

                }
            }
     */
    private void newGame() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                cells[i][j] = 0;
            }
        }
        score = 0;
        spawnCell( 2 );
        showField();
    }
    private boolean canMoveRight() {
        for ( int i = 0; i < N; i++ ) {
            for ( int j = N-1; j > 0; j-- ) {
                if (cells[i][j] == 0
                        && cells[i][j - 1] != 0) {
                    return true;
                }
                if( cells[i][j] == cells[i][j-1] && cells[i][j] != 0 ) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean canMoveLeft() {
        for ( int i = 0; i < N; i++ ) {
            for ( int j = 0; j < N-1; j++ ) {
                if (cells[i][j] == 0
                        && cells[i][j + 1] != 0) {
                    return true;
                }
                if( cells[i][j] == cells[i][j+1] && cells[i][j] != 0 ) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean canMoveTop() {
        for ( int i = 0; i < N; i++ ) {
            for ( int j = 0; j < N-1; j++ ) {
                if (cells[j][i] == 0
                        && cells[j+1][i] != 0) {
                    return true;
                }
                if( cells[j][i] == cells[j+1][i] && cells[j][i] != 0 ) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean canMoveBottom() {
        for ( int i = 0; i < N; i++ ) {
            for ( int j = N-1; j > 0; j-- ) {
                if (cells[j][i] == 0
                        && cells[j-1][i] != 0) {
                    return true;
                }
                if( cells[j][i] == cells[j-1][i] && cells[j][i] != 0 ) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint("DiscouragedApi")
    private void showField() {
        Resources resources = getResources();
        String packageName = getPackageName();
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                tvCells[i][j].setText(String.valueOf(cells[i][j]));
                tvCells[i][j].setTextAppearance(
                        resources.getIdentifier(
                                "GameCell_" + cells[i][j],
                                "style",
                                packageName
                        )
                );
                tvCells[i][j].setBackgroundColor(
                        resources.getColor(
                                resources.getIdentifier(
                                        "game_bg_" + cells[i][j],
                                        "color",
                                        packageName
                                ),
                                getTheme()
                        )
                );
            }
        }
        tvScore.setText( getString( R.string.game_score, String.valueOf( score ) ) );
    }

    private boolean spawnCell( int count ) {
        List<Cord> cords = new ArrayList<>();
        for ( int i = 0; i < N; ++i ) {
            for ( int j = 0; j < N; ++j ) {
                if ( cells[i][j] == 0 ) {
                    cords.add( new Cord( i, j ) );
                }
            }
        }
        int cnt = cords.size();
        if ( cnt == 0 ) return false;
        else if( cnt < count ) return false;

        while ( count > 0 ) {
            int randIndex = random.nextInt( cnt );
            int x = cords.get( randIndex ).getX();
            int y = cords.get( randIndex ).getY();
            cells[x][y] = random.nextInt( 10 ) == 0 ? 4 : 2;
            cords.remove( randIndex );
            cnt--;
            count--;
            tvCells[x][y].startAnimation( collapseAnimation );
        }
        return true;
    }

    private boolean moveRight() {
        boolean isMoved = false;
        for ( int i = 0; i < N; i++ ) {
            boolean wasReplace;
            do {
                wasReplace = false;
                for ( int j = N-1; j > 0; j-- ) {
                    if( cells[i][j] == 0
                     && cells[i][j-1] != 0 ) {
                        cells[i][j] = cells[i][j-1];
                        cells[i][j-1] = 0;
                        wasReplace = true;
                        isMoved = true;
                    }
                }
            } while ( wasReplace );

            // collapse
            for ( int j = N-1; j > 0; j-- ) {
                if( cells[i][j] == cells[i][j-1] && cells[i][j] != 0 ) {  // [2222]
                    score +=  cells[i][j] + cells[i][j-1];
                    cells[i][j] *= -2;                                    // [2224]
                    cells[i][j-1] = 0;                                    // [2204] [2404] [2040]
                    isMoved = true;
                }
            }

            for ( int j = N-1; j > 0; j-- ) {
                if( cells[i][j] == 0
                 && cells[i][j-1] != 0 ) {
                    cells[i][j] = cells[i][j-1];
                    cells[i][j-1] = 0;
                }
            }

            for ( int j = N-1; j > 0; j-- ) {
                if( cells[i][j] < 0 ) {
                    cells[i][j] = -cells[i][j];
                    tvCells[i][j].startAnimation( collapseAnimation );
                }
            }
        }
        return isMoved;
    }
    private boolean moveLeft() {
        boolean isMoved = false;
        for ( int i = 0; i < N; i++ ) {
            boolean wasReplace;
            do {
                wasReplace = false;
                for ( int j = 0; j < N-1; j++ ) {
                    if( cells[i][j] == 0
                     && cells[i][j+1] != 0 ) {
                        cells[i][j] = cells[i][j+1];
                        cells[i][j+1] = 0;
                        wasReplace = true;
                        isMoved = true;
                    }
                }
            } while ( wasReplace );

            for ( int j = 0; j < N-1; j++ ) {
                if( cells[i][j] == cells[i][j+1] // [2222] [0020]
                 && cells[i][j] != 0 ) {
                    score +=  cells[i][j] + cells[i][j+1];
                    cells[i][j] *= -2;                                    // [2224]
                    cells[i][j+1] = 0;                                    // [2204] [2404] [2040]
                    isMoved = true;
                }
            }
            for ( int j = 0; j < N-1; j++ ) {
                if( cells[i][j] == 0
                        && cells[i][j+1] != 0 ) {
                    cells[i][j] = cells[i][j+1];
                    cells[i][j+1] = 0;
                }
            }
            for ( int j = 0; j < N-1; j++ ) {
                if( cells[i][j] < 0 ) {
                    cells[i][j] = -cells[i][j];
                    tvCells[i][j].startAnimation( collapseAnimation );
                }
            }
        }
        return isMoved;
    }
    private boolean moveTop() {
        boolean isMoved = false;
        for ( int i = 0; i < N; i++ ) {
            boolean wasReplace;
            do {
                wasReplace = false;
                for ( int j = 0; j < N-1; j++ ) {
                    if( cells[j][i] == 0
                            && cells[j+1][i] != 0 ) {
                        cells[j][i] = cells[j+1][i];
                        cells[j+1][i] = 0;
                        wasReplace = true;
                        isMoved = true;
                    }
                }
            } while ( wasReplace );

            for ( int j = 0; j < N-1; j++ ) {
                if( cells[j][i] == cells[j+1][i] // [2222] [0020]
                        && cells[j][i] != 0 ) {
                    score +=  cells[j][i] + cells[j+1][i];
                    cells[j][i] *= -2;                                    // [2224]
                    cells[j+1][i] = 0;                                    // [2204] [2404] [2040]
                    isMoved = true;
                }
            }
            for ( int j = 0; j < N-1; j++ ) {
                if( cells[j][i] == 0
                        && cells[j+1][i] != 0 ) {
                    cells[j][i] = cells[j+1][i];
                    cells[j+1][i] = 0;
                }
            }
            for ( int j = 0; j < N-1; j++ ) {
                if( cells[j][i] < 0 ) {
                    cells[j][i] = -cells[j][i];
                    tvCells[j][i].startAnimation( collapseAnimation );
                }
            }
        }
        return isMoved;
    }
    private boolean moveBottom() {
        boolean isMoved = false;
        for ( int i = 0; i < N; i++ ) {
            boolean wasReplace;
            do {
                wasReplace = false;
                for ( int j = N-1; j > 0; j-- ) {
                    if( cells[j][i] == 0
                     && cells[j-1][i] != 0 ) {
                        cells[j][i] = cells[j-1][i];
                        cells[j-1][i] = 0;
                        wasReplace = true;
                        isMoved = true;
                    }
                }
            } while ( wasReplace );

            for ( int j = N-1; j > 0; j-- ) {
                if( cells[j][i] == cells[j-1][i] // [2222] [0020]
                 && cells[j][i] != 0 ) {
                    score +=  cells[j][i] + cells[j-1][i];
                    cells[j][i] *= -2;                                    // [2224]
                    cells[j-1][i] = 0;                                    // [2204] [2404] [2040]
                    isMoved = true;
                }
            }
            for ( int j = N-1; j > 0; j-- ) {
                if( cells[j][i] == 0
                 && cells[j-1][i] != 0 ) {
                    cells[j][i] = cells[j-1][i];
                    cells[j-1][i] = 0;
                }
            }
            for ( int j = N-1; j > 0; j-- ) {
                if( cells[j][i] < 0 ) {
                    cells[j][i] = -cells[j][i];
                    tvCells[j][i].startAnimation( collapseAnimation );
                }
            }
        }
        return isMoved;
    }

    private void saveField() {
        for (int i = 0; i < N; i++) {
            System.arraycopy( cells[i], 0, saves[i], 0, N );
        }
    }
    private void undoMove() {
        for (int i = 0; i < N; i++) {
            System.arraycopy( saves[i], 0, cells[i], 0, N );
        }
    }

    private class Cord {
        private int x;
        private int y;

        public Cord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}