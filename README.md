# platzivideoexample
Example project for the course: Curso de Programación Funcional con Scala, I added a basic UI to interact with the routes

To deploy on Heroku
1. Make sure you run : 
		sbt compile stage
2. Run:
		heroku create -s cedar
		git push heroku master
