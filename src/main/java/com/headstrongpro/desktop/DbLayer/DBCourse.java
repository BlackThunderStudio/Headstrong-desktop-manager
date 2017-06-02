package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.DbLayer.util.ActionType;
import com.headstrongpro.desktop.DbLayer.util.IDataAccessObject;
import com.headstrongpro.desktop.DbLayer.util.Synchronizable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * DB Courses
 */
public class DBCourse extends Synchronizable implements IDataAccessObject<Course> {

    private DBConnect dbConnect;
    private Date timestamp;

    public DBCourse() {
        timestamp = new Date(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public ObservableList<Course> getAll() throws ModelSyncException {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String selectQuery = "SELECT * FROM [courses];";
        try {
            dbConnect = new DBConnect();
            ResultSet resultSet = dbConnect.getFromDataBase(selectQuery);
            while (resultSet.next()) {
                courses.add(create(resultSet));
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load courses.", e);
        }
        return courses;
    }

    @Override
    public Course getById(int id) throws ModelSyncException {
        Course course = null;
        String selectQuery = "SELECT * FROM [courses] WHERE id = " + id + ";";
        try {
            dbConnect = new DBConnect();
            ResultSet resultSet = dbConnect.getFromDataBase(selectQuery);
            if (resultSet.next()) {
                course = create(resultSet);
            }
            timestamp = setTimestamp();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve the course!", e);
        }
        return course;
    }

    @Override
    public Course persist(Course object) throws ModelSyncException {
        try {
            dbConnect = new DBConnect();
            //language=TSQL
            String createCourseQuery = "INSERT INTO courses (name, description) VALUES (?, ?)";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createCourseQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getName());
            preparedStatement.setString(2, object.getDescription());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setId(generatedKeys.getInt(1));
                    logChange("courses", object.getId(), ActionType.CREATE);
                } else {
                    throw new ModelSyncException("Creating course failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create new course!", e);
        }
        return object;
    }

    @Override
    public void update(Course object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String updateCourseQuery = "UPDATE courses SET name = ?, description = ? WHERE id = ?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(updateCourseQuery);
                preparedStatement.setString(1, object.getName());
                preparedStatement.setString(2, object.getDescription());
                preparedStatement.setInt(3, object.getId());
                dbConnect.uploadSafe(preparedStatement);
                logChange("courses", object.getId(), ActionType.UPDATE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not update course of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    public void delete(Course object) throws ModelSyncException, DatabaseOutOfSyncException {
        if (verifyIntegrity(object.getId())) {
            try {
                dbConnect = new DBConnect();
                //language=TSQL
                String deleteCourseQuery = "DELETE FROM courses WHERE id = ?;";
                PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(deleteCourseQuery);
                preparedStatement.setInt(1, object.getId());
                preparedStatement.execute();
                logChange("courses", object.getId(), ActionType.DELETE);
            } catch (ConnectionException | SQLException e) {
                throw new ModelSyncException("WARNING! Could not delete course of ID: " + object.getId() + " !", e);
            }
        } else {
            throw new DatabaseOutOfSyncException();
        }
    }

    @Override
    protected boolean verifyIntegrity(int itemID) throws ModelSyncException {
        return verifyIntegrity(itemID, timestamp, "courses");
    }

    private Course create(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt("id"));
        course.setName(resultSet.getString("name"));
        course.setDescription(resultSet.getString("description"));
        return course;
    }
}
