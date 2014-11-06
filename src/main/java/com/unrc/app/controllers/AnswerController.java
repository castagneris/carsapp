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


public class  AnswerController {  
        public ModelAndView answerQuestion(Request request, Response response) {
            Question q = Question.findById(request.params("idQ")); 
        Answer a = new Answer();
        a = Answer.findFirst("question_id=?",q.getId());
        
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("getText",a.getText());
        
        
        return new ModelAndView(attributes, "AnswerQuestions.moustache");
        }
public String addAnswer(Request request, Response response) {
            String retornar= "";
            Question q = Question.findById(request.params("idQ"));
            Session session = request.session(false);
            String email = session.attribute("user_email");
            User u = User.findFirst("email=?",email);
            Answer answer = new Answer();
            answer.set("textA",request.queryParams("textA"));
            answer.saveIt();
            q.add(answer);
            u.add(answer);
            retornar =retornar +"Ha respondido pregunta exitosamente";
            return retornar; 
}
}