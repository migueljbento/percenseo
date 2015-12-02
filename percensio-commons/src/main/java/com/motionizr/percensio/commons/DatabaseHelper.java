/*
 * The MIT License (MIT)
 *
 * Copyright © 2015 Miguel José Carvalho Bento (migueljbento@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.motionizr.percensio.commons;

import com.feedzai.commons.sql.abstraction.engine.DatabaseEngine;
import com.feedzai.commons.sql.abstraction.engine.DatabaseEngineException;
import com.feedzai.commons.sql.abstraction.engine.DatabaseFactory;
import com.feedzai.commons.sql.abstraction.engine.DatabaseFactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Helper class that contains several database related utilities.
 * <p/>
 * Currently it's mainly used to establish and close database connections using PDB.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class DatabaseHelper {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

    /**
     * Initializes the database connection.
     *
     * @param dbFile                    The database file.
     * @throws DatabaseFactoryException If an error occurs getting the database connection.
     * @throws DatabaseEngineException  If an error occurs creating the call result entity.
     */
    public static DatabaseEngine initializeDbConnection(String dbFile) throws DatabaseFactoryException, DatabaseEngineException {
        Properties properties = new Properties() {{
            setProperty("pdb.jdbc", "jdbc:h2:" + dbFile + ";LOCK_TIMEOUT=60000;AUTO_SERVER=TRUE");
            setProperty("pdb.engine", "com.feedzai.commons.sql.abstraction.engine.impl.H2Engine");
            setProperty("pdb.schema_policy", "create");
        }};


        DatabaseEngine engine = DatabaseFactory.getConnection(properties);

        if (!engine.containsEntity(SurveyEntities.CALL_RESULT_TABLE)) {
            engine.addEntity(SurveyEntities.CALL_RESULT_ENTITY);
        }

        return engine;
    }

    /**
     * Closes the database connection.
     *
     * @param engine    The database connection to close.
     */
    public static void closeDbConnection(DatabaseEngine engine) {
        try {
            engine.close();
        } catch (Exception e) {
            logger.error("Unable to close the database connection.", e);
        }
    }
}
