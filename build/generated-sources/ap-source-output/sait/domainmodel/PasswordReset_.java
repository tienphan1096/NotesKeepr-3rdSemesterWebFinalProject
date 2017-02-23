package sait.domainmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sait.domainmodel.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-11T12:45:59")
@StaticMetamodel(PasswordReset.class)
public class PasswordReset_ { 

    public static volatile SingularAttribute<PasswordReset, String> uuid;
    public static volatile SingularAttribute<PasswordReset, User> user;
    public static volatile SingularAttribute<PasswordReset, String> username;

}