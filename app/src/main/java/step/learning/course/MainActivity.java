package step.learning.course;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button del_button = findViewById( R.id.del_button );

        findViewById( R.id.button_calc ).setOnClickListener( this::buttonCalcClick );
        del_button.setOnClickListener( this::buttonDELClick );
    }

    private void buttonCalcClick( View view ) {

    }

    private void buttonADDClick( View view ) {
        TextView textHello = findViewById( R.id.text_hello );
        String txt = textHello.getText().toString();
        txt += "!" ;
        textHello.setText( txt );
    }
    private void buttonDELClick( View view ) {
        TextView textHello = findViewById( R.id.text_hello );
        String txt = textHello.getText().toString();
        if( txt.endsWith("!") ) {
            txt = txt.substring(0, txt.length() - 1);
            textHello.setText( txt );
        }
    }
}