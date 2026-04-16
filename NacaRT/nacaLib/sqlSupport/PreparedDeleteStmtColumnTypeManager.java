/*
 * NacaRT - Naca RunTime for Java Transcoded Cobol programs v1.2.0.
 *
 * Copyright (c) 2005, 2006, 2007, 2008, 2009 Publicitas SA.
 * Licensed under LGPL (LGPL-LICENSE.txt) license.
 */
package nacaLib.sqlSupport;

import java.util.ArrayList;
import jlib.misc.ArrayFixDyn;
import jlib.sql.DbConnectionBase;
import jlib.sql.OracleColumnDefinition;

public class PreparedDeleteStmtColumnTypeManager extends PreparedStmtColumnTypeManager
{
	private String m_csTableName = null;
	private ArrayList<String> m_arrColNames = null;
	
	PreparedDeleteStmtColumnTypeManager(String csQueryUpper)
	{		
		super(csQueryUpper); 
	}
	
	boolean analyse(ArrayFixDyn<String> arrMarkerNames)
	{
//		m_csTableName = extractTableName();
//		if(m_csTableName != null)
//		{
//			m_arrColNames = extractColNames();
//			if(m_arrColNames != null)
//				return true;
//		}
		return false;
	}
	
	public synchronized OracleColumnDefinition getOracleColumnDefinition(DbConnectionBase dbConnection, String csSharpName)
	{
		if(m_csTableName == null)
			return null;
		
//		OracleTableDefinition oracleTableDefinition = OracleTableDefinitionManager.getOrFillDefinitionsforTable(dbConnection, m_csTableName);
//		if(oracleTableDefinition != null)	// Found catalog definition for the table  
//		{
//			// Get the column's name
//			if(nParamIndex >= 0 && nParamIndex < m_arrColNames.size())
//			{
//				String csColName = m_arrColNames.get(nParamIndex);	
//				OracleColumnDefinition oracleColumnDefinition = oracleTableDefinition.getNamedColumnDefinition(csColName);
//				return oracleColumnDefinition;
//			}
//		}
		return null;
	}
}

