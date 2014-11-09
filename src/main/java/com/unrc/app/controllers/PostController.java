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


public class PostController { 

    public ModelAndView userPost(Request request, Response response) {
        User u = User.findById(request.params("id"));
        String email = u.getEmail();
        Session session = request.session(false);
        Map<String, Object> attributes = new HashMap<>();
        if ((session != null)) {
            if ((session.attribute("user_email").equals(email)) || (u.getAdmin())) {
                List <Post> posts = Post.where("user_id = ?", request.params("id"));
                attributes.put("UserPosts",posts);
                attributes.put("userId",u.id()); 
            }
            else{
                response.redirect("/app");
            }
        }
        else{
            response.redirect("/app");
        }
        return new ModelAndView(attributes, "UserPosts.moustache");
       
    }
    
    public ModelAndView idPost(Request request, Response response) {
        Post p = Post.findById(request.params("pId"));
        User u = User.findById(p.getOwner());
        Vehicle v = Vehicle.findById(p.getVehicle());
        List<Car> listC = Car.where("vehicle_id = ?", v.getId());
        List<Truck> listT = Truck.where("vehicle_id = ?", v.getId());
        List<Motorcycle> listM = Motorcycle.where("vehicle_id = ?", v.getId());   
        
        Map<String,Object> attributes = new HashMap<String,Object>();
        
        attributes.put("userFirstName",u.getFirstName());
        attributes.put("userLastName",u.getLastName());
        attributes.put("price",p.getPrice());
        attributes.put("title",p.getTitle());
	attributes.put("description",p.getDescription());
        attributes.put("model",v.getModel());
        attributes.put("patent",v.getPatent());
        attributes.put("idp",p.getId());
        if (!(listM.isEmpty())){
            attributes.put("type_motor",listM.get(0).getTypeMotor()); 
        } 
        if (!(listC.isEmpty())) {
            attributes.put("version",listC.get(0).getVersion());
        } 
     
        if (!(listT.isEmpty())){
            attributes.put("brake_system",listT.get(0).getbrakeSystem());
        } 
        return new ModelAndView(attributes,"PostId.moustache");
    }
    
     public ModelAndView listPost(Request request, Response response) {
         Session session = request.session(false);
          boolean log = false; 
          if (session != null){  
              log = true;
          }
          Map<String, Object> attributes = new HashMap<>();
          List<Post> post = Post.findAll();
          
          attributes.put("post_count", post.size());
          attributes.put("post", post);
          attributes.put("log", log);
          
          return new ModelAndView(attributes, "ListPost.moustache");
     }
   
    public ModelAndView idPosts(Request request, Response response) {
         Post p = Post.findById(request.params("idp"));
        List<Point> points = Point.where("post_id=?",request.params("idp"));
        int cant = 0;
        int count = 0;
        for (Point p2 : points){
            cant = cant  + p2.getPoint();
            count ++;
        }
        if (count ==0) {
            count =1; }
        Vehicle v = Vehicle.findById(p.getVehicle());
        User u = User.findById(v.getOwner());
        
        List<Car> listC = Car.where("vehicle_id = ?", v.getId());
       
        List<Truck> listT = Truck.where("vehicle_id = ?", v.getId());
        List<Motorcycle> listM = Motorcycle.where("vehicle_id = ?", v.getId());   
        Map<String,Object> attributes = new HashMap<String,Object>();
        double mean = cant / count;
        attributes.put("mean",mean);
        attributes.put("id", u.id());
        attributes.put("userFirstName",u.getFirstName());
        attributes.put("userLastName",u.getLastName());
        attributes.put("price",p.getPrice());
        attributes.put("title",p.getTitle());
        attributes.put("model",v.getModel());
        attributes.put("patent",v.getPatent());
	attributes.put("mark",v.getMark());
	attributes.put("color",v.getColor());
	attributes.put("km",v.getKm());  
       
       
        
        if (!(listM.isEmpty())){
            attributes.put("type_motor",listM.get(0).getTypeMotor()); 
        } 
        if (!(listC.isEmpty())) {
            attributes.put("version",listC.get(0).getVersion());
    
        } 
     
        if (!(listT.isEmpty())){
            attributes.put("brake_system",listT.get(0).getbrakeSystem());
        } 
            
           
        return new ModelAndView(attributes,"PostId.moustache");
    }

    public ModelAndView newPost(Request request, Response response) {
          User u = User.findById(request.params("id"));
          String email = u.getEmail();
          Session session = request.session(false);
          String emailSession = session.attribute("user_email");
          String idSession = session.attribute("id");
          User userSession = User.findById(idSession);
          Map<String, Object> attributes = new HashMap<>();
          if((email.compareTo(emailSession) == 0) || userSession.getAdmin() ){

              attributes.put("u",u.id());
          }else{
              response.redirect("/Login");
          }
          
          return new ModelAndView(attributes, "NewPost.moustache");
    }
    
          public String addPost(Request request, Response response) {
              String retornar = "";
            User u = User.findById(request.params("id"));
            List<Vehicle> v = Vehicle.where("user_id = ?", request.params("id"));
            String patente = request.queryParams("patent");
            boolean checkpatent = false;
            for (Vehicle v2 : v){
                if ((v2.getPatent().compareTo(patente)) == 0){
                    checkpatent = true;
                }
            }
            if (checkpatent){
            Vehicle vehicle = Vehicle.findFirst("patent =?", patente);    
            
            
            Post post = new Post();
            post.set("title",request.queryParams("title"));
            post.set("description",request.queryParams("description"));
            post.set("price",request.queryParams("price"));
            post.saveIt();
            u.add(post);
            vehicle.add(post);
            retornar = retornar +"Se ha creado el post exitosamente";
            retornar = retornar + "<a href="+"http://localhost:4567/app/"+request.params("id")+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
            return retornar;
            }
            else {
            retornar =retornar +"No se ha creado el post, patente inexistente";
            retornar = retornar + "<a href="+"http://localhost:4567/app/"+request.params("id")+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";    
            return retornar;
            }
          }

    
    











}

