package com.pzk.studentmenadment;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    EditText ime, indeks, sifraPredmeta, kolokvijum, brojPoena;
    Button dodaj, pregledSve, uputstvo, izbrisi, pregled, izbrisiSve;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ime = (EditText) findViewById(R.id.ime);
        indeks = (EditText) findViewById(R.id.indeks);
        sifraPredmeta = (EditText) findViewById(R.id.sifraPredmeta);
        kolokvijum = (EditText) findViewById(R.id.kolokvijum);
        brojPoena = (EditText) findViewById(R.id.brojPoena);
        dodaj = (Button) findViewById(R.id.dodaj);
        pregled=(Button)findViewById(R.id.pregled);
        pregledSve = (Button) findViewById(R.id.pregledSve);
        izbrisi = (Button) findViewById(R.id.izbrisi);
        uputstvo = (Button) findViewById(R.id.uputstvo);
        izbrisiSve = (Button) findViewById(R.id.izbrisiSve);


        db = openOrCreateDatabase("Student_manage", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(indeks VARCHAR,ime VARCHAR,sifraPredmeta VARCHAR,kolokvijum VARCHAR,brojPoena VARCHAR);");


        dodaj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (indeks.getText().toString().trim().length() == 0 ||
                        ime.getText().toString().trim().length() == 0 ||
                        sifraPredmeta.getText().toString().trim().length() == 0 ||
                        kolokvijum.getText().toString().trim().length() == 0 ||
                        brojPoena.getText().toString().trim().length() == 0 ) {
                    showMessage("Greška", "Molimo Vas da unesete sve vrijednosti");
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('" + indeks.getText() + "','" + ime.getText() +
                        "','" + sifraPredmeta.getText() + "','" + kolokvijum.getText() + "','" + brojPoena.getText() + "')");
                showMessage("Gotovo", "Uspješno dodato");
                clearText();
            }
        });
        izbrisi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (indeks.getText().toString().trim().length() == 0 ||
                sifraPredmeta.getText().toString().trim().length()== 0 ||
                kolokvijum.getText().toString().trim().length()== 0) {
                    showMessage("Greška", "Molimo Vas da unesete indeks, šifru predmeta i kolokvijum");
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE indeks='" + indeks.getText() + "'AND sifraPredmeta='"+sifraPredmeta.getText()+"'AND kolokvijum='"+kolokvijum.getText()+"'",
                        null);
                if (c.moveToFirst()) {
                    db.execSQL("DELETE FROM student WHERE indeks='" + indeks.getText() + "'AND sifraPredmeta='"+sifraPredmeta.getText()+"'AND kolokvijum='"+kolokvijum.getText()+"'");
                    showMessage("Gotovo", "Uspješno izbrisano");
                } else {
                    showMessage("Greška", "Nepostojeći unos");
                }
                clearText();
            }
        });
        pregled.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (indeks.getText().toString().trim().length() == 0 ||
                        sifraPredmeta.getText().toString().trim().length()== 0 ||
                        kolokvijum.getText().toString().trim().length()== 0)
                {
                    showMessage("Greška", "Molimo Vas da unesete indeks, šifru predmeta i kolokvijum");
                    return;
                }
                Cursor c=db.rawQuery("SELECT * FROM student WHERE indeks='" + indeks.getText() + "'AND sifraPredmeta='"+sifraPredmeta.getText()+"'AND kolokvijum='"+kolokvijum.getText()+"'",
                        null);
                if(c.moveToFirst())

                {
                    ime.setText(c.getString(1));
                    sifraPredmeta.setText(c.getString(2));
                    brojPoena.setText(c.getString(4));
                }
                else
                {
                    showMessage("Greška", "Nepostojeći unos");
                    clearText();
                }
            }
        });
        pregledSve.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Cursor c = db.rawQuery("SELECT * FROM student", null);
                if (c.getCount() == 0) {
                    showMessage("Greška", "Niste unijeli nijedan podatak");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
                    buffer.append("Indeks: " + c.getString(0) + "\n");
                    buffer.append("Ime: " + c.getString(1) + "\n");
                    buffer.append("Šifra predmeta: " + c.getString(2) + "\n");
                    buffer.append("Kolokvijum: " + c.getString(3) + "\n");
                    buffer.append("Broj poena: " + c.getString(4) + "\n");
                    buffer.append("__________________________________"+"\n\n");
                }
                showMessage("Detalji o studentu", buffer.toString());
            }
        });
        izbrisiSve.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Cursor c = db.rawQuery("DELETE FROM student", null);
                if (c.getCount() == 0) {
                    showMessage("Gotovo", "Uspješno izbrisano");
                    clearText();
                }
            }

        });
        uputstvo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showMessage("Aplikacija za menadžment studenata", "Prvo popunjavate polja, pa onda birate što želite od ponuđenih dugmadi." +"\n"+
                        "Polja indeks, šifra predmeta i kolokvijum su obavezni u slučaju pretrage i brisanja, jer su oni jedinstveni za studenta.");
            }
        });

    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        indeks.setText("");
        ime.setText("");
        sifraPredmeta.setText("");
        kolokvijum.setText("");
        brojPoena.setText("");
        indeks.requestFocus();
    }
}

