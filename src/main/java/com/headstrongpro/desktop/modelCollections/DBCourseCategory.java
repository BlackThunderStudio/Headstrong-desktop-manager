package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.CourseCategory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1062085 on 25-Apr-17.
 */
public class DBCourseCategory implements IDataAccessObject<CourseCategory> {
    //table name maybe not exact
    private List<CourseCategory> courseCategories;
    private DBConnect dbConnect;
    private boolean isLoaded;

    public DBCourseCategory() throws ModelSyncException{
        courseCategories = new ArrayList<>();
        isLoaded = false;
    }

    public void load() throws ModelSyncException{
        courseCategories = new ArrayList<>();
        try{
            dbConnect = new DBConnect();
            String query = "SELECT [id], [name] FROM [course_categories]";
            ResultSet courseCategoriesRS = dbConnect.getFromDataBase(query);
            while(courseCategoriesRS.next()){
                courseCategories.add(new CourseCategory(courseCategoriesRS.getInt("id"),
                                                        courseCategoriesRS.getString("name")));
            }
            isLoaded = true;
        }catch (ConnectionException | SQLException e) {
            throw new ModelSyncException("Could not load course categories.", e);
        }
    }

    @Override
    public List<CourseCategory> getAll() throws ModelSyncException{
        if(!isLoaded)
            load();
        return courseCategories;
    }

    @Override
    public CourseCategory getById(int id) throws ModelSyncException{
        if(!isLoaded)
            load();
        return courseCategories.stream().filter(o -> o.getId() == id).findFirst().get();
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
        } finally {
            courseCategories.add(newCourseCategory);
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
            throw new ModelSyncException("Couldn't delete order of id=" + courseCategory.getId(), e);
        } finally {
            courseCategories.removeIf(p -> p.getId() == courseCategory.getId());
        }
    }

}
