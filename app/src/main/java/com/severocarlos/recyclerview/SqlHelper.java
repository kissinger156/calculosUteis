package com.severocarlos.recyclerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "calculoSaudavel.db";
    private static final int DB_VERSION = 1;

    private static SqlHelper INSTANCE;
    public static final String TYPE_IMC = "imc";
    public static final String TYPE_CFC = "cfc";
    public static final String TYPE_TMB = "tmb";

    public static synchronized SqlHelper getInstance(Context context){

        if(INSTANCE == null) {
            INSTANCE = new SqlHelper(context.getApplicationContext());
        }
        return INSTANCE;

    }

    private SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE calc (id INTEGER PRIMARY KEY, type_calc TEXT, res DECIMAL, created_date DATETIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Register> getRegisterBy(String type) {
        List<Register> registers = new ArrayList<>(); // lista para armazenar os dados que serão consultados
        SQLiteDatabase db = getReadableDatabase(); // criando instância que irá ler o bd

        Cursor cursor = db.rawQuery("SELECT * FROM calc WHERE type_calc = ?", new String[]{type}); // query seria o comando para consulta ao
        //banco de dados, onde existe o SELECT * (tudo) from (da tabela) calc (nome da tabela) where (pegar os dados que contenham) type_calc = ?
        //(quero consultar os dados apenas que contenham o tipo passado como parâmetro na frente)

        try {
            if (cursor.moveToFirst()) { //indo para primeira posição do cursos

                do {
                    Register register = new Register(); //instanciando a classe criada lá em baixo com os atributos da lista que receberá os dados do banco dados
                    register.type = cursor.getString(cursor.getColumnIndex("type_calc")); //indo até a coluna type_calc e atribuindo os dados dela
                    //para o register.type;

                    register.response = cursor.getDouble(cursor.getColumnIndex("res")); //indo a coluna res e atribuindo os dados double para o
                    //atributo register.response;

                    register.createdDate = cursor.getString(cursor.getColumnIndex("created_date"));

                    registers.add(register);
                } while (cursor.moveToNext());

                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.e("Teste", e.getMessage(), e);
        } finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return registers;
    }

    public long addItem(String type, double response) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        long calcId = 0;

        try {

            ContentValues values = new ContentValues();
            
            values.put("type_calc", type);
            values.put("res", response);

            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm", new Locale("pt", "BR"));
            String now = format.format(Calendar.getInstance().getTime());
            values.put("created_date", now);

            calcId = db.insertOrThrow("calc", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Teste", e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        return calcId;
    }

    static class Register {
        String type;
        double response;
        String createdDate;
    }

}
