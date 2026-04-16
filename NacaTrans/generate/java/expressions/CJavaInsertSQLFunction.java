/*
 * NacaTrans - Naca Transcoder v1.2.0.
 *
 * Copyright (c) 2008-2009 Publicitas SA.
 * Licensed under GPL (GPL-LICENSE.txt) license.
 */
/*
 * NacaRTTests - Naca Tests for NacaRT support.
 *
 * Copyright (c) 2005, 2006, 2007, 2008 Publicitas SA.
 * Licensed under GPL (GPL-LICENSE.txt) license.
 */
/**
 * 
 */
package generate.java.expressions;

import generate.CBaseLanguageExporter;
import parser.CIdentifier;
import semantic.expression.CEntityInsertSQLFunction;
import utils.CObjectCatalog;

/**
 *
 * @author Pierre-Jean Ditscheid, Consultas SA
 * @version $Id: CJavaInsertSQLFunction.java,v 1.1 2009/05/25 11:32:59 u930di Exp $
 */
public class CJavaInsertSQLFunction extends CEntityInsertSQLFunction
{
	public CJavaInsertSQLFunction(CObjectCatalog cat, CBaseLanguageExporter out, CIdentifier id, String csFormat)
	{
		super(cat, out, id, csFormat);
	}
	
	public String ExportReference(int nLine)
	{
		String cs = "TO_DATE(?, '" + m_csFormat + "')";
		return cs;
	}
	
	public String GetConstantValue()
	{		
		String cs = m_id.GetName(); 
		cs = FormatIdentifier(cs) ;
		return cs;
	}

	
	public boolean isValNeeded()
	{
		return false;
	}
}