package sait.domainmodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sait.domainmodel.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-11T12:45:59")
@StaticMetamodel(Activation.class)
public class Activation_ { 

    public static volatile SingularAttribute<Activation, String> uuid;
    public static volatile SingularAttribute<Activation, User> user;
    public static volatile SingularAttribute<Activation, String> username;

}