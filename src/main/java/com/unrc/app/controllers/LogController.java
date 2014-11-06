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


public class  LogController { 
        
    public ModelAndView login() {
        return new ModelAndView(null, "Login.moustache");
    }
    
    public String checkLogin(Request request, Response response) {
        String retornar;
			retornar=" <body>";           
	    User u = User.findFirst("email=?",request.queryParams("email"));         
	    String pass =request.queryParams("pass");
            
            if (u !=null ){
	    if (u.getPass().compareTo(pass) == 0) {
                Session session = request.session(true);
                session.attribute("user_email", u.getEmail());
                session.attribute("user_id", u.id());
                if (u.getAdmin()) {
                      response.redirect("/app/"+u.id());
                }
                else {
                    response.redirect("/app/"+u.getId());
                }
            }
            else{
                response.redirect("/Login");
            }
            
            }
            else {
                retornar = retornar + "Usuario no existente";
                retornar = retornar +"<a href="+"http://localhost:4567/Login"+"><h3 style="+"color:#DF3A01"+"> Volver </h3></a>";
            }         
	         
	 return retornar;
    }

        public String logout(Request request, Response response) {
             Session session = request.session(false);
            if (session!=null) {
                session.invalidate();
            }
            response.redirect("/app");
            return null;
        }


}