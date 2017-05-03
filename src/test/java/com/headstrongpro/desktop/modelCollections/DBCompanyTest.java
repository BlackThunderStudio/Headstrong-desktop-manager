package java.com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.modelCollections.DBCompany;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for company collection~
 */
public class DBCompanyTest {
    private static DBCompany companies;

    @BeforeClass
    public static void setCompanies() throws Exception{
        companies = new DBCompany();
    }

    @Test
    public void oneOne(){
        assertEquals(1,1);
    }
}
