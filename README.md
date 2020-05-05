# platzivideoexample
Example project for the course: Curso de Programación Funcional con Scala, I added a basic UI to interact with the routes

To deploy on Heroku
1. First run (and make sure this command passed successfully) : 
```bash
sbt compile stage 
```
2. Run:

```bash
heroku create -s cedar
git push heroku master
```
## Custom host
When you get the host for your Heroku app, please add this to:

```
play.filters.hosts on conf/application.conf
```

Thanks