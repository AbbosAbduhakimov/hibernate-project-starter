package com.abbos.util;

import com.abbos.converter.BirthdayConverter;
import com.abbos.entitiy.Company;
import com.abbos.entitiy.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {



//        BlockingQueue<Connection> connectionPool = null;
//        DriverManager.getConnection("db.url",
//                "db.username","db.password");
    // Session => wrap Connection
    // SessionFactory => wrap Connection pool

    public static SessionFactory buildSessionFactory(){

        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAttributeConverter(new BirthdayConverter());
//     register to converting type
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.configure();
        return configuration.buildSessionFactory();
    }

}
