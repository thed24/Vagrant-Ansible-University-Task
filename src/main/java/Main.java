import database.HibernatePersonRepository;
import domain.PersonService;
import domain.Repository;
import http.support.Server;

class Main {

  public static void main(String[] args) {
    Repository repository = new HibernatePersonRepository();
    PersonService personService = new PersonService(repository);
    Server server = new Server(personService, 8080, args[0]);
    server.start();
  }
}
