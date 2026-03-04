package com.example.lab2;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements InputFragment.OnInputSubmitListener, ResultFragment.OnResultCancelListener {

    private static final String TAG_INPUT = "TAG_INPUT";
    private static final String TAG_RESULT = "TAG_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.inputContainer, new InputFragment(), TAG_INPUT)
                    .commit();
        }
    }

    @Override
    public void onInputSubmitted(int selectedShapeId, double value,
                                 boolean areaChecked, boolean perimeterChecked) {
        Shape shape = Shape.fromRadioId(selectedShapeId);
        if (shape == null) return;

        String result = buildResult(shape, value, areaChecked, perimeterChecked);
        showResultFragment(result);
    }

    private String buildResult(@NonNull Shape shape, double value,
                               boolean areaChecked, boolean perimeterChecked) {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("Selected: " + shape.title);
        lines.add(shape.valueLabel + " = " + format(value));

        if (areaChecked) {
            lines.add("Area: " + format(shape.area(value)));
        }
        if (perimeterChecked) {
            lines.add("Perimeter: " + format(shape.perimeter(value)));
        }

        return TextUtils.join("\n", lines);
    }

    private String format(double x) {
        return String.format(Locale.US, "%.4f", x);
    }

    private void showResultFragment(String resultText) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.resultContainer, ResultFragment.newInstance(resultText), TAG_RESULT)
                .commit();
    }

    @Override
    public void onCancelResult() {
        // 1) Прибрати/сховати ResultFragment
        Fragment resultFragment = getSupportFragmentManager().findFragmentByTag(TAG_RESULT);
        if (resultFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(resultFragment)
                    .commit();
        }

        // 2) Очистити форму в InputFragment
        Fragment inputFragment = getSupportFragmentManager().findFragmentByTag(TAG_INPUT);
        if (inputFragment instanceof InputFragment) {
            ((InputFragment) inputFragment).clearForm();
        }
    }
}