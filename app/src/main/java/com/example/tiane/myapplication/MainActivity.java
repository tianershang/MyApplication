package com.example.tiane.myapplication;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;





public class MainActivity extends AppCompatActivity {


    String myJSON;

    private  static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_PHO = "Phone";
    private static final String TAG_ADD = "address";
    private static final String TAG_BUL = "bulletin";

    JSONArray peoples = null;
    ArrayList<HashMap<String,String>> personList;
    ListView list;

   // private String Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData("http://192.168.1.12/PHP_connection.php");
        
    }
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String Phone = c.getString(TAG_PHO);
                String address = c.getString(TAG_ADD);
                String bulletin = c.getString(TAG_BUL);
               // String app = c.getString(TAG_ABC);



                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_ID, id);
                persons.put(TAG_PHO, Phone);
                persons.put(TAG_ADD, address);
                persons.put(TAG_BUL, bulletin);


                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, personList, R.layout.list_item,
                    new String[]{TAG_ID, TAG_PHO,TAG_ADD,TAG_BUL},

                    //new String[]{TAG_ID, TAG_NAME, TAG_ADD},
                    new int[]{R.id.id, R.id.Phone,R.id.address,R.id.bulletin}
                    //new int[]{R.id.id, R.id.name, R.id.address}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

}




