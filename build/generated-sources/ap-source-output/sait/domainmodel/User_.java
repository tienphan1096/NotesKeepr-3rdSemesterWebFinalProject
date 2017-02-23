package sait.domainmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sait.domainmodel.Activation;
import sait.domainmodel.Note;
import sait.domainmodel.PasswordReset;
import sait.domainmodel.Role;
import sait.domainmodel.Subscription;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-11T12:45:59")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> firstname;
    public static volatile SingularAttribute<User, Character> gender;
    public static volatile SingularAttribute<User, String> phonenumber;
    public static volatile ListAttribute<User, Note> noteList;
    public static volatile SingularAttribute<User, Subscription> subscription;
    public static volatile ListAttribute<User, Role> roleList;
    public static volatile SingularAttribute<User, String> lastname;
    public static volatile SingularAttribute<User, String> password;
    public static volatile ListAttribute<User, Note> noteList1;
    public static volatile SingularAttribute<User, PasswordReset> passwordReset;
    public static volatile SingularAttribute<User, Activation> activation;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> username;

}