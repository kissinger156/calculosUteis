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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.severocarlos.recyclerview.ListCalcActivity;
import com.severocarlos.recyclerview.R;
import com.severocarlos.recyclerview.SqlHelper;

public class TmbActivity extends AppCompatActivity {

    private EditText editWeight;
    private EditText editHeight;
    private EditText editAge;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private double calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        editWeight = findViewById(R.id.edit_peso);
        editHeight = findViewById(R.id.edit_altura);
        editAge = findViewById(R.id.edit_anos);
        Button btnCalc = findViewById(R.id.button_calc);
        radioGroup = findViewById(R.id.radio_group);

        btnCalc.setOnClickListener(sendListener);

    }

    private View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!validate()) {
                Toast.makeText(TmbActivity.this, getString(R.string.verify),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String sWeight = editWeight.getText().toString();
            String sHeight = editHeight.getText().toString();
            String sAge = editAge.getText().toString();

            double weight = Double.parseDouble(sWeight);
            int height = Integer.parseInt(sHeight);
            int age = Integer.parseInt(sAge);

            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);

            if (radioButton.getText().toString().equals("HOMEM")) {
                calc = 66 + (13.8 * weight) + (5 * height) - (6.8 * age);
                alertDialog(getString(R.string.tmb_response, calc),
                        getString(R.string.tmb_message));
            } else {
                calc = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
                alertDialog(getString(R.string.tmb_response, calc),
                        getString(R.string.tmb_message));
            }

            InputMethodManager im =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(editAge.getWindowToken(), 0);
            im.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
            im.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
        }
    };

    public void checkButton(View v) { //metodo utilizado para receber informações do 'onClick'
        // dos radiosButton, eles precisam ter o mesmo nome na função OnClick
        int radioId = radioGroup.getCheckedRadioButtonId(); //recebendo o id do botao que está
        // selecionado, pelos testes, recebe a string
        radioButton = findViewById(radioId); //atribuindo o id direto recebido acima para o
        // radiobutton

        Toast.makeText(this, "item selecionado: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    public void alertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(TmbActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    SqlHelper helper = SqlHelper.getInstance(TmbActivity.this);
                    long calcId = helper.addItem(SqlHelper.TYPE_TMB, calc);

                    if (calcId > 0) {
                        Toast.makeText(TmbActivity.this, getString(R.string.calc_saved),
                                Toast.LENGTH_SHORT).show();
                        openListCalcActivity();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void openListCalcActivity() {
        Intent intent = new Intent(TmbActivity.this, ListCalcActivity.class);
        intent.putExtra("type", SqlHelper.TYPE_TMB);
        TmbActivity.this.startActivity(intent);
    }

    private boolean validate() {
        return !editWeight.getText().toString().startsWith("0")
                && !editHeight.getText().toString().startsWith("0")
                && !editAge.getText().toString().startsWith("0")
                && !editWeight.getText().toString().isEmpty()
                && !editHeight.getText().toString().isEmpty()
                && !editAge.getText().toString().isEmpty();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
