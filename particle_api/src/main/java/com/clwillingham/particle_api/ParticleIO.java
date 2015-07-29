package com.clwillingham.particle_api;

import com.clwillingham.particle_api.models.FunctionResponse;
import com.clwillingham.particle_api.models.ParticleDevice;
import com.clwillingham.particle_api.models.VariableResponse;

import java.util.ArrayList;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by Chris on 7/28/2015.
 */
public class ParticleIO{
    private RestAdapter adapter;
    private API api;
    private String accessToken;

    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            if(accessToken != null)
                request.addHeader("Authorization", "Bearer " + accessToken);
        }
    };

    public interface API{
        @GET("/v1/devices")
        ArrayList<ParticleDevice> getDevices();

        @GET("/v1/devices/{DEVICE_ID}")
        ParticleDevice getDevice(@Path("DEVICE_ID") String deviceId);

        @GET("/v1/devices/{DEVICE_ID}/{VARIABLE}")
        VariableResponse<?> getVariable(@Path("DEVICE_ID") String deviceId, @Path("VARIABLE") String variable);

        @Multipart
        @POST("/v1/devices/{DEVICE_ID}/{FUNCTION}")
        FunctionResponse callFunction(@Path("DEVICE_ID") String deviceId, @Path("FUNCTION") String function, @Part("args") String args);
    }

    public ParticleIO(String url, String accessToken){
        adapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setRequestInterceptor(requestInterceptor)
                .build();
        setAccessToken(accessToken);
        api = adapter.create(API.class);
    }

    public ParticleIO(String accessToken){
        this("https://api.particle.io", accessToken);
    }

    public ParticleIO(){
        this(null);
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public boolean hasValidAccessToken(){
        return getAccessToken() == null || getAccessToken().length() == 40;
    }

    public ArrayList<ParticleDevice> getDevices(){
        ArrayList<ParticleDevice> response = api.getDevices();
        for(ParticleDevice device : response){
            device.setApi(api);
        }
        return response;
    }
    public ParticleDevice getDevice(String deviceId){
        ParticleDevice device = api.getDevice(deviceId);
        device.setApi(api);
        return device;
    }

    public VariableResponse getVariable(String deviceId, String variable){
        return api.getVariable(deviceId, variable);
    }

    public FunctionResponse callFunction(String deviceId, String functionName, String args){
        return api.callFunction(deviceId, functionName, args);
    }

    public FunctionResponse callFunction(String deviceId, String functionName){
        return callFunction(deviceId, functionName, "");
    }
}
