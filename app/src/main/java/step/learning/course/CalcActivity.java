package step.learning.course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalcActivity extends AppCompatActivity {

    private TextView tvHistory;
    private TextView tvResult;
    private String minusSign;
    private String zeroSymbol;
    private String decimalPoint;
    private boolean needClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        tvHistory = findViewById(R.id.tv_history);
        tvResult = findViewById(R.id.tv_result);
        minusSign = getString(R.string.calc_minus_sign);
        zeroSymbol = getString(R.string.calc_btn_0_text);
        decimalPoint = getString(R.string.calc_decimal_point_sign);

        tvHistory.setText("");
        displayResult("");

        for (int i = 0; i < 10; i++) {
            @SuppressLint("DiscouragedApi")
            int buttonId = getResources().getIdentifier(
                    "calc_btn_" + i,
                    "id",
                    getPackageName()
            );
            findViewById(buttonId).setOnClickListener(this::digitClick);
        }

        findViewById(R.id.calc_btn_backspace).setOnClickListener(this::backspaceClick);
        findViewById(R.id.calc_btn_plus_minus).setOnClickListener(this::plusMinusClick);
        findViewById(R.id.calc_btn_comma).setOnClickListener(this::decimalPointClick);
        findViewById(R.id.calc_btn_clear).setOnClickListener(this::clearClick);
        findViewById(R.id.calc_btn_ce).setOnClickListener(this::clearEditClick);
        findViewById(R.id.calc_btn_square).setOnClickListener(this::squareClick);
        findViewById(R.id.calc_btn_inverse).setOnClickListener(this::inverseClick);
        findViewById(R.id.calc_btn_root).setOnClickListener(this::sqrtClick);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savingState) {
        super.onSaveInstanceState(savingState);
        Log.d("CalcActivity", "onSaveInstanceState");
        System.out.println("i form onSaveInstanceState");
        savingState.putCharSequence("history", tvHistory.getText());
        savingState.putCharSequence("result", tvResult.getText());
        savingState.putBoolean("needClean", needClean);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        Log.d("CalcActivity", "onRestoreInstanceState");
        System.out.println("i form onRestoreInstanceState");
        tvHistory.setText(savedState.getCharSequence("history"));
        tvResult.setText(savedState.getCharSequence("result"));
        needClean = savedState.getBoolean("needClean", needClean);
    }

    private void sqrtClick(View view) {
        String result = tvResult.getText().toString();
        double arg;
        try {
            arg = Double.parseDouble(result
                    .replace(minusSign, "-")
                    .replaceAll(zeroSymbol, "0")
                    .replace(decimalPoint, ".")
            );
        } catch (NumberFormatException | NullPointerException ignored) {
            Toast.makeText(
                            this,
                            R.string.calc_error_parse,
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Vibrator vibrator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager)
                    getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        long[] vibratePattern = {0, 200, 100, 200};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                    VibrationEffect.createWaveform(
                            vibratePattern, -1
                    )
            );
        } else {
            vibrator.vibrate(vibratePattern, -1);
        }
//        vibrator.vibrate( 250 );
        /*
        vibrator.vibrate(
                VibrationEffect.createOneShot(
                        250, VibrationEffect.DEFAULT_AMPLITUDE
                )
        );*/
        /*
            Получение вибратора:
                        vibratorManager
            API >= 31 <
                        getSystemService

            Использование:
                        vibrator.vibrate( VibrationEffect.... )
            API >= 26 <
                        vibrator.vibrate()
        */
    }

    private void inverseClick(View view) {
        String result = tvResult.getText().toString();
        double arg;
        try {
            arg = Double.parseDouble(result
                    .replace(minusSign, "-")
                    .replaceAll(zeroSymbol, "0")
                    .replace(decimalPoint, ".")
            );
        } catch (NumberFormatException | NullPointerException ignored) {
            Toast.makeText(
                            this,
                            R.string.calc_error_parse,
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        tvHistory.setText(getString(R.string.calc_inverse_history, result));
        arg = 1 / arg;
        needClean = true;
        displayResult(arg);
    }

    private void squareClick(View view) {
        String result = tvResult.getText().toString();
        double arg;
        try {
            arg = Double.parseDouble(result
                    .replace(minusSign, "-")
                    .replaceAll(zeroSymbol, "0")
                    .replace(decimalPoint, ".")
            );
        } catch (NumberFormatException | NullPointerException ignored) {
            Toast.makeText(
                            this,
                            R.string.calc_error_parse,
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        tvHistory.setText(getString(R.string.calc_square_history, result));
        arg *= arg;
        needClean = true;
        displayResult(arg);
    }

    private void clearClick(View view) {
        clearAll();
    }

    private void clearEditClick(View view) {
        displayResult("");
    }

    private void decimalPointClick(View view) {
        String result = tvResult.getText().toString();
        if (!result.endsWith(decimalPoint) && !result.contains(decimalPoint)) {
            result += decimalPoint;
        }
        displayResult(result);
    }

    private void plusMinusClick(View view) {
        String result = tvResult.getText().toString();
        if (result.equals(zeroSymbol)) {
            return;
        }
        if (result.startsWith(minusSign)) {
            result = result.substring(1);
        } else {
            result = minusSign + result;
        }
        displayResult(result);
    }

    private void backspaceClick(View view) {
        String result = tvResult.getText().toString();
        int len = result.length();
        if (len > 0) {
            result = result.substring(0, len - 1);
        }
        if (needClean) {
            clearAll();
            return;
        }

        displayResult(result);
    }

    private void digitClick(View view) {
        String result = tvResult.getText().toString();
        String digit = ((Button) view).getText().toString();
        final int maxDigits = 9;
        if (result.equals(zeroSymbol) || needClean) {
            clearAll();
            result = "";
        }
        if (lengthOfDigits(result) >= maxDigits) {
            return;
        }
        result += digit;
        displayResult(result);
    }

    private int lengthOfDigits(String result) {
        int count = result.length();
        if (count == 0 || result.equals(zeroSymbol)) {
            return 0;
        }
        String[] symbols = {decimalPoint, minusSign};
        for (String sym : symbols) {
            if (result.contains(sym)) {
                count = count - 1;
            }
        }
        return count;
    }

    private void displayResult(String result) {
        if ("".equals(result) || minusSign.equals(result)) {
            result = zeroSymbol;
        }
        result = result
                .replace(".", decimalPoint);

        tvResult.setText(result);
    }

    private void displayResult(double arg) {
        long argInt = (long) arg;
        String result = argInt == arg ? "" + argInt : "" + arg;
        result = result
                .replace("-", minusSign)
                .replaceAll("0", zeroSymbol);
        displayResult(result);
    }

    private void clearAll() {
        needClean = false;
        tvHistory.setText("");
        displayResult("");
    }
}