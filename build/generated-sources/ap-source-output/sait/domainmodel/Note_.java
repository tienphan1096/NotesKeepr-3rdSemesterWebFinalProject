package sait.domainmodel;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sait.domainmodel.User;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-12-11T12:45:59")
@StaticMetamodel(Note.class)
public class Note_ { 

    public static volatile SingularAttribute<Note, User> owner;
    public static volatile ListAttribute<Note, User> userList;
    public static volatile SingularAttribute<Note, String> contents;
    public static volatile SingularAttribute<Note, Integer> noteid;
    public static volatile SingularAttribute<Note, Date> datecreated;

}