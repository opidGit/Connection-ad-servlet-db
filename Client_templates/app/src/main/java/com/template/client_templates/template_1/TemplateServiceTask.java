package com.template.client_templates.template_1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by yeongjun-lab on 2014-11-03.
 */
class TemplateServiceTask extends AsyncTask<ArrayList<String>, Object, ArrayList<String>> {
    final String APP_TAG = "TemplateServiceTask";
    private String scheme = "http";
    private String host = "203.247.240.62";
    private int port = 80;
    private String path = "server_template/GetData";

    private Context mContext = null;
    private TemplateServiceListener mlistener = null;

    TemplateServiceTask(Context context, TemplateServiceListener listener) {
        this.mContext = context;
        this.mlistener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        /* 2. Call requestGetHttp */
        return requestGetHttp(params[0]);
    }


    /**
     * **********************************
     * GET 방식 사용 *
     * ***********************************
     */
    private ArrayList<String> requestGetHttp(ArrayList<String> parameters) {
        URI uri = null;
        HttpGet httpGet = null;
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); // http parameter
        ArrayList<String> result = null; // return value


        try {
            ////////////////////////////////////////////////////////////////////
            // Add parameters
            nameValuePairs.add(new BasicNameValuePair("data", parameters.get(0)));
            String qParams = URLEncodedUtils.format(nameValuePairs, "UTF-8");

            ////////////////////////////////////////////////////////////////////
            // set timeout
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000); // 서버가 응답하는 시간 한도
            HttpConnectionParams.setSoTimeout(params, 5000); // 서버가 응답하지 않는 경우

            uri = URIUtils.createURI(scheme, host, port, path, qParams, null);
            httpGet = new HttpGet(uri);

            ////////////////////////////////////////////////////////////////////
            // sending and receive
            response = client.execute(httpGet); // 발신

            HttpEntity httpEntity = response.getEntity(); // 수신
            String output = EntityUtils.toString(httpEntity);

            ////////////////////////////////////////////////////////////////////
            // Parses the results.
            // ArrayList<String> results = resultParsing(output);
            result = new ArrayList<String>();
            result.add(output.toString());

            ////////////////////////////////////////////////////////////////////
            // Check result status of the request.
            if (response.getStatusLine().getStatusCode() == 200) {
                Log.d(APP_TAG, "HTTP GET succeeded");

                StringBuilder strBuilder = new StringBuilder();
                HttpEntity messageEntity = response.getEntity();
                InputStream is = messageEntity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    strBuilder.append(line);
                }
                Log.d(APP_TAG, strBuilder.toString());
            }

        } catch (URISyntaxException e) {
            Log.e("URISyntaxException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return result;

    }


    /**
     * **********************************
     * POST 방식 사용 *
     * ***********************************
     */
    private String requestHttpPOST(ArrayList<String> parameters) {
        final String TAG = "REQUEST_POST_HTTP";

        InputStream inputStream = null;
        URI uri = null;
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();

        try {
            uri = URIUtils.createURI(scheme, host, port, path, null, null);

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(uri);

            String json = "";
            JSONObject jsonObject = new JSONObject();


            ///////////////////////////////////////////////////////////////////////////////////////
            // 사용자 파라미터 추가하는 부분
            jsonObject.put("key", 1);
            //jsonObject.accumulate("key", parameters.get(0));

            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpClient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";


        } catch (Exception e) {
            Log.d("HttpPost", e.getLocalizedMessage());
        }

        return result;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////


    /* 3. Return to Activity. */
    @Override
    protected void onPostExecute(ArrayList<String> result) {
        // It returns the results to the Activity that called this class.
        mlistener.onTemplateActionComplete(result);
    }

}
