package com.severocarlos.recyclerview;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListCalcActivity extends AppCompatActivity {
    private List<SqlHelper.Register> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc_imc);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("type");

            RecyclerView recyclerItemCalc = findViewById(R.id.recycler_view_list);

            SqlHelper db = SqlHelper.getInstance(this);
            datas.addAll(db.getRegisterBy(type));

            ListCalcAdapter adapter = new ListCalcAdapter(datas);
            recyclerItemCalc.setAdapter(adapter);

            recyclerItemCalc.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private class ListCalcAdapter extends RecyclerView.Adapter<ListCalcViewHolder> {
        private List<SqlHelper.Register> registers;


        ListCalcAdapter(List<SqlHelper.Register> datas) {
            this.registers = datas;
        }

        @NonNull
        @Override
        public ListCalcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent,
                    false);
            return new ListCalcViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListCalcViewHolder holder, int i) {
            SqlHelper.Register data = this.registers.get(i);
            holder.bind(data);
        }

        @Override
        public int getItemCount() {
            return registers.size();
        }

    }

    private static class ListCalcViewHolder extends RecyclerView.ViewHolder {

        ListCalcViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(SqlHelper.Register data) {
            ((TextView) itemView).setText(String.format(Locale.getDefault(), "Resultado: %.2f, " +
                    "Data: %s", data.response, data.createdDate));
        }
    }
}
