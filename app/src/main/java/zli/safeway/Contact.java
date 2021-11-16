package zli.safeway;


public class Contact {
    private String id;
    private String name;
    private String firstname;
    private String phonenumber;

    public Contact(String id, String name, String firstname, String phonenumber){
        this.id = id;
        this.name = name;
        this.firstname = firstname;
        this.phonenumber = phonenumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
