package server.login.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity 
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
	
    private int id;
    private String name;
    private String password;

    @Id  
    @GenericGenerator(name = "generator", strategy = "increment")  
    @GeneratedValue(generator = "generator")  
    @Column(name = "id")  
    public int getId() {
        return id;
    }

    @Column(name = "password")  
    public String getPassword() {
        return password;
    }

    @Column(name = "name")  
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

}
