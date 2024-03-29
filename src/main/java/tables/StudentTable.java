package tables;


import objects.Student;
import utils.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class StudentTable extends AbsTable {
    private static final String groupSearch = "оптика";
    private final static String TABLE_NAME = "students";

    public StudentTable() {
        super(TABLE_NAME);
        columns = new HashMap<>();
        columns.put("id", "INT NOT NULL AUTO_INCREMENT PRIMARY KEY");
        columns.put("fullName", "varchar(100)");
        columns.put("sex", "varchar(10)");
        columns.put("idGroup", "int");
        create();
    }

    public ArrayList<Student> selectAll() {
        String sqlQuery = String.format("SELECT * FROM %s", tableName);
        return selectByQuery(sqlQuery);
    }

    private ArrayList<Student> selectByQuery(String sqlQuery) {
        ArrayList<Student> students = new ArrayList<>();
        ResultSet rs = db.executeRequestWithAnswer(sqlQuery);
        try {
            // Перебор строк с данными
            while (rs.next()) {
                //Создать объект устройство и добавление его в результирующий массив
                students.add(new Student(
                        rs.getLong("id"),
                        rs.getString("fullName"),
                        rs.getString("sex"),
                        rs.getInt("idGroup")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return students;
    }

    public void insert(Student student) {
        String sqlQuery = String.format("INSERT INTO %s (fullName, sex, idGroup) " +
                        "VALUES ( '%s', '%s', '%d')",
                tableName,
                student.getFullName(),
                student.getSex(),
                student.getIdGroup());
        db.executeRequest(sqlQuery);
    }

    public void selectAllStudents() {
        Tools.delimeter();
        System.out.print("Всего студентов:");
        final String sqlQuery = String.format("SELECT COUNT(*) FROM %s", tableName);
        select(sqlQuery);
    }

    public void selectAllFemaleStudents() {
        Tools.delimeter();
        System.out.println("Все студентки:");
        final String sqlQuery = "SELECT fullName FROM students WHERE sex='ж'";
        select(sqlQuery);
    }

    public void selectAllGroupsWithCuratorsAndStudents() {
        Tools.delimeter();
        System.out.println("Все группы с кураторами и студентами:");
        final String sqlQuery = "SELECT students.id, students.fullName, students.sex, " +
                "group1.groupName, curator.curatorName" +
                " FROM students JOIN group1 ON students.idGroup=group1.id" +
                " JOIN curator ON group1.idCurator=curator.id ORDER BY students.id ASC;";
        select(sqlQuery);
    }

    public void selectSearchGroup() {
        Tools.delimeter();
        System.out.println("Все студенты из заданой группы:");
        final String sqlQuery = String.format("SELECT fullName " +
                "FROM students WHERE idGroup=(SELECT id FROM group1 WHERE groupName='" + groupSearch + "')");
        select(sqlQuery);
    }
}
