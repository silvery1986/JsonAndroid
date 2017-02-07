package com.example.jsonandroid;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;











import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.databean.Weatherinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
 //ģ�����Լ����Լ�����localhost�ˣ�������Ӧ��Ϊ10.0.2.2
 private static  String url="http://10.0.2.2:8080/JsonWeb/login.action?";
 private final String url_constant="http://10.0.2.2:8080/JsonWeb/login.action?";
 private EditText txUserName;
 private EditText txPassword;
 private Button btnLogin;
 private RequestQueue mQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	///��Android2.2�Ժ����������´���
		//��Ӧ�ò��õ�Android4.0
		//�����̵߳Ĳ���
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()   
         .detectDiskReads()   
         .detectDiskWrites()   
         .detectNetwork()   // or .detectAll() for all detectable problems   
         .penaltyLog()   
         .build());   
		//����������Ĳ���
		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()   
		         .detectLeakedSqlLiteObjects()   
		         //.detectLeakedClosableObjects()   
		         .penaltyLog()   
		         .penaltyDeath()   
		         .build());
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        //����ҳ�沼��
        setContentView(R.layout.main);
        //���ó�ʼ����ͼ
        initView();
        //�����¼�����������
        setListener();
    }
    
    /**
     * ������ʼ����ͼ�ķ���
     */
	private void initView() {
		btnLogin=(Button)findViewById(R.id.btnLogin);
		txUserName=(EditText)findViewById(R.id.UserName);
		txPassword=(EditText)findViewById(R.id.textPasswd);
	}
	/**
	 * �����¼��ļ������ķ���
	 */
	private void setListener() {
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName=txUserName.getText().toString();
				Log.v("userName = ", userName);
				String password=txPassword.getText().toString();
				Log.v("passwd = ",password);
				//loginRemoteService(userName,password);
				//useStringVolley(userName,password);
				useJsonVolley( userName, password);
			}
		});
	}
	
	public void useJsonVolley(String userName,String password)
	{
		//url= url_constant+"userName="+userName+"&password="+password;
		url="http://www.weather.com.cn/adat/cityinfo/101010100.html";
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jresponse) {
						Log.d("TAG", jresponse.toString());
						Weatherinfo weatherinfo = new Weatherinfo();
						weatherinfo =JSON.parseObject(jresponse.toString(), Weatherinfo.class);
						String result;
						try {
							   result = jresponse.get("message").toString();
							   if(result.contains("ʧ��"))
							   {
								 AlertDialog.Builder builder=new Builder(MainActivity.this);
								 builder.setTitle("��ʾ")
								 .setMessage(result)
								 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}).create().show();
							   }
							   else
							   {
								   startActivity(new Intent(MainActivity.this, WorkIndexActivity.class));
							   }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				});
		mQueue.add(jsonObjectRequest);
	}
	public void useStringVolley(String userName,String password)
	{
    	url= url_constant+"userName="+userName+"&password="+password;
		
		StringRequest stringRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String strresponse) {
						Log.d("TAG", strresponse);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(strresponse);
							String result=jsonObject.get("message").toString();
							   if(result.contains("ʧ��"))
							   {
								 AlertDialog.Builder builder=new Builder(MainActivity.this);
								 builder.setTitle("��ʾ")
								 .setMessage(result)
								 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}).create().show();
							   }
							   else
							   {
								   startActivity(new Intent(MainActivity.this, WorkIndexActivity.class));
							   }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				});
		mQueue.add(stringRequest);
	}
	
	/**
     * ��ȡStruts2 Http ��¼��������Ϣ
     * @param  userName
     * @param  password
     */
    public void loginRemoteService(String userName,String password){
        String result=null;
    	try {
	    		 
	    	//����һ��HttpClient����
	    	HttpClient httpclient = new DefaultHttpClient();
	    	//Զ�̵�¼URL
	    	//���������ԭ�е�
	    	//processURL=processURL+"userName="+userName+"&password="+password;
	    	url= url_constant+"userName="+userName+"&password="+password;
	    	Log.d("Զ��URL", url);
	        //����HttpGet����
	    	HttpGet request=new HttpGet(url);
	    	//������Ϣ����MIMEÿ����Ӧ���͵��������ͨ�ı���html �� XML��json�����������Ӧ����Ӧ��ƥ����Դ�������ɵ� MIME ����
	    	//��Դ�����ɵ� MIME ����Ӧ��ƥ��һ�ֿɽ��ܵ� MIME ���͡�������ɵ� MIME ���ͺͿɽ��ܵ� MIME ���Ͳ� ƥ�䣬��ô��
	    	//���� com.sun.jersey.api.client.UniformInterfaceException�����磬���ɽ��ܵ� MIME ��������Ϊ text/xml������
	    	//���ɵ� MIME ��������Ϊ application/xml�������� UniformInterfaceException��
	    	request.addHeader("Accept","text/json");
	        //��ȡ��Ӧ�Ľ��
			HttpResponse response =httpclient.execute(request);
			//��ȡHttpEntity
			HttpEntity entity=response.getEntity();
			//��ȡ��Ӧ�Ľ����Ϣ
			String json =EntityUtils.toString(entity,"UTF-8");
			//JSON�Ľ�������
			if(json!=null){
				JSONObject jsonObject=new JSONObject(json);
				result=jsonObject.get("message").toString();
			}
		   if(result==null){  
			   json="��¼ʧ�������µ�¼";
		   }
			//������ʾ�������Ƿ��¼�ɹ�
		   if(result.contains("ʧ��"))
		   {
			 AlertDialog.Builder builder=new Builder(MainActivity.this);
			 builder.setTitle("��ʾ")
			 .setMessage(result)
			 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create().show();
		   }
		   else
		   {
			   startActivity(new Intent(MainActivity.this, WorkIndexActivity.class));
		   }
		 
    	 } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
