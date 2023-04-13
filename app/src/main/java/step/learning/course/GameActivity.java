package step.learning.course;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private final int N = 4;
    private final int[][] cells = new int[N][N];
    private final TextView[][] tvCells = new TextView[N][N];

    private int score;
    private int bestScore;
    private TextView tvScore;
    private TextView tvBestScore;

    private Random random;

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
                                Toast.makeText(
                                                GameActivity.this,
                                                "Right",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onSwipeLeft() {
                                Toast.makeText(
                                                GameActivity.this,
                                                "Left",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onSwipeTop() {
                                Toast.makeText(
                                                GameActivity.this,
                                                "Top",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onSwipeBottom() {
                                Toast.makeText(
                                                GameActivity.this,
                                                "Bottom",
                                                Toast.LENGTH_SHORT)
                                        .show();
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
        spawnCell( 2 );
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
        }

        showField();
        return true;
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