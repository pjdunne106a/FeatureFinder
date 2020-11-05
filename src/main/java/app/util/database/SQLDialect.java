package app.util.database;

import java.sql.Types;

import org.hibernate.dialect.Dialect;

public class SQLDialect extends Dialect {
	
	public SQLDialect() {
		registerColumnType(Types.BIT, "integer");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.REAL, "real");
		registerColumnType(Types.DOUBLE, "double");
		registerColumnType(Types.NUMERIC, "numeric");
		registerColumnType(Types.DECIMAL, "decimal");
		registerColumnType(Types.CHAR, "char");
		registerColumnType(Types.VARCHAR, "varchar");
		registerColumnType(Types.LONGVARCHAR, "longvarchar");
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		registerColumnType(Types.BINARY, "blob");
		registerColumnType(Types.VARBINARY, "blob");
		registerColumnType(Types.LONGVARBINARY, "blob");
		registerColumnType(Types.BLOB, "blob");
		registerColumnType(Types.CLOB, "clob");
		registerColumnType(Types.BOOLEAN, "integer");
		
		registerFunction("concat", new VarArgsSQLFunction(StringType.INSTANCE, "", "||", "");
		registerFunction("mod", new SQLFunctionTemplate(StringType.INSTANCE, "?1 % ?2");
		registerFunction("substr", new StandardSQLFunction("substr",StringType.INSTANCE);
		registerFunction("substring", new StandardSQLFunction("substr",StringType.INSTANCE);
		
		public boolean supportsIdentityColumns() {
			return true;
		}
		
		public boolean hasDataTypeInIdentityColumn() {
			return false;
		}
		
		public String getIdentityColumnString() {
			return "integer";
		}
		
		public String getIdentitySelectString() {
			return "select last_insert_rowid()";
		}
		
		public boolean supportsLimit() {
			return true;
		}
		
		
		protected String getLimitString(String query, boolean hasOffset) {
			return new StringBuffer(query.length() + 20).append(query).append(hasOffset ? " limit ? offset ?" : " limit ?").toString();
		}	
		}
	}

}
