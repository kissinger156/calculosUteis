package com.severocarlos.recyclerview.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.severocarlos.recyclerview.ListCalcActivity;
import com.severocarlos.recyclerview.R;
import com.severocarlos.recyclerview.SqlHelper;

public class ImcActivity extends AppCompatActivity {

    private EditText editPeso;
    private EditText editAltura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editPeso = findViewById(R.id.edit_peso);
        editAltura = findViewById(R.id.edit_altura);
        Button buttonCalc = findViewById(R.id.button_calc);

        buttonCalc.setOnClickListener(sendListener);
    }

    private View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!validate()) {
                alert(R.string.verify);
                return;
            }

            String sHeight = editAltura.getText().toString();
            String sWeight = editPeso.getText().toString();

            double weight = Double.parseDouble(sWeight);
            int height = Integer.parseInt(sHeight);
            double imc = calculateImc(weight, height);

            int resId = imcResponse(imc);

            AlertDialog alertDialog = new AlertDialog.Builder(ImcActivity.this)
                    .setTitle(getString(R.string.imc_response, imc))
                    .setMessage(resId)
                    .setNegativeButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(R.string.save, (dialog, which) -> {
                        SqlHelper helper = SqlHelper.getInstance(ImcActivity.this);
                        long calcId = helper.addItem(SqlHelper.TYPE_IMC, imc);

                        if (calcId > 0) {
                            alert(R.string.calc_saved);
                            openListCalcActivity();
                        }
                    })
                    .create();

            alertDialog.show();

            InputMethodManager im =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(editAltura.getWindowToken(), 0);
            im.hideSoftInputFromWindow(editPeso.getWindowToken(), 0);
        }
    };

    private void alert(int resId) {
        Toast.makeText(ImcActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    private void openListCalcActivity() {
        Intent intent = new Intent(ImcActivity.this, ListCalcActivity.class);
        intent.putExtra("type", SqlHelper.TYPE_IMC);
        ImcActivity.this.startActivity(intent);
    }

    @StringRes
    private int imcResponse(double imc) {
        if (imc < 15) {
            return R.string.imc_severely_low_weight;
        } else if (imc < 16) {
            return R.string.imc_very_low_weight;
        } else if (imc < 18.5) {
            return R.string.imc_low_weight;
        } else if (imc < 25) {
            return R.string.normal;
        } else if (imc < 30) {
            return R.string.imc_high_weight;
        } else if (imc < 35) {
            return R.string.imc_so_high_weight;
        } else if (imc < 40) {
            return R.string.imc_severely_high_weight;
        } else {
            return R.string.imc_extreme_weight;
        }
    }

    private double calculateImc(double weight, int height) {
        return weight / (((double) height / 100) * ((double) height / 100));
    }

    private boolean validate() {
        return !editPeso.getText().toString().startsWith("0")
                && !editAltura.getText().toString().startsWith("0")
                && !editPeso.getText().toString().isEmpty()
                && !editAltura.getText().toString().isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            openListCalcActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
