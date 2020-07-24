package com.severocarlos.recyclerview.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.severocarlos.recyclerview.ListCalcActivity;
import com.severocarlos.recyclerview.R;
import com.severocarlos.recyclerview.SqlHelper;

public class CfcActivity extends AppCompatActivity {

    private EditText editBpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfc);

        editBpm = findViewById(R.id.edit_batimentos);
        Button btnCalc = findViewById(R.id.button_calc);

        btnCalc.setOnClickListener(sendListener);
    }

    private View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!validate()) {
                alert(R.string.verify);
                return;
            }

            String valueS = editBpm.getText().toString();

            int value = Integer.parseInt(valueS);
            double cfc = calculateCfc(value);
            int resId = cfcResponse(cfc);

            AlertDialog alert = new AlertDialog.Builder(CfcActivity.this)
                    .setTitle(getString(R.string.cfc_response, cfc))
                    .setMessage(resId)
                    .setNegativeButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(getString(R.string.save), (dialog, which) -> {
                        SqlHelper helper = SqlHelper.getInstance(CfcActivity.this);
                        long calcId = helper.addItem(SqlHelper.TYPE_CFC, cfc);

                        if (calcId > 0) {
                            alert(R.string.cfc_response);
                            openListCalcActivity();
                        }
                    })
                    .create();
            alert.show();

            InputMethodManager im =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(editBpm.getWindowToken(), 0);
        }
    };

    private void alert(int resId) {
        Toast.makeText(CfcActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    private void openListCalcActivity() {
        Intent intent = new Intent(CfcActivity.this, ListCalcActivity.class);
        intent.putExtra("type", SqlHelper.TYPE_CFC);
        CfcActivity.this.startActivity(intent);
    }

    @StringRes
    private int cfcResponse(double cfc) {
        if (cfc < 56) {
            return R.string.fc_low;
        } else if (cfc >= 56 && cfc < 79) {
            return R.string.fc_normal;
        } else {
            return R.string.fc_high;
        }
    }

    private double calculateCfc(int value) {
        return value * 4;
    }

    private boolean validate() {
        return !editBpm.getText().toString().isEmpty()
                && !editBpm.getText().toString().startsWith("0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            openListCalcActivity();
        }
        return super.onOptionsItemSelected(item);
    }
}
