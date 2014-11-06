package com.unrc.app;

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
import com.unrc.app.controllers.*;

public class App {
    public static final Node node = org.elasticsearch.node
                                        .NodeBuilder
                                        .nodeBuilder()
                                        .clusterName("carsapp")
                                        .local(true)
                                        .node();
    public static Client client(){
        return node.client();
    }
      
    public static void main( String[] args )
    {
        System.out.println( "Hello cruel World!" );
	externalStaticFileLocation("./Images");
   
	before((request, response) -> {                                 //OJO CON LA CONTRASEÑA
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_development", "root", "root");
        });     
   
                UserController userController = new UserController();
                VehicleController vehicleController = new VehicleController();
                PostController postController = new PostController();
                LogController logController = new LogController();
                QuestionController questionController = new QuestionController ();
                AnswerController answerController = new AnswerController ();
                
		get ("/app", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			return new ModelAndView(attributes, "Main.moustache");

			 },
            new MustacheTemplateEngine()
        );
		
		get ("/InsertVehicle", (request, response) -> {
			return vehicleController.newVehicle(request, response);

			 },
            new MustacheTemplateEngine()
        );
	get("/app/:id/darAdmin", (request, response) -> {
           return userController.addAdmin(request, response);
        });	

//------------------------------------------Listar----------------------------  
   // Listar Usuario
    get("/ListUser", (request, response) -> {
          return userController.listUser(request, response);
      },
      new MustacheTemplateEngine()
  );
    

    get("/app/:id", (request, response) -> {
        return userController.apptUser(request, response);
    },
            
        new MustacheTemplateEngine()
    );
     
       
    get("/app/:id/posts",(request,response)-> {
        return postController.userPost(request, response);
        } ,
       new MustacheTemplateEngine()
    );
            
            
    get("posts/:pId", (request, response) -> {
       return postController.idPost(request, response); 
        },
        new MustacheTemplateEngine()
    );
   
    get("app/:id/vehicles/:vId", (request, response) -> {
        return vehicleController.idVehicle(request, response);
        },
        new MustacheTemplateEngine()
            );
    
                

// Listar Post
        get("/ListPost", (request, response) -> {
       return postController.listPost(request, response); 
          
      },
      new MustacheTemplateEngine()
  );
        

                
   get("/app/:id/posts/:idp", (request, response) -> {
       return postController.idPosts(request, response); 
       
        },
        new MustacheTemplateEngine()
            
   );
 
  get("app/:id/posts/:idp/questions", (request, response) -> {
        return questionController.questionPost(request, response);
        } ,
       new MustacheTemplateEngine()
   );

  
   get("app/:id/posts/:idP/questions/:idQ/answerInd", (request, response) -> {
       return answerController.answerQuestion(request, response);       
        } ,
       new MustacheTemplateEngine()
   );
 
  
   get("app/:id/delete", (request, response) -> {
            return userController.deleteUser(request, response);

            
    });
 
    //listar Vehiculos
	get("/ListVehicles", (request, response) -> {
            return vehicleController.listVehicle(request, response);
      },
      new MustacheTemplateEngine()
  );
        
     // todos los vehiculos del usario   
    get("/app/:id/vehicles", (request, response) -> {
        return vehicleController.userVehicle(request, response);
        },
        new MustacheTemplateEngine()
            );
 
        
   get("/ListVehicles/:id/delete", (request, response) -> {
            Vehicle v = Vehicle.findById(request.params("id"));
            v.deleteCascade();
            return "<a href="+"http://localhost:4567/app"+"><h3 style="+"color:#0000FF"+"> Volver </h3></a>";
    });
 
  //--------------------------------------Insert---------------------------------- 
     
          get ("/RegisterUser", (request, response) -> {                          
              return userController.newUser();
      },
      new MustacheTemplateEngine()
  );
         post("/User", (request, response) -> {
           return userController.addUser(request, response);
            
         });
        
        get("/Guest", (request, response) -> {
         return userController.guestUser();
      },
      new MustacheTemplateEngine()
  );
          
   get("/Login", (request, response) -> {
          return logController.login();
      },
      new MustacheTemplateEngine()
  );
 
   
  
    post("/CheckLogin", (request, response) -> {
            return logController.checkLogin(request, response);
        });
         
      get("/Logout", (request, response) -> {
            return logController.logout(request, response);

        });
         
        get ("/app/:id/newCar", (request, response) -> {         
          return vehicleController.newCar(request, response);
      },
      new MustacheTemplateEngine()
  );        
     
        post("/app/:id/car", (request, response) -> {
            return vehicleController.addCar(request, response);
         });   
        
        get ("/app/:id/newMotorcycle", (request, response) -> {         
          return vehicleController.newMotorcycle(request, response);
      },
      new MustacheTemplateEngine()
  );                  
        
        post("/app/:id/motorcycle", (request, response) -> {
            return vehicleController.addMotorcycle(request, response);
     });   
        
         
        get ("/app/:id/newTruck", (request, response) -> {         
          return vehicleController.newTruck(request, response);
      },
      new MustacheTemplateEngine()
  ); 
        
        
        post("/app/:id/truck" , (request, response) -> {
            return vehicleController.addTruck(request, response);
         });   

    
        get("/app/:id/newPost" , (request, response)-> {
          return postController.newPost(request, response);
      },
      new MustacheTemplateEngine()
  ); 
      
        post("/app/:id/post", (request, response) -> {
          return postController.addPost(request, response);
            
         });
        
        
         get("/:idp/newQuestion", (request, response) -> {        
           Map<String, Object> attributes = new HashMap<>();
          
          attributes.put("idp",request.params("idp"));
          return new ModelAndView(attributes, "newQuestion.moustache");
      },
      new MustacheTemplateEngine()
  ); 
         
        post("/:idp/question", (request, response) -> {
            return questionController.addQuestion(request, response);
           
         });
                 
                                                                                    
        get ("app/:id/posts/:idp/questions/:idq", (request, response) -> {         
           Map<String, Object> attributes = new HashMap<>();
          
          attributes.put("idq",request.params("idq"));
          return new ModelAndView(attributes, "newAnswer.moustache");
      },
      new MustacheTemplateEngine()
  ); 
        
        post("questions/:idQ/answer", (request, response) -> {
            return answerController.addAnswer(request, response);
           
         });
        
    /*----------------------------------------------BUSQUEDAS----------------------------------------------------*/    
        get ("/Search/User", (request, response) -> {         
            String a ="<html> <head> <title> Busqueda </title> </head> <body> <form action=\"/Search\" method=\"post\">";

		 // crea un enlace para volver a la pag principal.
	    a=a + "<table align="+"right"+"><td><a href="+"http://localhost:4567/app"+"><img src="+"http://localhost:4567/ButtonHome.png"+" width="+"45"+" height="+"45" +"/></a></td></table>";
             a= a + "<h1 style="+"color:#DF3A01"+"> Buscar Usuario </h1> <FORM >";          
            
            //Usuario
            //lectura 
            a=a+"<p>Nombre  </p><INPUT type=text SIZE=25 NAME=firstName>";
                      
            a= a + "<td align=right valign=top></td><td align=center>";
            //creacion de botones
            a = a + "<input type=reset value=Borrar_informacion><input type=submit value= Enviar></FORM></BODY></HTML>";
            return a; 	
        });
        
        post("/Search",(request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
           
            Client client = node.client();
           
  
            SearchResponse response_elastic = client.prepareSearch("users")
                                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                                        .setQuery(QueryBuilders.matchQuery("name",request.queryParams("firstName")))
                                        .execute()
                                        .actionGet();

            SearchHit[] docs = response_elastic.getHits().getHits();

            node.close();

          
            Map<String,Object> map = new HashMap<String,Object>();
            List<Map<String,Object>> lista = new LinkedList<Map<String,Object>>();
            for (SearchHit sh : docs) {
                map = sh.getSource();
                lista.add(map);

            } 
            long cant = response_elastic.getHits().getTotalHits();
            
            attributes.put("list",lista);
            attributes.put("cantidad",cant);
            return new ModelAndView(attributes,"SearchUser.moustache");
        },
            new MustacheTemplateEngine()
       
     );
        
      get("posts/:idp/punctuate", (request, response) -> {
                     Map<String,Object> attribute = new HashMap<String,Object>();
                     attribute.put("idp",request.params("idp"));
       return new ModelAndView(attribute,"Point.moustache");
   },
    new MustacheTemplateEngine()
   );     
   
   post("posts/:idp/checkPunctuate", (request, respone) ->{
       
       Post p = Post.findById(request.params("idp"));
       Session session = request.session(false);
       String email = session.attribute("user_email");
       User u = User.findFirst("email =?", email);
       String retornar = "";
       Point point = new Point();
       point.set("point",request.queryParams("points"));
       u.add(point);
       p.add(point);
       point.saveIt();
       retornar = "Has puntuado correctamente";
       return retornar;
      } );
        
     get("/cerrar", (request, response) -> {
            node.close();
            return "servidor cerrado!";
        });    
        
        //cierra la base de datos
        after((request, response) -> {
            Base.close();    
        });   
    }   
}