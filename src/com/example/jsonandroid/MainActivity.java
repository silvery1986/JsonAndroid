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
 //模拟器自己把自己当成localhost了，服务器应该为10.0.2.2
 private static  String url="http://10.0.2.2:8080/JsonWeb/login.action?";
 private final String url_constant="http://10.0.2.2:8080/JsonWeb/login.action?";
 private EditText txUserName;
 private EditText txPassword;
 private Button btnLogin;
 private RequestQueue mQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	///在Android2.2以后必须添加以下代码
		//本应用采用的Android4.0
		//设置线程的策略
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()   
         .detectDiskReads()   
         .detectDiskWrites()   
         .detectNetwork()   // or .detectAll() for all detectable problems   
         .penaltyLog()   
         .build());   
		//设置虚拟机的策略
		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()   
		         .detectLeakedSqlLiteObjects()   
		         //.detectLeakedClosableObjects()   
		         .penaltyLog()   
		         .penaltyDeath()   
		         .build());
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        //设置页面布局
        setContentView(R.layout.main);
        //设置初始化视图
        initView();
        //设置事件监听器方法
        setListener();
    }
    
    /**
     * 创建初始化视图的方法
     */
	private void initView() {
		btnLogin=(Button)findViewById(R.id.btnLogin);
		txUserName=(EditText)findViewById(R.id.UserName);
		txPassword=(EditText)findViewById(R.id.textPasswd);
	}
	/**
	 * 设置事件的监听器的方法
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
							   if(result.contains("失败"))
							   {
								 AlertDialog.Builder builder=new Builder(MainActivity.this);
								 builder.setTitle("提示")
								 .setMessage(result)
								 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
									
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
							   if(result.contains("失败"))
							   {
								 AlertDialog.Builder builder=new Builder(MainActivity.this);
								 builder.setTitle("提示")
								 .setMessage(result)
								 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
									
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
     * 获取Struts2 Http 登录的请求信息
     * @param  userName
     * @param  password
     */
    public void loginRemoteService(String userName,String password){
        String result=null;
    	try {
	    		 
	    	//创建一个HttpClient对象
	    	HttpClient httpclient = new DefaultHttpClient();
	    	//远程登录URL
	    	//下面这句是原有的
	    	//processURL=processURL+"userName="+userName+"&password="+password;
	    	url= url_constant+"userName="+userName+"&password="+password;
	    	Log.d("远程URL", url);
	        //创建HttpGet对象
	    	HttpGet request=new HttpGet(url);
	    	//请求信息类型MIME每种响应类型的输出（普通文本、html 和 XML，json）。允许的响应类型应当匹配资源类中生成的 MIME 类型
	    	//资源类生成的 MIME 类型应当匹配一种可接受的 MIME 类型。如果生成的 MIME 类型和可接受的 MIME 类型不 匹配，那么将
	    	//生成 com.sun.jersey.api.client.UniformInterfaceException。例如，将可接受的 MIME 类型设置为 text/xml，而将
	    	//生成的 MIME 类型设置为 application/xml。将生成 UniformInterfaceException。
	    	request.addHeader("Accept","text/json");
	        //获取响应的结果
			HttpResponse response =httpclient.execute(request);
			//获取HttpEntity
			HttpEntity entity=response.getEntity();
			//获取响应的结果信息
			String json =EntityUtils.toString(entity,"UTF-8");
			//JSON的解析过程
			if(json!=null){
				JSONObject jsonObject=new JSONObject(json);
				result=jsonObject.get("message").toString();
			}
		   if(result==null){  
			   json="登录失败请重新登录";
		   }
			//创建提示框提醒是否登录成功
		   if(result.contains("失败"))
		   {
			 AlertDialog.Builder builder=new Builder(MainActivity.this);
			 builder.setTitle("提示")
			 .setMessage(result)
			 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
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
