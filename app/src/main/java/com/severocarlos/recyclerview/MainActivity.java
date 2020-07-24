package com.severocarlos.recyclerview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.severocarlos.recyclerview.activitys.CfcActivity;
import com.severocarlos.recyclerview.activitys.ImcActivity;
import com.severocarlos.recyclerview.activitys.PdgActivity;
import com.severocarlos.recyclerview.activitys.TmbActivity;
import com.severocarlos.recyclerview.items.MainItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(1,getString(R.string.imc), R.drawable.muscle, 0XFF819BFF));
        mainItems.add(new MainItem(2, getString(R.string.tmb), R.drawable.metabolism, 0XFFACFF90));
        mainItems.add(new MainItem(3, getString(R.string.pdg), R.drawable.fat, 0XFFFFF689));
        mainItems.add(new MainItem(4, getString(R.string.cfc), R.drawable.heart, 0XFFFF87C3));


        RecyclerView mRecyclerView = findViewById(R.id.recycler_main);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        MainAdapter mAdapter = new MainAdapter(mainItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public class MainAdapter extends RecyclerView.Adapter<MainHolder> implements MainHolder.OnItemClickListener{
        private List<MainItem> mainItems;

        MainAdapter(ArrayList<MainItem> mainItems) {
            this.mainItems = mainItems;
        }

        @NonNull
        @Override
        public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainHolder(getLayoutInflater().inflate(R.layout.main_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainHolder holder, int position) {
            MainItem mainItem = mainItems.get(position);
            holder.bind(mainItem, this);
        }

        @Override
        public int getItemCount() {
            return mainItems.size();
        }

        @Override
        public void OnClick(int position) {
            MainItem mainItem = this.mainItems.get(position);
            switch (mainItem.getId()) {

                case 1: {
                    Intent intent = new Intent(MainActivity.this, ImcActivity.class);
                    startActivity(intent);
                } break;
                case 2: {
                    Intent intent = new Intent(MainActivity.this, TmbActivity.class);
                    startActivity(intent);
                } break;
                case 3: {
                    Intent intent = new Intent(MainActivity.this, PdgActivity.class);
                    startActivity(intent);
                } break;
                case 4: {
                    Intent intent = new Intent(MainActivity.this, CfcActivity.class);
                    startActivity(intent);
                } break;
            }
        }
    }

    static class MainHolder extends RecyclerView.ViewHolder {

        interface OnItemClickListener {
            void OnClick(int position);
        }

        private ImageView imgMain;
        private TextView textMain;

        MainHolder(@NonNull View itemView) {
            super(itemView);

            imgMain = itemView.findViewById(R.id.img_main);
            textMain = itemView.findViewById(R.id.text_title);
        }

        void bind(MainItem mainItem, final OnItemClickListener listener) {
            itemView.setBackgroundColor(mainItem.getColorValue());
            imgMain.setImageResource(mainItem.getImgResource());
            textMain.setText(mainItem.getName());

            itemView.setOnClickListener(v -> listener.OnClick(getAdapterPosition()));
        }
    }
}


