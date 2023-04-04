package step.learning.course;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalcActivity extends AppCompatActivity {

    private TextView tvHistory;
    private TextView tvResult;
    private String minusSign;
    private String zeroSymbol;
    private String decimalPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        tvHistory = findViewById( R.id.tv_history );
        tvResult = findViewById( R.id.tv_result );
        minusSign = getString( R.string.calc_minus_sign );
        zeroSymbol = getString( R.string.calc_btn_0_text );
        decimalPoint = getString( R.string.calc_decimal_point_sign );

        tvHistory.setText( "" );
        displayResult( "" );

        for (int i = 0; i < 10; i++ ) {
            @SuppressLint("DiscouragedApi")
            int buttonId = getResources().getIdentifier(
                    "calc_btn_" + i,
                    "id",
                    getPackageName()
            ) ;
            findViewById( buttonId ).setOnClickListener( this::digitClick );
        }

        findViewById( R.id.calc_btn_backspace ).setOnClickListener( this::backspaceClick );
        findViewById( R.id.calc_btn_plus_minus ).setOnClickListener( this::plusMinusClick );
        findViewById( R.id.calc_btn_comma ).setOnClickListener( this::decimalPointClick );
    }

    private void decimalPointClick( View view ) {
        String result = tvResult.getText().toString() ;
        if( !result.endsWith( decimalPoint ) && !result.contains( decimalPoint ) ) {
            result += decimalPoint;
        }
        displayResult( result );
    }
    private void plusMinusClick( View view ) {
        String result = tvResult.getText().toString() ;
        if( result.equals( zeroSymbol ) ) {
            return;
        }
        if (result.startsWith( minusSign )) {
            result = result.substring(1);
        }
        else {
            result = minusSign + result;
        };
        displayResult( result ) ;
    }

    private void backspaceClick( View view ) {
        String result = tvResult.getText().toString() ;
        int len = result.length();
        if( len > 0 ) {
            result = result.substring(0, len - 1);
        }
        displayResult( result ) ;
    }

    private void digitClick( View view ) {
        String result = tvResult.getText().toString() ;
        String digit = ( (Button) view ).getText().toString() ;
        final int maxDigits = 10;
        if( result.equals( zeroSymbol ) ) {
            result = "";
        }
        if( lengthOfDigits( result ) >= maxDigits ) {
            return;
        }
        result += digit ;
        displayResult( result ) ;
    }

    private int lengthOfDigits( String result ) {
        int count = result.length();
        if( count == 0 || result.equals( zeroSymbol ) ) {
            return 0;
        }
        String[] symbols = { decimalPoint, minusSign };
        for ( String sym: symbols ) {
            if( result.contains( sym ) ) {
                count = count - 1;
            }
        }
        return count;
    }

    private void displayResult( String result ) {
        if( "".equals( result ) || minusSign.equals( result ) ) {
            result = zeroSymbol;
        }
        tvResult.setText( result );
    }
}