import java.time.LocalDate;

/**
 * Person represent information about person
 * Created by oldrichkonecny on 13.05.16.
 */
public class Person {

    private Long id;
    private String name;
    private String surname;
    private LocalDate born;



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public LocalDate getBorn() { return born; }
    public void setBorn(LocalDate born) { this.born = born; }

    @Override
    public String toString() {
        return "Prisoner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", born=" + born +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (surname != null ? !surname.equals(person.surname) : person.surname != null) return false;
        return born != null ? born.equals(person.born) : person.born == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (born != null ? born.hashCode() : 0);
        return result;
    }
}
