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


public class QuestionController { 
        public ModelAndView questionPost(Request request, Response response) {
        List <Question> questions = Question.where("post_id = ?", request.params("idp"));
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("QuestionPosts",questions);
        
        
        return new ModelAndView(attributes, "QuestionPosts.moustache");
        }
        
        
        public String addQuestion(Request request, Response response) {
            String retornar;
            retornar=" <body>";
            Session session = request.session(false);
            String email = session.attribute("user_email");
            User u = User.findFirst("email=?",email);
            Post p = Post.findById(request.params("idp"));
            //Cargar variable user con datos tomados por pantalla
       
            //controlar existencia de usuario y de post en base de datos
                Question question = new Question();
                question.set("textQ",request.queryParams("textQ"));
                question.saveIt();
                
                u.add(question);
                p.add(question);

                retornar =retornar +"Carga Exitosa";
                    
		retornar = retornar + "<a href="+"http://localhost:4567/ListPost"+"><h3 style="+"\"color:#DF3A01\""+"> Volver </h3></a>";
            
                return retornar; 
        }

}
