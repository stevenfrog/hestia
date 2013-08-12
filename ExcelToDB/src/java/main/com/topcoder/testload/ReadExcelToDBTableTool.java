/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.testload;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Load the excel file data to database.
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is thread safe.
 * </p>
 *
 * v1.1 - Export and Search Filter
 *      - populated added fields to DB
 * v1.2 - Adjustments Freight and Branch Overrides
 *      - updated insertPricingAdjustment to insert new data
 * v1.3 - Hestia War Room Enhancements
 *      - update method to insert my pricing and user filter record
 * @author TCSASSEMBLYER
 * @version 1.3
 */
public class ReadExcelToDBTableTool {

    /**
     * the driver class name used to create db connection.
     */
    private static final String DRIVER_CLASS;

    /**
     * The connection string to create db connection.
     */
    private static final String CONNECTION_STRING;

    /**
     * the user name used to create db connection.
     */
    private static final String USERNAME;

    /**
     * the password used to create db connection.
     */
    private static final String PASSWORD;

    /**
     * the excel file path to load.
     */
    private static final String PATH = "test_files/";

    /**
     * Initialize database parameters.
     */
    static {
        Properties config = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(PATH + "db.properties");
            config.load(stream);
        } catch (IOException e) {
            // Ignore
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                // Ignore
            }
        }

        DRIVER_CLASS = config.getProperty("driverClassName");
        CONNECTION_STRING = config.getProperty("connectionString");
        USERNAME = config.getProperty("username");
        PASSWORD = config.getProperty("password");
    }

    /**
     * Create ReadExcelToDBTableTool instance.
     */
    public ReadExcelToDBTableTool() {
    }

    /**
     * Load the excel 2007 file and insert the data in file to db.
     *
     * @param filePath
     *            the Excel file path
     * @throws Exception
     *             error occurs during loading.
     */
    public void loadExcel2007ToDB(String filePath) throws Exception {
        InputStream is = null;

        try {
            is = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(is);

            // loop all the sheets in excel file.
            for (int i = 0; i <= 33; i++) {
                XSSFSheet sheet = wb.getSheetAt(i);

                List<List<String>> batch = new ArrayList<List<String>>();
                boolean isFirstRow = true;

                // Read the data from each sheet to list.
                for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {
                    if (isFirstRow) {
                        isFirstRow = false;
                        rit.next();
                        continue;
                    }
                    XSSFRow row = (XSSFRow) rit.next();
                    List<String> datas = new ArrayList<String>();
                    for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
                        XSSFCell cell = (XSSFCell) row.getCell(k);
                        String value = getValue2007(cell);
                        datas.add(value);
                    }
                    batch.add(datas);
                }

                // For each sheet, insert data to db.
                if (i == 0) {
                    insertBulkState(batch);
                } else if (i == 1) {
                    insertBulkBaseEntity("District", batch);
                } else if (i == 2) {
                    insertBulkBaseEntity("Region", batch);
                } else if (i == 3) {
                    insertBulkProductType(batch);
                } else if (i == 4) {
                    insertBulkMarket(batch);
                } else if (i == 5) {
                    insertBulkBranch(batch);
                } else if (i == 6) {
                    insertBulkCategories(batch);
                } else if (i == 7) {
                    insertVendors(batch);
                } else if (i == 8) {
                    insertShipPoints(batch);
                } else if (i == 9) {
                    insertBulkProduct(batch);
                } else if (i == 10) {
                    insertBulkUser(batch);
                } else if (i == 11) {
                    insertBulkRole(batch);
                } else if (i == 12) {
                    insertBulkDestination(batch);
                } else if (i == 13) {
                    insertBulkOrigination(batch);
                } else if (i == 14) {
                    insertOriginationToStateFreightRate(batch);
                } else if (i == 15) {
                    insertOriginationToDestinationFreight(batch);
                } else if (i == 16) {
                    insertPricingRecord(batch);
                } else if (i == 17) {
                    insertCWPricingRecord(batch);
                } else if (i == 18) {
                    insertNewPPT(batch);
                } else if (i == 19) {
                    insertNewListAndMultiplier(batch);
                } else if (i == 20) {
                    insertPricingAdjustment(batch);
                } else if (i == 21) {
                    insertNewFreightRate(batch);
                } else if (i == 22) {
                    insertBranchOverride(batch);
                } else if (i == 23) {
                    insertMyPricing(batch);
                } else if (i == 24) {
                    insertRelateTable(batch, "User_Role");
                } else if (i == 25) {
                    insertRelateTable(batch, "User_Branch");
                } else if (i == 26) {
                    insertUserFilterRecord(batch);
                } else if (i == 27) {
                    insertUserFilterRecordHas(batch, "UserFilterRecord_ProductType");
                } else if (i == 28) {
                    insertUserFilterRecordHas(batch, "UserFilterRecord_Vendor");
                } else if (i == 29) {
                    insertUserFilterRecordHas(batch, "UserFilterRecord_Branch");
                } else if (i == 30) {
                    insertUserFilterRecordHas(batch, "UserFilterRecord_Category");
                } else if (i == 31) {
                    insertUserFilterRecordHas(batch, "UserFilterRecord_Market");
                } else if (i == 32) {
                    insertRelateTable(batch, "Origination_ProductType");
                } else if (i == 33) {
                    insertBulkStateLookup(batch);
                }
            }

        } finally {
            if (is != null) {
                is.close();
            }
        }

        System.out.println("Load excel data to db successfully!");
    }

    /**
     * Populate the State table with state data.
     *
     * @param datas
     *            the list of state data
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkState(List<List<String>> datas) throws Exception {
        String INSERT_STATE_SQL = "INSERT INTO State VALUES(?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_STATE_SQL);
            for (List<String> ds : datas) {
                pstmt.setString(1, ds.get(0).trim());
                pstmt.setString(2, ds.get(1));
                pstmt.setString(3, ds.get(2).trim());

                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } finally {
            conn.close();
        }
    }

    /**
     * Populate the State_Lookup table with state data.
     *
     * @param datas
     *            the list of State_Lookup data
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkStateLookup(List<List<String>> datas) throws Exception {
        String INSERT_STATE_SQL = "INSERT INTO State_Lookup VALUES(?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_STATE_SQL);
            for (List<String> ds : datas) {
                pstmt.setString(1, ds.get(0).trim());
                pstmt.setInt(2, parseInt(ds.get(1)));
                pstmt.setString(3, ds.get(2).trim());

                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } finally {
            conn.close();
        }
    }

    /**
     * Populate the tables having id and name, such as District and Region.
     *
     * @param tableName
     *            the table name to populate.
     * @param datas
     *            the list of state data
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkBaseEntity(String tableName, List<List<String>> datas) throws Exception {
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            String sql = "INSERT INTO " + tableName + " VALUES(?, ?)";
            pstmt = conn.prepareStatement(sql);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, ds.get(1));

                pstmt.addBatch();
            }
            pstmt.executeBatch();

        } finally {
            conn.close();
        }
    }

    /**
     * Populate the ProductTyp table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkProductType(List<List<String>> datas) throws Exception {
        String INSERT_PRODUCTTYPE_SQL = "INSERT INTO ProductType VALUES(?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_PRODUCTTYPE_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, ds.get(1));
                pstmt.setString(3, "creation_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Market table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkMarket(List<List<String>> datas) throws Exception {
        String INSERT_MARKET_SQL = "INSERT INTO Market VALUES(?, ?, ?,?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_MARKET_SQL);
            for (List<String> ds : datas) {

                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, ds.get(1));
                pstmt.setString(3, ds.get(2));
                pstmt.setString(4, ds.get(3));
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(6, "modification_user");
                pstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Branch table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkBranch(List<List<String>> datas) throws Exception {
        String INSERT_BRANCH_SQL = "INSERT INTO Branch VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_BRANCH_SQL);
            for (List<String> ds : datas) {

                if (ds == null || ds.size() == 0 || ds.get(0).trim().length() == 0) {
                    continue;
                }

                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, parseString(ds.get(1)));
                pstmt.setString(3, ds.get(2));
                pstmt.setString(4, parseString(ds.get(3)));
                pstmt.setString(5, parseString(ds.get(4)));
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(7, "modification_user");
                pstmt.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setDouble(9, parseDouble(ds.get(8)) == 0 ? 1 : parseDouble(ds.get(8))); // market
                pstmt.setString(10, parseString(ds.get(9)));
                pstmt.setString(11, parseString(ds.get(10))); // phone number
                pstmt.setString(12, parseString(ds.get(11))); // fax number
                pstmt.setDouble(13, parseDouble(ds.get(12))); // region
                pstmt.setDouble(14, parseDouble(ds.get(13))); // district
                pstmt.setString(15, "contactName");
                pstmt.setString(16, parseString(ds.get(15))); // generalManagerName
                pstmt.setString(17, parseString(ds.get(16)));
                pstmt.setString(18, "address2");
                pstmt.setString(19, parseString(ds.get(18))); // city
                pstmt.setInt(20, parseInt(ds.get(19))); // state id
                pstmt.setString(21, parseString(ds.get(20))); // zipcode
                pstmt.setDouble(22, parseDouble(ds.get(21))); // price
                pstmt.setDouble(23, 2); // rate
                                        // /////////////////////////////////////////////
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Category table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkCategories(List<List<String>> datas) throws Exception {
        String INSERT_CATEGORY_SQL = "INSERT INTO Category VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_CATEGORY_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, ds.get(1)); // name
                pstmt.setString(3, ds.get(2)); // description
                pstmt.setString(4, "creation_user");
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(6, "modification_user");
                pstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(8, 1);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Vendor table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertVendors(List<List<String>> datas) throws Exception {
        String INSERT_VENDOR_SQL = "INSERT INTO Vendor VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_VENDOR_SQL);
            for (List<String> ds : datas) {
                if (ds.size() <= 1 || ds.get(0).trim().length() == 0) {
                    continue;
                }
                // populate the vendor
                pstmt.setInt(1, parseInt(ds.get(0)));
                pstmt.setString(2, ds.get(1)); // vendorNumber
                pstmt.setString(3, ds.get(2)); // name
                pstmt.setString(4, "add1");
                pstmt.setString(5, "add2");
                pstmt.setString(6, "city");

                pstmt.setInt(7, parseInt(ds.get(6))); // state id
                pstmt.setString(8, parseString(ds.get(7))); // zip phoneNumber1
                pstmt.setString(9, "000-000-0000");
                pstmt.setString(10, "000-000-0000");
                pstmt.setString(11, "000-000-0000");
                pstmt.setString(12, "contact1Name");
                pstmt.setString(13, "email@domain.com");
                pstmt.setString(14, "contact2Name");
                pstmt.setString(15, "email@domain.com");
                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } finally {
            conn.close();
        }
    }

    /**
     * Populate the ShipPoint table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertShipPoints(List<List<String>> datas) throws Exception {
        String INSERT_SHIPPOINT_SQL = "INSERT INTO ShipPoint VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt1;
        try {
            pstmt1 = conn.prepareStatement(INSERT_SHIPPOINT_SQL);
            for (List<String> ds : datas) {
                if (ds.size() <= 1) {
                    continue;
                }
                // populate the shippoint
                pstmt1.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt1.setString(2, ds.get(1)); // name
                pstmt1.setString(3, ds.get(2)); // desc
                pstmt1.setString(4, ds.get(4)); // zip code
                pstmt1.setString(5, "000-000-0000");
                pstmt1.setString(6, "000-000-0000");
                pstmt1.setString(7, "contact1Name");
                pstmt1.setString(8, "email@domain.com");
                pstmt1.setString(9, "contact2Name");
                pstmt1.setString(10, "email@domain.com");
                pstmt1.setString(11, "000-000-0000");
                pstmt1.setInt(12, Integer.parseInt(ds.get(12)));
                pstmt1.setInt(13, Integer.parseInt(ds.get(13)));
                if (ds.size() > 13 && ds.get(14).trim().length() > 0) {
                    pstmt1.setInt(14, Integer.parseInt(ds.get(14)));
                } else {
                    pstmt1.setNull(14, Types.INTEGER);
                }
                pstmt1.addBatch();
            }

            pstmt1.executeBatch();

        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Product table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkProduct(List<List<String>> datas) throws Exception {
        String INSERT_PRODUCT_SQL = "INSERT INTO Product VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_PRODUCT_SQL);
            for (List<String> ds : datas) {
                if (ds.size() <= 1 || ds.get(0).trim().length() == 0) {
                    continue;
                }
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, parseString(ds.get(1)));
                pstmt.setString(3, parseString(ds.get(2)));
                pstmt.setString(4, "createuser");
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(6, "updateuser");
                pstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));

                pstmt.setString(8, parseString(ds.get(7))); // alternateCode1
                pstmt.setDouble(9, parseDouble(ds.get(8))); // hundredWeight
                if (ds.get(9) == null || ds.get(9).trim().length() == 0) { // listPrice
                    pstmt.setNull(10, java.sql.Types.DOUBLE);
                } else {
                    pstmt.setDouble(10, parseDouble(ds.get(9)));
                }

                pstmt.setInt(11, Integer.parseInt(ds.get(10))); // category_id
                pstmt.setInt(12, Integer.parseInt(ds.get(11))); // product_type_id
                pstmt.setInt(13, 3); // ship id
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Role table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkRole(List<List<String>> datas) throws Exception {
        String INSERT_ROLE_SQL = "INSERT INTO Role VALUES(?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_ROLE_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, ds.get(1));
                pstmt.setString(3, "creation_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the user_table, User_Role and User_Branch tables with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkUser(List<List<String>> datas) throws Exception {
        String INSERT_USER_SQL = "INSERT INTO user_table VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_USER_SQL);
            for (List<String> ds : datas) {

                // create data for user.
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, parseString(ds.get(1)));
                pstmt.setString(3, parseString(ds.get(2)));
                pstmt.setString(4, parseString(ds.get(3)));
                pstmt.setString(5, "create_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(7, "modification_user");
                pstmt.setDate(8, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setDate(9, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(10, 9);
                pstmt.setString(11, "firstname");
                pstmt.setString(12, "lastname");
                pstmt.setString(13, "title");
                pstmt.setString(14, "010-89751254");
                pstmt.setString(15, "13992142121");
                pstmt.setString(16, "010-87875451");
                pstmt.setString(17, "email@topcoder.com");
                pstmt.setTimestamp(18, new Timestamp(System.currentTimeMillis()));
                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Destination table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkDestination(List<List<String>> datas) throws Exception {
        String INSERT_DESTINATION_SQL = "INSERT INTO Destination VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_DESTINATION_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, ds.get(1));
                pstmt.setString(3, parseString(ds.get(2)));
                pstmt.setString(4, "creation_user");
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(6, "modification_user");
                pstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setString(9, parseString(ds.get(8)));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the Origination table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBulkOrigination(List<List<String>> datas) throws Exception {
        String INSERT_DESTINATION_SQL = "INSERT INTO Origination VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_DESTINATION_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setString(2, parseString(ds.get(1)));
                pstmt.setString(3, parseString(ds.get(2)));
                pstmt.setString(4, "creation_user");
                pstmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(6, "modification_user");
                pstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setInt(9, parseInt(ds.get(8)));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the OriginationToStateFreightRate table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertOriginationToStateFreightRate(List<List<String>> datas) throws Exception {
        String INSERT_OTSFR_SQL = "INSERT INTO OriginationToStateFreightRate VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_OTSFR_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setDouble(2, parseDouble(ds.get(1)));
                pstmt.setString(3, "creation_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(7, parseInt(ds.get(6)));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setDouble(9, parseDouble(ds.get(8)));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the OgnToDstFreightRate table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertOriginationToDestinationFreight(List<List<String>> datas) throws Exception {
        String INSERT_OTDF_SQL = "INSERT INTO OgnToDstFreightRate VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_OTDF_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, Integer.parseInt(ds.get(0)));
                pstmt.setDouble(2, parseDouble(ds.get(1)));
                pstmt.setString(3, "creation_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(7, parseInt(ds.get(6)));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setDouble(9, parseDouble(ds.get(8)));
                pstmt.setDouble(10, parseDouble(ds.get(9)));
                pstmt.setDouble(11, parseDouble(ds.get(10)));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the PricingRecord table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertPricingRecord(List<List<String>> datas) throws Exception {
        String INSERT_PRICINGRECORD_SQL = "INSERT INTO PricingRecord VALUES(?, ?, ?,  ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_PRICINGRECORD_SQL);
            for (List<String> ds : datas) {

                if (ds == null || ds.size() == 0 || ds.get(0).trim().length() == 0) {
                    continue;
                }
                pstmt.setInt(1, parseInt(ds.get(0)));
                pstmt.setInt(2, parseInt(ds.get(1)));
                pstmt.setInt(3, parseInt(ds.get(2)));
                pstmt.setInt(4, parseInt(ds.get(3)));

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the CWPricingRecord table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertCWPricingRecord(List<List<String>> datas) throws Exception {
        String INSERT_CWPR_SQL = "INSERT INTO CWPricingRecord VALUES(?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;

        try {
            pstmt = conn.prepareStatement(INSERT_CWPR_SQL);
            for (List<String> ds : datas) {

                if (ds == null || ds.size() == 0 || ds.get(0).trim().length() == 0) {
                    continue;
                }

                pstmt.setInt(1, parseInt(ds.get(0)));
                pstmt.setInt(2, parseInt(ds.get(1)));
                pstmt.setInt(3, parseInt(ds.get(2)));
                pstmt.setInt(4, parseInt(ds.get(3)));
                pstmt.setInt(5, parseInt(ds.get(4)));

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the NewPPT table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertNewPPT(List<List<String>> datas) throws Exception {
        String INSERT_NEWPPT_SQL = "INSERT INTO NewPPT VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_NEWPPT_SQL);
            for (List<String> ds : datas) {

                if (ds == null || ds.size() == 0 || ds.get(0).trim().length() == 0) {
                    continue;
                }

                pstmt.setInt(1, parseInt(ds.get(0)));
                DateFormat df = new SimpleDateFormat("yyyyMMDD");
                Date start = df.parse("20110101");
                Date end = df.parse("20160101");
                pstmt.setDate(2, new java.sql.Date(start.getTime()));

                pstmt.setString(3, "create_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));

                pstmt.setDate(7, new java.sql.Date(end.getTime()));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setInt(9, parseInt(ds.get(8)));
                pstmt.setString(10, "PricingAdjustmentcol");

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the NewListAndMultiplier table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertNewListAndMultiplier(List<List<String>> datas) throws Exception {
        String INSERT_NLANDM_SQL = "INSERT INTO NewListAndMultiplier VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;

        try {
            pstmt = conn.prepareStatement(INSERT_NLANDM_SQL);
            for (List<String> ds : datas) {

                if (ds == null || ds.size() == 0 || ds.get(0).trim().length() == 0) {
                    continue;
                }
                pstmt.setInt(1, parseInt(ds.get(0)));
                DateFormat df = new SimpleDateFormat("yyyyMMDD");
                Date start = df.parse("20110101");
                Date end = df.parse("20160101");
                pstmt.setDate(2, new java.sql.Date(start.getTime()));

                pstmt.setString(3, "create_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));

                pstmt.setDate(7, new java.sql.Date(end.getTime()));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setInt(9, parseInt(ds.get(8)));
                pstmt.setInt(10, 4);
                pstmt.setString(11, "PricingAdjustmentcol");

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the PricingAdjustment table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db
     */
    private void insertPricingAdjustment(List<List<String>> datas) throws Exception {
        String INSERT_PRICINGADJUSTMENT_SQL = "INSERT INTO PricingAdjustment VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_PRICINGADJUSTMENT_SQL);
            for (List<String> ds : datas) {

                pstmt.setInt(1, parseInt(ds.get(0)));
                DateFormat df = new SimpleDateFormat("yyyyMMDD");
                Date start = df.parse("20110101");
                Date end = df.parse("20160101");
                pstmt.setDate(2, new java.sql.Date(start.getTime()));

                pstmt.setString(3, "create_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));

                pstmt.setDate(7, new java.sql.Date(end.getTime()));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setString(9, ds.get(8));
                pstmt.setInt(10, parseInt(ds.get(9))); // record_id
                pstmt.setInt(11, parseInt(ds.get(10))); // branch_id
                pstmt.setString(12, "This is a comment");

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the NewFreightRate table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertNewFreightRate(List<List<String>> datas) throws Exception {
        String INSERT_NEWFREIGHTRATE_SQL = "INSERT INTO NewFreightRate VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_NEWFREIGHTRATE_SQL);
            for (List<String> ds : datas) {

                pstmt.setInt(1, parseInt(ds.get(0)));
                DateFormat df = new SimpleDateFormat("yyyyMMDD");
                Date start = df.parse("20110101");
                Date end = df.parse("20160101");
                pstmt.setDate(2, new java.sql.Date(start.getTime()));

                pstmt.setString(3, "create_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));

                pstmt.setDate(7, new java.sql.Date(end.getTime()));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setString(9, parseString(ds.get(8)));
                pstmt.setInt(10, parseInt(ds.get(9))); // record_id
                pstmt.setInt(11, parseInt(ds.get(10))); // branch_id
                pstmt.setString(12, "PricingAdjustmentcol");

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the BranchOverride table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertBranchOverride(List<List<String>> datas) throws Exception {
        String INSERT_BRANCHOVERRIDE_SQL = "INSERT INTO BranchOverride VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_BRANCHOVERRIDE_SQL);
            for (List<String> ds : datas) {

                pstmt.setInt(1, parseInt(ds.get(0)));
                DateFormat df = new SimpleDateFormat("yyyyMMDD");
                Date start = df.parse("20110101");
                Date end = df.parse("20160101");
                pstmt.setDate(2, new java.sql.Date(start.getTime()));

                pstmt.setString(3, "create_user");
                pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(5, "modification_user");
                pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));

                pstmt.setDate(7, new java.sql.Date(end.getTime()));
                pstmt.setInt(8, parseInt(ds.get(7)));
                pstmt.setString(9, parseString(ds.get(8)));
                pstmt.setInt(10, parseInt(ds.get(9))); // record_id
                pstmt.setInt(11, parseInt(ds.get(10))); // branch_id
                pstmt.setString(12, "PricingAdjustmentcol");

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the MyPricing table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertMyPricing(List<List<String>> datas) throws Exception {
        String INSERT_MYPRICING_SQL = "INSERT INTO MyPricing VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_MYPRICING_SQL);
            for (List<String> ds : datas) {
                pstmt.setInt(1, parseInt(ds.get(0)));

                pstmt.setString(2, parseString(ds.get(1)));
                pstmt.setDouble(3, parseDouble(ds.get(2)));
                pstmt.setString(4, "create_user");
                pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                pstmt.setString(6, "modification_user");
                pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

                pstmt.setInt(8, parseInt(ds.get(7))); // status
                pstmt.setInt(9, parseInt(ds.get(8))); // priceRecord
                pstmt.setInt(10, parseInt(ds.get(9))); // cwPriceRecord
                pstmt.setInt(11, parseInt(ds.get(10))); // criteria

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the tables which have two id columns with the data, such as
     * User_Role, User_Branch.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @param table
     *            the table name to populate.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertRelateTable(List<List<String>> datas, String table) throws Exception {
        String INSERT_SQL = "INSERT INTO " + table + " VALUES(?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_SQL);
            for (List<String> ds : datas) {

                pstmt.setInt(1, parseInt(ds.get(0)));
                pstmt.setInt(2, parseInt(ds.get(1)));

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the UserFilterRecord table with the data.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertUserFilterRecord(List<List<String>> datas) throws Exception {
        String INSERT_UFR_SQL = "INSERT INTO UserFilterRecord VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(INSERT_UFR_SQL);
            for (List<String> ds : datas) {

                pstmt.setInt(1, parseInt(ds.get(0)));

                pstmt.setString(2, parseString(ds.get(1)));
                pstmt.setInt(3, parseInt(ds.get(2)));
                pstmt.setInt(4, parseInt(ds.get(3)));
                pstmt.setString(5, "create_user");
                pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis() + 1000));
                pstmt.setString(7, "modification_user");
                pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis() + 1000));
                pstmt.setInt(9, parseInt(ds.get(8))); // readyToExport
                pstmt.setInt(10, 0); // isCriteria

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Populate the insertUserFilterRecord_Has_xxx tables with the data, such as
     * UserFilterRecord_ProductType, UserFilterRecord_Vendor,
     * UserFilterRecord_Branch, UserFilterRecord_Category and
     * UserFilterRecord_Market.
     *
     * @param datas
     *            the list of data, the inner list contains the columns for
     *            every row.
     * @throws Exception
     *             if error occurs during insert data to db.
     */
    private void insertUserFilterRecordHas(List<List<String>> datas, String table) throws Exception {
        Connection conn = createConnection();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("INSERT INTO " + table + " VALUES(?, ?)");
            for (List<String> ds : datas) {

                pstmt.setInt(1, parseInt(ds.get(0)));
                pstmt.setInt(2, parseInt(ds.get(1)));

                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            conn.close();
        }
    }

    /**
     * Parse the given string to double.
     *
     * @param str
     *            the string to parse.
     * @return the Double type for the given string or 0 if the string is null
     *         or empty or error occurs.
     */
    private Double parseDouble(String str) {
        if (str == null || str.trim().length() == 0) {
            return 0D;
        } else {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException npe) {
                return 0D;
            }
        }
    }

    /**
     * Parse the given string to int.
     *
     * @param str
     *            the string to parse.
     * @return the int type for the given string or 1 if the string is null or
     *         empty or error occurs.
     */
    private int parseInt(String str) {
        if (str == null || str.trim().length() == 0) {
            return 1;
        } else {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException npe) {
                return 1;
            }
        }
    }

    /**
     * Parse the given string.
     *
     * @param str
     *            the string to parse.
     * @return the string itself if it is not null or empty, otherwise, the 4
     *         spaces returns.
     */
    private String parseString(String str) {
        if (str == null || str.trim().length() == 0) {
            return "   ";
        } else {
            return str;
        }
    }

    /**
     * Get the value for the give cell in excel file.
     *
     * @param cell
     *            the XSSFCell.
     * @return the retrieved value of cell.
     */
    public String getValue2007(XSSFCell cell) {
        String value = "";
        if (cell == null) {
            return value;
        }
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BLANK:
            break;
        case Cell.CELL_TYPE_STRING:
            value = cell.getRichStringCellValue().getString();
            break;
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                java.util.Date date = cell.getDateCellValue();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                value = format.format(date);
            } else {
                value = String.valueOf((int) cell.getNumericCellValue());
            }
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            value = "" + cell.getBooleanCellValue();
            break;
        case Cell.CELL_TYPE_FORMULA:
            value = cell.getCellFormula();
            break;
        default:
            System.out.println();
        }
        return value;
    }

    /**
     * Create the db connection.
     *
     * @return the newly db connection.
     * @throws Exception
     *             if error occurs.
     */
    private static Connection createConnection() throws Exception {
        Class.forName(DRIVER_CLASS);
        return DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
    }

    /**
     * the main entry of this program.
     *
     * @param args
     *            the parameters
     * @throws Exception
     *             if error occurs.
     */
    public static void main(String[] args) throws Exception {

        ReadExcelToDBTableTool tool = new ReadExcelToDBTableTool();
        tool.loadExcel2007ToDB(PATH + "War_Room_Data_040513.xlsx");
    }
}
