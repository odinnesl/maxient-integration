package edu.nesl.maxient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

// TODO convert class to DAO
/**
 * Main entry point to the service
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static final String SCHEDULEDELIMITER = "**********";

    private static String demographicFileName = "NESL_DEMOGRAPHICS_DATA.txt";

    private static String scheduleFileName = "NESL_SCHEDULES_DATA.txt";

    final String connectionUrl = ReadProperties.read("my.string");

    private static final String jdbcQuery = "SELECT\n" +
            "    nm.ID_NUM AS [Unique identifier],\n" +
            "    nm.LAST_NAME AS [Last name],\n" +
            "    nm.FIRST_NAME AS [First name],\n" +
            "    nm.MIDDLE_NAME AS [Middle name],\n" +
            "    nm.PREFERRED_NAME AS [Preferred name],\n" +
            "    bm.BIRTH_DTE AS [Date of birth],\n" +
            "    td2.table_desc AS [Gender],\n" +
            "    bm.GenderPronounDefinitionAppID AS [Pronouns],\n" +
            "    nm.PREFIX AS [Honorific],\n" +
            "    nm.EMAIL_ADDRESS AS [Email address],\n" +
            "    sm.CUR_STUD_DIV AS [Program type],\n" +
            "    am.ADDR_LINE_1 AS [Local address street],\n" +
            "    am.CITY AS [Local city],\n" +
            "    am.STATE AS [Local state],\n" +
            "    am.PostalCode AS [Local zip],\n" +
            "    ph.Phone as [Current phone],\n" +
            "    sm.CURRENT_CLASS_CDE as [Classification]\n" +
            "FROM NAME_MASTER nm\n" +
            "INNER JOIN BIOGRAPH_MASTER bm \n" +
            "    ON bm.ID_NUM = nm.ID_NUM\n" +
            "INNER JOIN TABLE_DETAIL td1 \n" +
            "    ON td1.TABLE_VALUE = bm.ETHNIC_GROUP \n" +
            "       AND td1.COLUMN_NAME = 'ethnic_group'\n" +
            "INNER JOIN TABLE_DETAIL td2 \n" +
            "    ON td2.TABLE_VALUE = bm.GENDER \n" +
            "       AND td2.COLUMN_NAME = 'gender'\n" +
            "INNER JOIN STUDENT_MASTER sm \n" +
            "    ON sm.ID_NUM = nm.ID_NUM\n" +
            "INNER JOIN NameAddressMaster nam \n" +
            "    ON nm.APPID = nam.NameMasterAppID\n" +
            "INNER JOIN AddressMaster am \n" +
            "    ON nam.AddressMasterAppID = am.AppID\n" +
            "OUTER APPLY (\n" +
            "    SELECT TOP 1 phm.PHONE AS Phone\n" +
            "    FROM NamePhoneMaster npm\n" +
            "    INNER JOIN PhoneMaster phm \n" +
            "        ON npm.PhoneMasterAppID = phm.AppID\n" +
            "    WHERE npm.PhoneMasterAppID = nm.APPID\n" +
            "    ORDER BY phm.AppID  -- Order by a unique or preferred column to choose the \"primary\" phone\n" +
            ") ph\n" +
            "WHERE sm.RESID_CDE = 'E'\n" +
            "  AND nam.AddressCodeDefAppID = -2\n" +
            "ORDER BY nm.ID_NUM;\n";

    public static void main(String[] args) {
        logger.info("Processing demograhic and schedule data feeds");
        final Main main = new Main();

        final List<Student> studentIdentifiers = main.retrieveDemographicData();
        main.writeStudentDemographicsToFile(studentIdentifiers, demographicFileName); // TODO update file path

        // now transform per transform strategy
        final List<String> schedules = main.retrieveScheduleData(studentIdentifiers);
        logger.info("Schedule for a student:{}", schedules.getFirst()); //TODO remove sample
        main.writeStudentScheduleToFile(schedules, scheduleFileName);
        logger.info("Processing complete");
    }

    /**
     * Retrieve demographic data from sql server
     */
    public List<Student> retrieveDemographicData() {
        logger.info("Retrieving demographic data from the database");

        final List<Student> students = new ArrayList<>();

        /* Data structure to handle the situation if the field names may change or others may be added: */
       /*
       Requirements for demographicFields: -- need to find address, classification, program
       We'd like to include unique identifier,
       authentication token, last name, first name, middle name, preferred name,
       date of birth, gender,
       local mailing address, local city, local state, local zip, cell phone,
       NELB email address,
       classification (e.g., 1L, 2L, etc.),
       and program (e.g., day, evening, etc.)
       */

        final String[] demographicFields = {
                "Unique identifier",
                "Last name",
                "First name",
                "Middle name",
                "Preferred name",
                "Date of birth",
                "Gender",
                "Email address",
                "Program type",
                "Local address street",
                "Local city",
                "Local state",
                "Local zip",
                "Current phone",
                "Classification"
        };

        // Since this is batch call it's okay not to use a database connection pool
        try (final Connection connection = DriverManager.getConnection(connectionUrl)) {
            logger.info("Connected to SQL Server successfully for demographic feed");

            final Statement statement = connection.createStatement();
            final ResultSet set = statement.executeQuery(jdbcQuery);

            while (set.next()) {
                logger.info("Processing student:{}", set.getString(demographicFields[0]));
                Student student = new Student();

                // TODO replace with Java lambda for SQL Server
                // TODO check field length - enforce MAX length
                // TOOD explore Adapter or utility pattern for conversion and length check

                student.setId(set.getString(Field.UNIQUE_IDENTIFIER.getDescription()));
                student.setLastName(set.getString(Field.LAST_NAME.getDescription()));
                student.setFirstName(set.getString(Field.FIRST_NAME.getDescription()));

                // nulls can appear for middle name and preferred name for some students

                if (set.getString(Field.MIDDLE_NAME.getDescription()) == null ||
                        set.getString(Field.MIDDLE_NAME.getDescription()).compareTo("null") == 0) {
                    student.setMiddleName("");
                } else {
                    student.setMiddleName(set.getString(Field.MIDDLE_NAME.getDescription()));
                }

                if (set.getString(Field.PREFERRED_NAME.getDescription()) == null
                        || set.getString(Field.PREFERRED_NAME.getDescription()).compareTo("null") == 0)
                            { // if preferred name is the same as first name
                    // TODO what if caps
                    student.setPreferredName("");
                } else {
                    if (set.getString(Field.PREFERRED_NAME.getDescription()).
                            equalsIgnoreCase(set.getString(Field.FIRST_NAME.getDescription()))) {
                        logger.info("Setting preferred name to empty for:{}",
                                set.getString(Field.FIRST_NAME.getDescription()));
                        student.setPreferredName("");
                    } else {
                        logger.info("Setting preferred name to empty for:{}",
                                set.getString(Field.FIRST_NAME.getDescription()));
                        student.setPreferredName(set.getString(Field.PREFERRED_NAME.getDescription()));
                    }
                }

                student.setBirthDate(set.getString(Field.DATE_OF_BIRTH.getDescription()).split(" ")[0]);
                student.setGender(set.getString(Field.GENDER.getDescription()));
                student.setEmailAddress(set.getString(Field.EMAIL.getDescription()));
                student.setAuthenticationToken(set.getString(Field.EMAIL.getDescription())); //TODO truncate
                student.setLocalMailingAddress(set.getString(Field.LOCAL_ADDRESS_STREET.getDescription()));
                student.setLocalCity(set.getString(Field.LOCAL_CITY.getDescription()));
                student.setLocalState(set.getString(Field.LOCAL_STATE.getDescription()));
                student.setLocalZip(set.getString(Field.LOCAL_ZIP.getDescription()));

                if (set.getString(Field.CURRENT_PHONE.getDescription()) == null
                        || set.getString(Field.CURRENT_PHONE.getDescription()).compareTo("null") == 0) {
                    student.setLocalPhone("");
                } else {
                    student.setLocalPhone(set.getString(Field.CURRENT_PHONE.getDescription()));
                }

                //student.setLocalPhone(set.getString(Field.CURRENT_PHONE.getDescription()));
                student.setClassifcation(set.getString(Field.CLASSIFICATION.getDescription()));
                students.add(student);
            }

        } catch (final SQLException e) {
            logger.error("Database error while retrieving demographic feed", e);
        }
        return students;
    }

    /**
     * Retrieve schedule data from the database
     */
    public List<String> retrieveScheduleData(final List<Student> students) {

        logger.info("Retrieving schedule data from the database");

        final List<String> schedules = new ArrayList<>();

        final List<String> studentIdentifiers;
        studentIdentifiers = students.stream()
                .map(Student::getId)
                .collect(Collectors.toList()); //TODO why not toList()

        //TODO find path to the variable here:

        Statement statement = null;

        try (final Connection connection = DriverManager.getConnection(connectionUrl)) {
            logger.info("Connected to SQL Server successfully for schedule feed");

            statement = connection.createStatement();

            // Format the data to comply with Maxient requirements

            int lineCount = 0;

            for (String s : studentIdentifiers) {
                logger.info("Processing student:{}", s);

                if (lineCount != 0) {
                    schedules.add(new String(SCHEDULEDELIMITER)); // TODO check
                }

                // add student number
                schedules.add(s);

                final String scheduleQuery = "\n" +
                        "/*Example query for Maxient Student Course Schedule Data*/\n" +
                        "WITH CTE_CLEAN_CRS_CDE as (\n" +
                        "\tSELECT\n" +
                        "\t\tsec.appid\n" +
                        "\t\t, REPLACE(REPLACE(REPLACE(LTRIM(RTRIM(sec.CRS_CDE)),'  ',' '+ CHAR(7)) , CHAR(7)+' ',''), CHAR(7),'') AS CLEAN_CRS_CDE\n" +
                        "\tFROM SECTION_MASTER sec\n" +
                        "\tWHERE CHARINDEX('  ',sec.CRS_CDE) > 0\n" +
                        ")\n" +
                        "\n" +
                        "SELECT\n" +
                        "\tsch.ID_NUM\n" +
                        "\t, ccc.CLEAN_CRS_CDE as 'CRS_CDE'\n" +
                        "\t, sec.CRS_TITLE\n" +
                        "\t, REPLACE(LTRIM(RTRIM(ss.MONDAY_CDE + ss.TUESDAY_CDE + ss.WEDNESDAY_CDE + ss.THURSDAY_CDE + ss.FRIDAY_CDE)),' ','') as 'Days'\n" +
                        "\t, FORMAT(ss.BEGIN_TIM,'hh:mm') + ' - ' + FORMAT(ss.END_TIM,'hh:mm tt')as 'Time'\n" +
                        "\t, nfbv1.FIRST_MI_LAST as 'Instructor'\n" +
                        "\t, ss.ROOM_CDE\n" +
                        "FROM STUDENT_CRS_HIST sch\n" +
                        "\tinner join SECTION_MASTER sec on sec.CRS_CDE = sch.CRS_CDE and sec.TRM_CDE = sch.TRM_CDE and sec.YR_CDE = sch.YR_CDE\n" +
                        "\tinner join SECTION_SCHEDULES ss on ss.CRS_CDE = sec.CRS_CDE and ss.TRM_CDE = sec.TRM_CDE and ss.YR_CDE = sec.YR_CDE\n" +
                        "\tinner join NAME_FORMAT_BASIC_VIEW nfbv1 on nfbv1.ID_NUM = sec.LEAD_INSTRUCTR_ID\n" +
                        "\tinner join YEAR_TERM_TABLE ytt on ytt.YR_CDE = sec.YR_CDE and ytt.TRM_CDE = sec.TRM_CDE\n" +
                        "\tinner join CTE_CLEAN_CRS_CDE ccc on ccc.appid = sec.APPID\n" +
                        "WHERE 1=1\n" +
                        "\t and sch.ID_NUM = " + s +
                        "\tand ytt.TRM_END_DTE > CURRENT_TIMESTAMP\n" +
                        "\tand sch.TRANSACTION_STS NOT IN ('D','W')\n" +
                        "ORDER BY\n" +
                        "\tsch.ID_NUM\n" +
                        "\t, ss.CRS_CDE\n" +
                        "\t, ss.SEQ_NUM_2\n" +
                        ";";

                /* Data structure to handle the situation if the field names may change or others may be added: */
                final String[] scheduleFields = {
                        "ID_NUM",
                        "CRS_CDE",
                        "CRS_TITLE",
                        "Days",
                        "Time",
                        "Instructor",
                        "ROOM_CDE"
                };

                final ResultSet resultSet = statement.executeQuery(scheduleQuery);

                while (resultSet.next()) {
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < scheduleFields.length; i++) {
                        if (i == 0) { // append course number
                            //sb.append(resultSet.getString(scheduleFields[0])); //?
                        } else {
                            if (resultSet.getString(scheduleFields[i]) != null)
                                sb.append(resultSet.getString(scheduleFields[i]).replaceAll("null", ""));
                            sb.append(" ");
                        }
                    }
                    schedules.add(sb.toString());
                }
                resultSet.close(); // close ResulSet
                lineCount++;
            }
        } catch (final SQLException e) {
            logger.error("Database error while retrieving schedule feed", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Error closing statement:", e);
                }
            }
        }
        return schedules;
    }


    public void writeStudentDemographicsToFile(final List<Student> students, final String filePath)  {
        try (final BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.US_ASCII))) {
            for (final Student student : students) {
                writer.write(student.toString());
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            logger.error("Exception writing demographics file:{}", filePath, e);
        }
    }

    public void writeStudentScheduleToFile(final List<String> studentSchedule, final String path)  {
        final Path filePath = Paths.get(path);
        try {
            Files.write(filePath, studentSchedule);
            logger.info("Schedule file written successfully to:{} ", path);
        } catch (IOException e) {
            logger.error("Error writing schedule file:", e);
        }
    }
}

/**
 * Database fields to pick up - externalized for type safety
 */
 enum Field {
    UNIQUE_IDENTIFIER("Unique identifier"),
    LAST_NAME("Last name"),
    FIRST_NAME("First name"),
    MIDDLE_NAME("Middle name"),
    PREFERRED_NAME("Preferred name"),
    DATE_OF_BIRTH("Date of birth"),
    GENDER("Gender"),
    EMAIL("Email address"),
    PROGRAM_TYPE("Program type"),
    LOCAL_ADDRESS_STREET("Local address street"),
    LOCAL_CITY("Local city"),
    LOCAL_STATE("Local state"),
    LOCAL_ZIP("Local zip"),
    CURRENT_PHONE("Current phone"),
    CLASSIFICATION("Classification");

    private final String description;

    Field(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
