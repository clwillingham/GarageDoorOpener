package com.clwillingham.particle_api.models;

import com.clwillingham.particle_api.ParticleIO;

/**
 * Created by Chris on 7/28/2015.
 */
public class ParticleDevice {
    private String id;
    private String deviceID;
    public String name;
    public String last_app;
    public String last_heard;
    public String product_id;
    public boolean connected;
    public String last_handshake_at;

    private transient ParticleIO.API api;

    public ParticleDevice(){

    }

    public ParticleDevice(String id){
        this.id = id;
    }

    public String getId(){
        return id == null? deviceID : id;
    }

    public void setApi(ParticleIO.API api){
        this.api = api;
    }

    public VariableResponse getVariable(String variable){
        return api.getVariable(id, variable);
    }

    public FunctionResponse callFunction(String functionName, String args){
        return api.callFunction(id, functionName, args);
    }

    public FunctionResponse callFunction(String functionName){
        return callFunction(functionName, "");
    }

    public void getDeviceData(){
        ParticleDevice me = api.getDevice(id);
        this.id = me.id;
        this.last_app = me.last_app;
        this.last_heard = me.last_heard;
        this.product_id = me.product_id;
        this.connected = me.connected;
        this.last_handshake_at = me.last_handshake_at;
    }
}
