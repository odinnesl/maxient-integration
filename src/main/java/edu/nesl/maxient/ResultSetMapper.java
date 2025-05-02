package edu.nesl.maxient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetMapper {

    public static void test() {
        Statement s = null;
        Student student = new Student();
        try (ResultSet set = s.executeQuery("jdbcQuery")) {
            if (set.next()) {
                student.setId(ResultSetMapper.getString(set, Field.UNIQUE_IDENTIFIER));
            }
        } catch (Exception e) {

        }

    }

    public static String getString(ResultSet rs, Field field) throws SQLException {
        return rs.getString(field.getColumnName());
    }


    public enum Field {
        UNIQUE_IDENTIFIER("Unique identifier"),
        LAST_NAME("Last name"),
        FIRST_NAME("First name"),
        MIDDLE_NAME("Middle name"),
        PREFERRED_NAME("Preferred name"),
        DATE_OF_BIRTH("Date of birth"),
        GENDER("Gender"),
        EMAIL_ADDRESS("Email address"),
        PROGRAM_TYPE("Program type"),
        LOCAL_ADDRESS_STREET("Local address street"),
        LOCAL_CITY("Local city"),
        LOCAL_STATE("Local state"),
        LOCAL_ZIP("Local zip"),
        CURRENT_PHONE("Current phone"),
        CLASSIFICATION("Classification");

        private final String columnName;

        Field(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

}
