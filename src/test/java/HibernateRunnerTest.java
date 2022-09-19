import com.abbos.entitiy.*;
import com.abbos.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {


    @Test
    void checkOneToOne() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        User user = User.builder()
                .username("test")
                .personalInfo(PersonalInfo
                        .builder()
                        .email("test@mail.ru")
                        .build())
                .build();

        Profile profile = Profile.builder()
                .language("uz")
                .build();


        profile.setUser(user);

        session.save(user);

        System.out.println();
        session.getTransaction().commit();

    }


    @Test
    void checkManyToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

//        User user = User.builder()
//                .username("test")
//                .personalInfo(PersonalInfo
//                        .builder()
//                        .email("test@mail.ru")
//                        .build())
//                .build();
        User user = session.get(User.class, 1L);

        Chat chat = Chat.builder()
                .name("newChat")
                .build();


        if (user.getCompanyId() == 1) {
            user.getCompany().getUsers().toArray();
        }

        session.save(chat);

        session.getTransaction().commit();
    }


    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1L);
        System.out.println("");


        session.getTransaction().commit();
    }



    @Test
    void checkSingleTableInherit() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

//        var google = Company.builder()
//                .name("Google")
//                .build();
//        session.save(google);
//
//        Programmer programmer = Programmer.builder()
//                .username("john@gmail.com")
//                .language(Language.JAVA)
//                .company(google)
//                .build();
//        session.save(programmer);
//
//        Manager manager = Manager.builder()
//                .username("mark@gmail.com")
//                .projectName("Starter")
//                .company(google)
//                .build();
//        session.save(manager);
//        session.flush();
//
//        session.clear();

//        var programmer1 = session.get(Programmer.class, 1L);
//        var manager1 = session.get(User.class, 2L);
//        System.out.println();


        session.getTransaction().commit();
    }


    @Test
    void checkHql() {


        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var list = session.createQuery(
                        "select u from User u " +
                                "join u.company c " +
                                "where u.personalInfo.contact = :contact and c.name = :companyname " +
                                "order by u.personalInfo.visitDate desc ",
                        User.class)
                .setParameter("contact", "99879")
                .setParameter("companyname", "Google")
                .list();

        session.getTransaction().commit();
    }


    @Test
    void checkHqlNamedQuery() {


        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var list = session.createNamedQuery(
                        "findUserByName",
                        User.class)
                .setParameter("contact", "99879")
                .setParameter("companyname", "Google")
                .list();


        var updateRow = session.createQuery("UPDATE User u SET u.role = 'ADMIN'").executeUpdate();

        session.createNativeQuery("select u.* from users where role = 'ADMIN'");


        session.getTransaction().commit();
    }



    @Test
    void addUserToCompany() {

        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Meta")
                .build();


        User user = User.builder()
                .username("mark@mail.ru")
                .firstname("Mark")
                .lastname("Doe")
                .infoUser("""
                        {
                            "name": "John5",
                            "age": "24"
                        }
                        """)
                .birthDate(new BirthDay(LocalDate.of(1996, 12, 29)))
                .age(25)
                .personalInfo(PersonalInfo
                        .builder()
                        .contact("+9989965199879")
                        .email("mark@mail.ru")
                        .visitDate(LocalDate.of(2022, 9, 15))
                        .build())
                .build();


        company.addUser(user);
        session.save(company);

        session.getTransaction().commit();

    }

    @Test
    void checkProblemNPlus1(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();


        var users = session.createQuery("select u from User u", User.class)
                .list();

        users.forEach(user -> System.out.println(user.getPayments().size()));
        users.forEach(user -> System.out.println(user.getCompany().getName()));


        session.getTransaction().commit();
    }



    @Test
    void checkSolvingProblemNPlusOneUsingFetch(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        session.enableFetchProfile("withCompanyAndPayment");


//        var users = session.createQuery("select u from User u join  u.payments", User.class)
//                .list();
//        users.forEach(user -> System.out.println(user.getCompany().getName()));
//        users.forEach(user -> System.out.println(user.getPayments().size()));

        User user = session.get(User.class, 1L);
        System.out.println(user.getPayments().size());
        System.out.println(user.getClass().getName());


        session.getTransaction().commit();
    }


    @Test
    void checkEntityGraph(){
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();
        session.enableFetchProfile("withCompanyAndPayment");

        var userGraph = session.createEntityGraph(User.class);
        userGraph.addAttributeNodes("company", "userChats");
//        var userChatsSubgraph = userGraph.addSubgraph("userChats", UserChat.class);
//        userChatsSubgraph.addAttributeNodes("chat");

        Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("WithCompanyAndChat")
//                GraphSemantic.LOAD.getJpaHintName(), userGraph
        );
        var user = session.find(User.class, 1L,properties);
        System.out.println(user.getCompany().getName());
        System.out.println(user.getUserChats().size());
//            System.out.println(user.getPayments().size());

        var users = session.createQuery(
                        "select u from User u " +
                                "where id = 1", User.class)
//                    .setHint(GraphSemantic.LOAD.getJpaHintName(), session.getEntityGraph("WithCompanyAndChat"))
                .setHint(GraphSemantic.LOAD.getJpaHintName(), userGraph)
                .list();
        users.forEach(it -> System.out.println(it.getUserChats().size()));
        users.forEach(it -> System.out.println(it.getCompany().getName()));

        session.getTransaction().commit();
    }





    @Test
    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.getResultSet();
        resultSet.getString("username");
        resultSet.getString("firstname");
        resultSet.getString("lastname");

        Class<User> clazz = User.class;

        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();

        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));
    }


    @Test
    void checkSaveReflectionApi() throws SQLException, IllegalAccessException {


        // hibernate actively uses Reflection Api for formatting sql(hql) query
        String query = """
                insert
                into
                (%s)
                values
                (%s)
                """;

        // where used %s inserted dynamic table or values

        User user = User.builder()
                .username("john@mail.ru")
                .firstname("John")
                .lastname("Doe")
                .age(25)
                .build();

        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());


        String columnName = Arrays.stream(user.getClass().getDeclaredFields())
                // only for where used Column annotation
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(Collectors.joining(", "));


        String columnValues = Arrays.stream(user.getClass().getDeclaredFields())
                .map(field -> "?")
                .collect(Collectors.joining(", "));


        System.out.println(query.formatted(tableName, columnName, columnName));


        Connection connection = null;

        PreparedStatement preparedStatement = connection.prepareStatement(query.formatted(tableName, columnName, columnName));

        for (Field field : user.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            preparedStatement.setObject(1, field.get(user));
        }
    }
}
