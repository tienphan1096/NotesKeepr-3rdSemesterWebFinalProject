package sait.domainmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sait.domainmodel.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-11T12:45:59")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile ListAttribute<Role, User> userList;
    public static volatile SingularAttribute<Role, Integer> roleid;
    public static volatile SingularAttribute<Role, String> rolename;

}