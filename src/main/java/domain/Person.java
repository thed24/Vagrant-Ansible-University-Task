package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.json.JSONObject;

@Entity
@Table
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer id;

  @Column
  private String name;

  @Column(name = "is_owner")
  private Boolean isOwner;

  public Person(Integer id, String name, boolean isOwner) {
    this.name = name;
    this.id = id;
    this.isOwner = isOwner;
  }

  Boolean getIsOwner() {
    return isOwner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public JSONObject toJSON() {
    JSONObject personAsJson = new JSONObject();
    personAsJson.put("ID", id);
    personAsJson.put("Name", name);
    personAsJson.put("IsOwner", isOwner.toString());
    return personAsJson;
  }

  @Override
  public int hashCode() {
    return 31 * id + name.hashCode();
  }

  @Override
  public boolean equals(Object givenPerson) {

    if (givenPerson == this) {
      return true;
    }

    if (!(givenPerson instanceof Person)) {
      return false;
    }

    Person person = (Person) givenPerson;
    return this.id.equals(person.id) && this.name.equals(person.name);
  }
}
