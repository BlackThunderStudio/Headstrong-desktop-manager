package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.CourseCategory;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**********************************
 * course category model collection
 *********************************/
public class DBCourseCategory implements IDataAccessObject<CourseCategory> {

    private DBConnect dbConnect;

    @Override
    public List<CourseCategory> getAll() throws ModelSyncException{
        List<CourseCategory> courseCategories = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            String getAllCourseCategoriesQuery = "SELECT [id], [name] FROM [course_categories]";
            ResultSet ccRS = dbConnect.getFromDataBase(getAllCourseCategoriesQuery);
            while(ccRS.next())
                courseCategories.add(new CourseCategory(ccRS.getInt("int"),
                                                        ccRS.getString("name")));
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load companies.", e);
        }
        return courseCategories;
    }

    @Override
    public CourseCategory getById(int id) throws ModelSyncException{
        CourseCategory courseCategory = null;
        try{
            dbConnect = new DBConnect();
            String getByIdCourseCategoriesQuery = "SELECT * FROM [course_categories] WHERE [id] = " + id + ";";
            ResultSet rs = dbConnect.getFromDataBase(getByIdCourseCategoriesQuery);
            rs.next();
            courseCategory = new CourseCategory(rs.getInt("id"),
                                                rs.getString("name"));
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not retrieve object by ID", e);
        }
        return courseCategory;
    }

    @Override
    public CourseCategory create(CourseCategory newCourseCategory) throws ModelSyncException{
        try{
            dbConnect = new DBConnect();
            String createCourseCategoryQuery = "INSERT INTO course_categories(name) VALUES (?);";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(createCourseCategoryQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newCourseCategory.getName());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newCourseCategory.setId(generatedKeys.getInt(1));
                } else {
                    throw new ModelSyncException("Creating course category failed. No ID retrieved!");
                }
            }
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not create new course category!", e);
        }
        return newCourseCategory;
    }

    @Override
    public void update(CourseCategory courseCategory) throws ModelSyncException{
        try{
            dbConnect = new DBConnect();
            String updateCourseCategoryQuery = "UPDATE course_categories SET name=? WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(updateCourseCategoryQuery);
            preparedStatement.setString(1, courseCategory.getName());
            dbConnect.uploadSafe(preparedStatement);
        } catch (SQLException | ConnectionException e) {
            throw new ModelSyncException("WARNING! Could not update the course category of id [" + courseCategory.getId() + "]!", e);
        }
    }

    @Override
    public void delete(CourseCategory courseCategory) throws ModelSyncException{
        try{
            dbConnect = new DBConnect();
            String deleteCourseCategoryQuery = "DELETE FROM course_categories WHERE id=?;";
            PreparedStatement preparedStatement = dbConnect.getConnection().prepareStatement(deleteCourseCategoryQuery);
            preparedStatement.setInt(1, courseCategory.getId());
            preparedStatement.execute();
        } catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Couldn't delete course category of id=" + courseCategory.getId(), e);
        }
    }

}
