package com.unrc.app.controllers;

import com.unrc.app.models.*;
import java.util.*;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.javalite.activejdbc.Base;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.Session;
import spark.Request;
import spark.Response;


public class VehicleController {  
    public ModelAndView newVehicle(Request request, Response response) {
        Map<String, Object> attributes = new HashMap<>();
	return new ModelAndView(attributes, "InsertVehicle.moustache");
    }
    
    public ModelAndView idVehicle(Request request, Response response) {
        Vehicle v = Vehicle.findById(request.params("vId"));
        User u = User.findById(request.params("id"));           
        
         Map<String,Object> attributes = new HashMap<String,Object>();
        attributes.put("userFirstName",u.getFirstName());
        attributes.put("userLastName",u.getLastName());
        attributes.put("id", u.id());        
        attributes.put("model",v.getModel());
        attributes.put("patent",v.getPatent());
	attributes.put("mark",v.getMark());
	attributes.put("color",v.getColor());
	attributes.put("km",v.getKm());
            
        return new ModelAndView(attributes,"VehicleId.moustache");
    }
    
        public ModelAndView listVehicle(Request request, Response response) {
          Map<String, Object> attributes = new HashMap<>();
          List<Vehicle> vehicles = Vehicle.findAll();
          Session session = request.session(false);
            if (session!=null) {
                String email = session.attribute("user_email");
                User u = User.findFirst("email=?",email);
                attributes.put("admin",u.getAdmin());
                attributes.put("notGuest",true);
          }
          attributes.put("vehiRegiscles_count", vehicles.size());
          attributes.put("vehicles", vehicles);
          return new ModelAndView(attributes, "ListVehicles.moustache");
        }
    
        
        public ModelAndView userVehicle(Request request, Response response) {
        User u = User.findById(request.params("id"));
        List<Vehicle> v = Vehicle.where("user_id=?", u.id());
 
        Map<String,Object> attributes = new HashMap<String,Object>();
        attributes.put("id", u.id());
        attributes.put("userFirstName",u.getFirstName());
        attributes.put("userLastName",u.getLastName());
        attributes.put("vehicles", v); 
            
           
        return new ModelAndView(attributes,"UserVehicles.moustache");    
        }
        
        public ModelAndView newCar(Request request, Response response) {
          Map<String, Object> attributes = new HashMap<>();
          Session session = request.session(false);
          String id = session.attribute("user_id");
          
          attributes.put("u",id);
          return new ModelAndView(attributes, "NewCar.moustache");
        }
        
        public String addCar (Request request, Response response) {
            String retornar;
            retornar=" <body>";
            User user = new User();
            //Cargar variable user con datos tomados por pantalla
            User user2 = new User();
            user.set("id",request.params("id"));
            
            user2 = User.findFirst("id=?",user.get("id"));
            //controlar existencia de usuario en base de datos

            Vehicle vehicle = new Vehicle();
            //carga variable vehicle con datos tomados por pantalla
            vehicle.set("model",request.queryParams("model"));
            vehicle.set("patent",request.queryParams("patent"));
            vehicle.set("color",request.queryParams("color"));
            vehicle.set("km",request.queryParams("km"));
            vehicle.set("mark",request.queryParams("mark"));
            vehicle.set("year",request.queryParams("year"));
            vehicle.saveIt();
            user2.add(vehicle); 


            Car car = new Car();
            //carga variable car con datos tomados por pantalla
            car.set("doors",request.queryParams("doors"));
            car.set("version",request.queryParams("version"));
            car.set("transmission",request.queryParams("transmission"));
            car.set("direction",request.queryParams("direction"));

            car.saveIt();
            vehicle.add(car);
            retornar =retornar +"Carga Exitosa";
           
            retornar = retornar + "<a href="+"http://localhost:4567/app/"+user2.id()+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
            return retornar;    
        }
        
        public ModelAndView newMotorcycle(Request request, Response response) {
          Map<String, Object> attributes = new HashMap<>();
          Session session = request.session(false);
          String id = session.attribute("user_id");
          
          attributes.put("u",id);
          return new ModelAndView(attributes, "NewMotorcycle.moustache");
        }
        
        public String addMotorcycle (Request request, Response response) {
            String retornar;
            retornar=" <body>";
            User user = new User();
            //Cargar variable user con datos tomados por pantalla
            User user2 = new User();
            user.set("id",request.params("id"));
            
            user2 = User.findFirst("id=?",user.get("id"));
            
            Vehicle vehicle = new Vehicle();
            //carga variable vehicle con datos tomados por pantalla
            vehicle.set("model",request.queryParams("model"));
            vehicle.set("patent",request.queryParams("patent"));
            vehicle.set("color",request.queryParams("color"));
            vehicle.set("km",request.queryParams("km"));
            vehicle.set("mark",request.queryParams("mark"));
            vehicle.set("year",request.queryParams("year"));
            vehicle.saveIt();
            user2.add(vehicle);    

            Motorcycle moto = new Motorcycle();
            //carga variable moto con datos tomados por pantalla
            moto.set("type",request.queryParams("type"));
            moto.set("type_motor",request.queryParams("type_motor"));
            moto.set("boot_system",request.queryParams("boot_system"));
            moto.set("displacement",request.queryParams("displacement"));

            moto.saveIt();
            vehicle.add(moto);

            retornar =retornar +"Carga Exitosa";
        retornar = retornar + "<a href="+"http://localhost:4567/app/"+user2.id()+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
        return retornar;
        }
    
        public ModelAndView newTruck(Request request, Response response) {
          Map<String, Object> attributes = new HashMap<>();
          Session session = request.session(false);
          String id = session.attribute("user_id");
          
          attributes.put("u",id);
          return new ModelAndView(attributes, "NewTruck.moustache");
        }
        
        public String addTruck (Request request, Response response) {
            String retornar;
            retornar=" <body>";
            User user = new User();
            //Cargar variable user con datos tomados por pantalla
            User user2 = new User();
            user.set("id",request.params("id"));
            
            user2 = User.findFirst("id=?",user.get("id"));
            Vehicle vehicle = new Vehicle();
            //carga variable vehicle con datos tomados por pantalla
            vehicle.set("model",request.queryParams("model"));
            vehicle.set("patent",request.queryParams("patent"));
            vehicle.set("color",request.queryParams("color"));
            vehicle.set("km",request.queryParams("km"));
            vehicle.set("mark",request.queryParams("mark"));
            vehicle.set("year",request.queryParams("year"));
            vehicle.saveIt();
            user2.add(vehicle);    

            Truck camion = new Truck();
            //carga variable camion con datos tomados por pantalla
            camion.set("brake_system",request.queryParams("brake_system"));
            camion.set("direction",request.queryParams("direction"));
            camion.set("capacity",request.queryParams("capacity"));                    
            camion.saveIt();
            vehicle.add(camion);


            retornar =retornar +"Carga Exitosa";
            retornar = retornar + "<a href="+"http://localhost:4567/app/"+user2.id()+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
            return retornar;
        }
    
    
    
    
    
    
    
    
    
    
}
