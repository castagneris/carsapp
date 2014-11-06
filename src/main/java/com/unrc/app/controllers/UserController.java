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


public class UserController { 
    
    
    public ModelAndView listUser(Request request, Response response) {
              Map<String, Object> attributes = new HashMap<>();
          //pasa a list todos los usuarios
          List<User> users = User.findAll();
          
          boolean admin = false;
          Session session = request.session(false);
          if (session != null){   
              String email = session.attribute("user_email");
              User u = User.findFirst("email=?",email);
              attributes.put("admin",u.getAdmin());
          }
          else{
              attributes.put("admin",admin);
          }
          //carga tamaño de usuarios
          attributes.put("users_count", users.size());
          //carga list en map
          attributes.put("users", users);
         
          
          return new ModelAndView(attributes, "ListUsers.moustache");
     
      
    }  
    public ModelAndView apptUser(Request request, Response response) {
        User user = User.findById(request.params("id"));
        String email2 = user.getEmail();
        Session session = request.session(false);
        String email = session.attribute("user_email");
        User u = User.findFirst("email=?",email);
        Map<String,Object> attributes = new HashMap<String,Object>();
        if ((session != null)) {
                if ((u.getAdmin()) || (session.attribute("user_email").equals(email2))) {
                   attributes.put("user",u); 
                }
                else{
                    response.redirect("/Login");
                }
        }
        else{
            response.redirect("/app");
        }
        return new ModelAndView(attributes,"UserId.moustache");
   }
    public ModelAndView newUser() {
        Map<String, Object> attributes = new HashMap<>();
          
          return new ModelAndView(attributes, "RegisterUser.moustache");
    }
      public String addUser(Request request, Response response) {
           String retornar;
			retornar=" <body>";
            User user = new User();
            //Cargar variable user con datos tomados por pantalla
            user.set("first_name", request.queryParams("first_name"));
            user.set("last_name", request.queryParams("last_name"));
            user.set("email",request.queryParams("email"));
            user.set("pass",request.queryParams("pass"));
            user.set("admin",false);
            
            Address city = new Address();
            //Carga variable city con datos tomados por pantalla
            city.set("country", request.queryParams("country"));
            city.set("state",request.queryParams("state"));
            city.set("name",request.queryParams("name"));
            city.saveIt();

            user.saveIt();
            city.add(user);
			retornar = retornar + "Usuario Cargado Correctamente";
			retornar = retornar + "<a href="+"http://localhost:4567/app"+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
            return retornar;
      }
      
      public ModelAndView guestUser() {
          return new ModelAndView(null, "Guest.moustache");
      }
      
      public String addAdmin(Request request, Response response) {
           Session session = request.session(false);
            String email = session.attribute("user_email");
            User u2 = User.findFirst("user_email=?",email);
            if (u2.getAdmin()){
                User u = User.findById(request.params("id"));
                u.set("admin",true);
                u.save();
                String retornar = "Has añadido un nuevo administrador ";
                retornar = retornar + "<a href="+"http://localhost:4567/ListUser"+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
                return retornar; 
            }
            else{
                String retornar = "No eres administrador ";
                retornar = retornar + "<a href="+"http://localhost:4567/ListUser"+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
                return retornar; 
            }
      }
      public String deleteUser(Request request, Response response) {
          Session session = request.session(false);
            User u = User.findById(request.params("id"));
            String idSession = session.attribute("id");
            User u2 = User.findById(request.params("idSession"));
            if (u.id().compareTo(u2.id()) == 0 || u2.getAdmin()){
                u.deleteCascade();
                String retornar = "Has sido eliminado";
                return retornar +"<a href="+"http://localhost:4567/app"+"><h3 style="+"color:#0000FF"+"> Volver </h3></a>";
            }else{
                String retornar = "Error no tienes permisos para esta operacion";
                return retornar +"<a href="+"http://localhost:4567/app"+"><h3 style="+"color:#0000FF"+"> Volver </h3></a>";
            }
          
      }

}
