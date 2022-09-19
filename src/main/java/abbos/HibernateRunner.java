package com.abbos;

import com.abbos.entitiy.*;
import com.abbos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner {


    public static void main(String[] args) throws SQLException {


        Company company = Company.builder()
                .name("Google")
                .build();


        User user = User.builder()
                .username("johndoe2@mail.ru")
                .firstname("John")
                .lastname("Doe")
                .infoUser("""
                            {
                                "name": "John5",
                                "age": "24"
                            }
                            """)
                .birthDate(new BirthDay(LocalDate.of(1996,12,29)))
                .age(25)
                .personalInfo(PersonalInfo
                        .builder()
                        .contact("+998996514535")
                        .email("johnMa45@mail.ru")
                        .visitDate(LocalDate.of(2022,9,15))
                        .build())
//                .company(company)
                .role(Role.ADMIN)
                .build();





        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.save(company);
            session.save(user);

            session.get(User.class,1L);
//            session.save(company);
            session.save(user);
            session.get(Company.class,1L);




            /** update user comes after commit or close transaction if such object not found throwing Exception
             session.update(user);*/

            /** during a transaction comes select query if such object has found object will update
             * if not found object will save don't throw Exception
             * session.saveOrUpdate(user);*/


            /** during a transaction comes select query if such object has found object will delete
             *  don't throw Exception
             *  session.delete(user);
             * */


            /** in one application there will be one session factor, and it will have a meta model,
             *  the basic type will have all our entities in the meta model, each entity will have its own entity persister
             *  that stores entities in the map, the entity persister mappings ours into a skl model or wheel
             *  in time mapping has working EventListeners
             *  MetaModel = Map<User.class,Id> EntityPersister HashMap<>() other CRUD...
             *  session.get(User.class,"john1@mail.ru");*/

            /** FirstLevelCache
             *
             *  Translation results
             * only one request will be executed in this request, because everything is a first level cache, each session will have
             * peristercontext, in turn, persistercontext stores our entities in the map, each persistercontext will have a link to its session,
             * the session receives an entity from the method model and this entity is stored in persistenceContext
             *
             * HashMap<key=EntityKey,Value= User.class> (firsLevelCache) PersistentContext  = Session <= (SessionFactory-MetaModel) => Map<User.class,Id> EntityPersistent
             *
             * session.get(User.class,"john2@mail.ru");
             * session.get(User.class,"john2@mail.ru");
             *
             * */


            /** delete caches
             * session.evict(user)
             * session().clear()
             * session.close()
             * */
            session.getTransaction().commit();

            System.out.println();


            Session session2 = sessionFactory.openSession();

            try(session2) {
                session2.beginTransaction();




                session2.getTransaction().commit();

            }

        }

    }

}
