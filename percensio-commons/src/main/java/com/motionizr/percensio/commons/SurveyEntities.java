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

import com.feedzai.commons.sql.abstraction.ddl.DbColumnConstraint;
import com.feedzai.commons.sql.abstraction.ddl.DbColumnType;
import com.feedzai.commons.sql.abstraction.ddl.DbEntity;

import static com.feedzai.commons.sql.abstraction.dml.dialect.SqlBuilder.dbEntity;

/**
 * Database entities used to store survey data.
 * <p/>
 * Currently we are only storing the results of the dialed calls.
 *
 * @author Miguel Bento (migueljbento@gmail.com)
 * @version 1.0.0
 */
public class SurveyEntities {

    /**
     * Table and column names for a {@link CallResult}.
     */
    public static final String CALL_RESULT_TABLE = "CALL_RESULT";
    public static final String CALL_RESULT_SID = "SID";
    public static final String CALL_RESULT_TO = "TO";
    public static final String CALL_RESULT_DURATION = "DURATION";
    public static final String CALL_RESULT_STATUS = "STATUS";
    public static final String CALL_RESULT_DATE = "DATE";
    public static final String CALL_RESULT_HUMAN_ANSWERED = "HUMAN_ANSWERED";
    public static final String CALL_RESULT_DIRECTION = "DIRECTION";
    public static final String CALL_RESULT_DIGITS = "DIGITS_PRESSED";

    /**
     * The database entity that represents a {@link CallResult}.
     */
    public static final DbEntity CALL_RESULT_ENTITY = dbEntity()
            .name(CALL_RESULT_TABLE)
            .addColumn(CALL_RESULT_SID,             DbColumnType.STRING,    64, DbColumnConstraint.UNIQUE)
            .addColumn(CALL_RESULT_TO,              DbColumnType.STRING,    32, DbColumnConstraint.NOT_NULL)
            .addColumn(CALL_RESULT_DURATION,        DbColumnType.INT,           DbColumnConstraint.NOT_NULL)
            .addColumn(CALL_RESULT_STATUS,          DbColumnType.INT,           DbColumnConstraint.NOT_NULL)
            .addColumn(CALL_RESULT_DATE,            DbColumnType.LONG)
            .addColumn(CALL_RESULT_HUMAN_ANSWERED,  DbColumnType.BOOLEAN)
            .addColumn(CALL_RESULT_DIRECTION,       DbColumnType.STRING,    16, DbColumnConstraint.NOT_NULL)
            .addColumn(CALL_RESULT_DIGITS,          DbColumnType.STRING,    32)
            .pkFields(CALL_RESULT_SID)
            .build();

}
