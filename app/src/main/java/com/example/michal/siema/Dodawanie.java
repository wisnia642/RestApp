package com.example.michal.siema;import android.content.Intent;import android.database.Cursor;import android.database.sqlite.SQLiteDatabase;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.drawable.BitmapDrawable;import android.net.Uri;import android.os.Bundle;import android.os.Environment;import android.os.StrictMode;import android.provider.MediaStore;import android.support.v7.app.ActionBarActivity;import android.view.View;import android.widget.AdapterView;import android.widget.ArrayAdapter;import android.widget.Button;import android.widget.EditText;import android.widget.ImageView;import android.widget.Spinner;import android.widget.TextView;import android.widget.Toast;import java.io.ByteArrayOutputStream;import java.io.File;import java.io.FileInputStream;import java.io.FileNotFoundException;import java.io.FileOutputStream;import java.io.IOException;import java.sql.Connection;import java.sql.DriverManager;import java.sql.PreparedStatement;import java.sql.ResultSet;import java.sql.SQLException;import java.sql.Statement;import java.util.Arrays;import java.util.regex.Matcher;import java.util.regex.Pattern;public class Dodawanie extends ActionBarActivity {    Connection connection = null;    int baza=0;    Statement st;    PreparedStatement ps;    ResultSet rs;    String kategoria = null;    String skladniki = null;    String cena = null;    String nazwa = null;    String Sposob_przygotowania=null;    String zdjecie = "";    String sql,email;    String sklad="";    String skladniki1[];    String gdzie;    private static final String url="jdbc:mysql://192.168.1.100:3306/restalracja1234";    private static final String user="michal";    private static final String pass="kaseta12";    private static final String SAMPLE_DB_NAME = "Restalracja";    private static final int CAMERA_PIC_REQUEST = 1111;    TextView text,txt;    Button button1, button2, button3, button4;    EditText text1, text2, text3, text4;    ImageView Obraz;    Bitmap thumbnail;    Bundle applesData;    Intent intent;    String posilek;    FileInputStream fis = null;    File file;    Spinner spnr;    String[] celebrities = {            "Przystawki", "Makarony", "Ryba", "Salatki", "Fast_Food", "Pizza", "Zupy",            "Suszi", "Wina", "Piwo", "Desery", "Dodatki",            "Napoje_Gazowane", "Napoje_Zimne", "Napje_Gorace", "Soki"};    private void showToast(String message) {        Toast.makeText(getApplicationContext(),                message,                Toast.LENGTH_LONG).show();    }    public void connect() {        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();        StrictMode.setThreadPolicy(policy);        try {            Class.forName("com.mysql.jdbc.Driver");        } catch (ClassNotFoundException e) {            return;        }        try {            connection = DriverManager.getConnection(url,user,pass);        } catch (SQLException e) {            showToast("Brak polaczenia z internetem");            return;        }    }    public void SaveSqlLigt()    {        try {            SQLiteDatabase sampleDB = this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);            if(baza==1){                nazwa = text1.getText().toString();                cena = text3.getText().toString();                Sposob_przygotowania = text4.getText().toString();                sampleDB.execSQL("INSERT INTO " + kategoria + " (Nazwa,Skladniki,Skladniki1,Sposob_przygotowania,Zdjecie,Cena) VALUES ('"+nazwa+"','"+email+"','"+sklad+"','"+Sposob_przygotowania+"','"+zdjecie+"','"+cena+"') ");            }            if(baza==2)            {                nazwa = text1.getText().toString();                sampleDB.execSQL("DELETE FROM " + kategoria + " WHERE Nazwa =('" + nazwa + "') ");            }            if(baza==4)            {                sampleDB.execSQL("DELETE FROM " + kategoria + " WHERE Nazwa =('" + nazwa + "') ");                nazwa = text1.getText().toString();                Sposob_przygotowania = text4.getText().toString();                cena = text3.getText().toString();                sampleDB.execSQL("INSERT INTO " + kategoria + " (Nazwa,Skladniki,Skladniki1,Sposob_przygotowania,Zdjecie,Cena) VALUES ('"+nazwa+"','"+email+"','"+sklad+"','"+Sposob_przygotowania+"','"+zdjecie+"','"+cena+"') ");            }            sampleDB.close();        }catch (Exception e){showToast("Blad w sqlLight");}    }    public void connectToDataBase() {        connect();        if (connection != null) {            try {                st = connection.createStatement();            } catch (SQLException e1) {                //e1.printStackTrace();            }            cena = text3.getText().toString();            Sposob_przygotowania = text4.getText().toString();            if(baza==1) {                nazwa = text1.getText().toString();             //   sql = +                 //       "VALUES ('" + nazwa + "', '" + skladniki + "', '" + cena + "', '" + (InputStream)fis + "')";                sql = "INSERT INTO " + kategoria + " (Nazwa,Skladniki,Skladniki1,Sposob_przygotowania,Cena,Zdjecie) VALUES (?,?,?,?,?,?) ";                try {                    connection.setAutoCommit(false);                    File file =new File(zdjecie);                    try {                        fis = new FileInputStream(file);                    } catch (FileNotFoundException e) {                    }                    ps = connection.prepareStatement(sql);                    ps.setString(1,nazwa);                    ps.setString(2,email);                    ps.setString(3,sklad);                    ps.setString(4,Sposob_przygotowania);                    ps.setString(5,cena);                    ps.setBinaryStream(6, fis,(int) file.length());                    ps.executeUpdate();                    connection.commit();                } catch (SQLException e) {                }                finally {                    try {                        ps.close();                    } catch (SQLException e) {                    }                    try {                        fis.close();                    } catch (IOException e) {                    }                }            }            //usuwanie określonej pozycji z menu            if(baza==2) {                nazwa = text1.getText().toString();                sql ="DELETE FROM " + kategoria + " WHERE Nazwa =('" + nazwa + "')";                try {                    st.executeUpdate(sql);                } catch (SQLException e1) {                    e1.printStackTrace();                }            }            //zamiana okreslonej pozycji z menu            if(baza==4){                sql ="DELETE FROM " + kategoria + " WHERE Nazwa =('" + nazwa + "')";                try {                    st.executeUpdate(sql);                } catch (SQLException e1) {                    e1.printStackTrace();                }            }        }        try {            if (connection != null)                connection.close();        } catch (SQLException se) {            showToast("brak połączenia z internetem");        }    }    private boolean isValidEmail(String email){        //jestem świetny :)        String Product_Patern = "^([_A-Za-z0-9-\\+]*-" + "[0-99999]*,)*";        Pattern pattern = Pattern.compile(Product_Patern);        Matcher matcher = pattern.matcher(email);        return matcher.matches();    }    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_dodawanie);        button1 = (Button) findViewById(R.id.but1);        button2 = (Button) findViewById(R.id.but2);        button3 = (Button) findViewById(R.id.but3);        button4 = (Button) findViewById(R.id.but4);        text = (TextView) findViewById(R.id.textView21);        txt = (TextView) findViewById(R.id.textView16);        text1 = (EditText) findViewById(R.id.editTextN);        text2 = (EditText) findViewById(R.id.editTextS);        text3 = (EditText) findViewById(R.id.editTextC);        text4 = (EditText) findViewById(R.id.editText10);        Obraz = (ImageView) findViewById(R.id.imageView2);        applesData = getIntent().getExtras();        posilek = applesData.getString("warunek");        kategoria = applesData.getString("kategoria");        gdzie = applesData.getString("gdzie");        if(posilek.equals("false")) {            txt.setText("Dodaj do menu");            button3.setBackgroundResource(R.drawable.plus1);        }        if(posilek.equals("true")) {            nazwa = applesData.getString("nazwa");            cena = applesData.getString("cena");            zdjecie = applesData.getString("zdjecie");            skladniki = applesData.getString("skladniki");            Sposob_przygotowania = applesData.getString("Sposob_przygotowania");            button3.setBackgroundResource(R.drawable.minus1);            button1.setVisibility(View.GONE);            button2.setVisibility(View.GONE);            text.setVisibility(View.GONE);            txt.setText("Usuń z menu");            text1.setText(nazwa);            text2.setText(skladniki);            text3.setText(cena);            text4.setText(Sposob_przygotowania);            thumbnail = (BitmapFactory.decodeFile(zdjecie));            Obraz.setBackground(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 300, 250, true)));        }        if(posilek.equals("ttrue")) {            nazwa = applesData.getString("nazwa");            cena = applesData.getString("cena");            zdjecie = applesData.getString("zdjecie");            skladniki = applesData.getString("skladniki");            Sposob_przygotowania = applesData.getString("Sposob_przygotowania");            button3.setBackgroundResource(R.drawable.plus1);            txt.setText("Poprawka przepisu");            text1.setText(nazwa);            text2.setText(skladniki);            text3.setText(cena);            text4.setText(Sposob_przygotowania);            thumbnail = (BitmapFactory.decodeFile(zdjecie));            Obraz.setBackground(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 300, 250, true)));        }        spnr = (Spinner) findViewById(R.id.spinerdod);        ArrayAdapter<String> adapter = new ArrayAdapter<String>(                this, R.layout.simple_spinner_item, celebrities);        //obsługa listy ,dodawanie nowych logów        spnr.setAdapter(adapter);        int spinnerPostion = adapter.getPosition(kategoria);        spnr.setSelection(spinnerPostion);        spinnerPostion = 0;        spnr.setOnItemSelectedListener(                new AdapterView.OnItemSelectedListener() {                    @Override                    public void onItemSelected(AdapterView<?> arg0, View arg1,                                               int arg2, long arg3) {                        int position = spnr.getSelectedItemPosition();                        kategoria = celebrities[+position];                    }                    @Override                    public void onNothingSelected(AdapterView<?> arg0) {                    }                }        );        //wywołanie metody robienie zdjęcia i zapisywanie go        button1.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);                startActivityForResult(intent, CAMERA_PIC_REQUEST);            }        });        //wywołanie metody dodawania zdjęcia z galeri        button2.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);                startActivityForResult(intent, 2);            }        });        //wstawianie danych na serwer        button3.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if (posilek.equals("false")) {                    email = text2.getText().toString();                    skladniki1 = (email.split("[-0-99999+]+"));                    for (int i = 0; i < skladniki1.length; i = i + 0) {                        if (skladniki1[i] != null) {                            sklad = sklad + skladniki1[i];                        }                        i++;                    }                    if (!isValidEmail(email)) {                        text2.setError("Zły format skladnik : np: Czosnek-12,");                    } else {                        baza = 1;                        SaveSqlLigt();                        connectToDataBase();                        showToast("Produkt został Dodany");                        text1.setText("");                        text2.setText("");                        text3.setText("");                        text4.setText("");                        Obraz.setImageResource(android.R.color.transparent);                    }                }                if (posilek.equals("true")) {                    baza = 2;                    connectToDataBase();                    SaveSqlLigt();                    showToast("Produkt został Usuniety");                    Intent i = new Intent(Dodawanie.this, MainActivity.class);                    startActivity(i);                }                if (posilek.equals("ttrue")) {                    email = text2.getText().toString();                    skladniki1 = (email.split("[-0-99999+]+"));                    for (int i = 0; i < skladniki1.length; i = i + 0) {                        if (skladniki1[i] != null) {                            sklad = sklad + skladniki1[i];                        }                        i++;                    }                    if (!isValidEmail(email)) {                        text2.setError("Zły format skladnik : np: Czosnek-12,");                    } else {                        baza = 4;                        connectToDataBase();                        baza = 1;                        connectToDataBase();                        baza=4;                        SaveSqlLigt();                        showToast("Produkt został Zamieniony");                        Intent i = new Intent(Dodawanie.this, MainActivity.class);                        startActivity(i);                    }                }            }        });        //przejscie do następnego layoutu        button4.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if(gdzie.equals("true"))                {                    Intent i = new Intent(Dodawanie.this, Kuchnia.class);                    startActivity(i);                }                Intent i = new Intent(Dodawanie.this, MainActivity.class);                startActivity(i);            }        });    }    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        super.onActivityResult(requestCode, resultCode, data);        //Robienie zdjęcia i zapisywanie go        if (requestCode == CAMERA_PIC_REQUEST) {            //2            thumbnail = (Bitmap) data.getExtras().get("data");            Obraz.setBackground(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 300, 250, true)));            //3            ByteArrayOutputStream bytes = new ByteArrayOutputStream();            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);            //4            //nazwa = text1.getText().toString();            File file = new File(Environment.getExternalStorageDirectory() + File.separator + nazwa);            try {                file.createNewFile();                FileOutputStream fo = new FileOutputStream(file);                //5                fo.write(bytes.toByteArray());                fo.close();            } catch (IOException e) {                // TODO Auto-generated catch block                e.printStackTrace();            }            zdjecie=String.valueOf(file);        }        //pobieranie zdjęcia z galerii            else if (requestCode == 2) {                Uri selectedImage = data.getData();                String[] filePath = {MediaStore.Images.Media.DATA};                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);                c.moveToFirst();                int columnIndex = c.getColumnIndex(filePath[0]);                String picturePath = c.getString(columnIndex);                zdjecie=picturePath;                c.close();                thumbnail = (BitmapFactory.decodeFile(picturePath));                Obraz.setBackground(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(thumbnail, 300, 250, true)));            }        }    }