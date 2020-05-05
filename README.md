# platzivideoexample
Example project for the course: Curso de Programación Funcional con Scala, I added a basic UI to interact with the routes

To deploy on Heroku
1. First run (and make you this command passed successfully) : 
```bash
sbt compile stage 
```
2. Run:

```bash
heroku create -s cedar
git push heroku master
```

Thanks