package com.unrc.app.models;

import org.javalite.activejdbc.Model;

public class Vehicle extends Model {
  static {
      validatePresenceOf("model", "patent", "color","km","mark","year");
  }
  
  //get model
  public String getModel(){
	  return (this.getString("model"));
}

  //get patent
  public String getPatent(){
	  return (this.getString("patent"));
  }
  
  //get color
  public String getColor(){
	  return (this.getString("color"));
  }
  
  //get km
  public String getKm(){
	  return (this.getString("km"));
  }
  //get mark
  public String getMark(){
	  return (this.getString("mark"));
  } 
  
  //get year
  public String getYear(){
	  return (this.getString("year"));
  }
  
  public int getOwner (){
      return (this.getInteger("user_id"));
  }
}
