/*
 * JLib - Publicitas Java library v1.2.0.
 *
 * Copyright (c) 2005, 2006, 2007, 2008, 2009 Publicitas SA.
 * Licensed under LGPL (LGPL-LICENSE.txt) license.
 */
/*
 * JLib - Publicitas Java library.
 *
 * Copyright (c) 2005, 2006, 2007, 2008 Publicitas SA.
 * Licensed under LGPL (LGPL-LICENSE.txt) license.
 */
/**
 * 
 */
package jlib.misc;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataFileRead extends BaseDataFileBuffered
{
	private BufferedInputStream m_in = null;
		
	public DataFileRead()
	{
	}
	
	public DataFileRead(String csName)
	{
		m_csName = csName;
	}
		
//	public boolean open(String csName)
//	{
//		setName(csName);
//		return open();
//	}

	private boolean doOpen()
	{
		try
		{
			FileIOAccounting.startFileIO(FileIOAccountingType.Open);
			m_in = new BufferedInputStream(new DataInputStream(new FileInputStream(getName())));
			FileIOAccounting.endFileIO();
			return true;
		}
		catch (FileNotFoundException e)
		{
			FileIOAccounting.endFileIO();
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean open(LogicalFileDescriptor logicalFileDescriptor)
	{
		boolean bOpened = doOpen();
		if(bOpened && logicalFileDescriptor != null)
		{
			logicalFileDescriptor.readFileHeader(this);
		}
		return bOpened; 
	}

	public boolean  close()
	{
		try
		{
			if(m_in != null)
			{
				FileIOAccounting.startFileIO(FileIOAccountingType.Close);
				m_in.close();
				FileIOAccounting.endFileIO();
				m_in = null;
				return true;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean flush()
	{
		return false;
	}

	public boolean isOpen()
	{
		if(m_in != null)
			return true;
		return false;
	}
	
	public String toString()
	{
		String cs = m_csName + " (";
		if(isOpen())
		{
			cs += "Open";
			if(m_in != null)
				cs += " Read";
		}
		else
		{
			cs += "Close";
		}
		cs += ")";
		return cs;
	}
	
	public void write(byte[] tBytes, int nOffset, int nLength)
	{
	}	
	
	public void writeRecord(String cs)
	{
	}

	public void write(byte[] tBytes)
	{
	}
	
	public void write(char c)
	{
	}
	
	
	public void writeWithEOL(byte[] tBytes, int nSize)
	{
	}
	
	public void writeWithOptionalEOL(byte[] tBytes, int nSize, boolean bEndsCRLF, boolean bEndsLF)
	{
	}
	
	public void writeWithOptionalEOLMFCobol(byte[] tBytes, int nSize, boolean bEndsCRLF, boolean bEndsLF)
	{
	}
	
	public void writeWithEOL(LineRead lineRead)
	{
	}
	
	public void writeEndOfRecordMarker()
	{
	}
	
	public boolean readEndOfLineMarker()
	{
		int nByte = 0;
		if(m_in != null)
		{
			try
			{
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				nByte = m_in.read();
				FileIOAccounting.endFileIO();
				if(nByte == -1)
				{
					setEOF(true);
					return false;
				}
				if(nByte == FileEndOfLine.LF)
				{
					setEOF(false);
					return true;	// Found EOL
				}
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				setEOF(true);
				return false;
			}
		}
		setEOF(true);
		return false;
	}
		
	public byte[] read(int nSize)
	{
		if(m_in != null)
		{
			try
			{
				byte byteBuffer[] = getByteBuffer(nSize);
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				int nNBytesRead = m_in.read(byteBuffer, 0, nSize);
				FileIOAccounting.endFileIO();
				if(nNBytesRead == -1)
					setEOF(true);
				return byteBuffer;
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				e.printStackTrace();
			}			
		}
		return null;
	}
	
	public int getUnixRecordLength()
	{
		int n = 0;
		byte[] tVal = new byte[1];
		if(m_in != null)
		{
			try
			{	
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				while(tVal[0] != FileEndOfLine.LF)
				{
					int nNBytesRead = m_in.read(tVal, 0, 1);
					if(nNBytesRead == -1)
					{
						FileIOAccounting.endFileIO();
						setEOF(true);
						return n;
					}
					n++;
				}
				FileIOAccounting.endFileIO();
				return n;
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				e.printStackTrace();
			}			
		}
		return n;
	}
	
	public int readUnixLine(byte tBytes[], int nMaxLineSize)
	{
		int n = 0;
		byte[] tVal = new byte[1];
		if(m_in != null)
		{
			try
			{	
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				while(tVal[0] != FileEndOfLine.LF)
				{
					int nNBytesRead = m_in.read(tVal, 0, 1);
					if(nNBytesRead != -1)
						tBytes[n++] = tVal[0];
					else
					{
						FileIOAccounting.endFileIO();
						setEOF(true);
						return n;
					}
				}
				FileIOAccounting.endFileIO();
				return n;
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				e.printStackTrace();
				setEOF(true);
				return -1;
			}			
		}
		return n;
	}
	
	public int readUnixLine(byte tBytes[], int nOffset, int nMaxLineSize)
	{
		int n = nOffset;
		byte[] tVal = new byte[1];
		if(m_in != null)
		{
			try
			{	
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				while(tVal[0] != FileEndOfLine.LF)
				{
					int nNBytesRead = m_in.read(tVal, 0, 1);
					if(nNBytesRead != -1)
						tBytes[n++] = tVal[0];
					else
					{
						FileIOAccounting.endFileIO();
						setEOF(true);
						return n;
					}
				}
				FileIOAccounting.endFileIO();
				return n;
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				e.printStackTrace();
			}			
		}
		return n;
	}
	
	public int readChunk(byte tBytes[], int nNbBytes)
	{
		int n = -1;
		if(m_in != null && !isEOF())
		{
			try
			{	
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				int nNBytesRead = m_in.read(tBytes, 0, nNbBytes);
				FileIOAccounting.endFileIO();
				if(nNBytesRead == -1)
					setEOF(true);
				return nNBytesRead;
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				e.printStackTrace();
			}			
		}
		return n;
	}
	
	public int readChunk(byte tBytes[], int nOffset, int nNbBytes)
	{
		int n = -1;
		if(m_in != null && !isEOF())
		{
			try
			{	
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				int nNBytesRead = m_in.read(tBytes, nOffset, nNbBytes);
				FileIOAccounting.endFileIO();
				if(nNBytesRead == -1)
					setEOF(true);
				return nNBytesRead;
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				e.printStackTrace();
			}			
		}
		return n;
	}
	
	public byte[] readWholeFileAsArray()
	{
		if(m_in != null)
		{
			int nSize;
			try
			{
				FileIOAccounting.startFileIO(FileIOAccountingType.Read);
				nSize = m_in.available();
				byte tb[] = read(nSize);
				FileIOAccounting.endFileIO();
				return tb; 
			}
			catch (IOException e)
			{
				FileIOAccounting.endFileIO();
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public LineRead readNextUnixLine()
	{
		// Should use a DataFileLineReader
		return null;
	}
	
	public LineRead readNextUnixLineMFCobol()
	{
		// Should use a DataFileLineReader
		return null;
	}
	
	public LineRead readNextLineCRLFTerminated()
	{
		// Should use a DataFileLineReader
		return null;
	}

	public LineRead readBuffer(int nLength, boolean bTryReadNextLF)
	{
		// Should use a DataFileLineReader
		return null;
	}
	
	public LineRead readBufferOptionalEOL(int nLineLength, boolean bTryReadNextCRLF, boolean bTryReadNextLF)
	{
		// Should use a DataFileLineReader
		return null;
	}
	
	public void rewrite(byte[] tBytes, int nOffset, int nLength)
	{
	}
	
	public void rewriteWithEOL(byte[] tbyDest, int nSize)
	{
	}

	public void rewriteWithOptionalEOL(byte[] tbyDest, int nSize, boolean bEndsCRLF, boolean bEndsLF)
	{
	}
	
	public boolean isReadable()
	{
		return true;
	}
	
	public boolean isWritable()
	{
		return false;
	}
	
	public boolean isUpdateable()
	{
		return false;
	}
	
	public long getFileCurrentPosition()
	{
		return -1;
	}
	
	public boolean setFileCurrentPosition(long lCurrentPosition)
	{
		return false;
	}
	
	public boolean savePosition(int nMaxReadAheadSize)
	{
		if(m_in != null && m_in.markSupported())
		{
			FileIOAccounting.startFileIO(FileIOAccountingType.Position);
			m_in.mark(nMaxReadAheadSize);
			FileIOAccounting.endFileIO();
			return true;
		}
		return false;
	}
	
	public boolean returnAtSavedPosition()
	{
		if(m_in != null)
		{
			FileIOAccounting.startFileIO(FileIOAccountingType.Position);
			if(m_in.markSupported())
			{
				try
				{
					m_in.reset();
					FileIOAccounting.endFileIO();
					return true;
				}
				catch (IOException e)
				{
				}
			}
			FileIOAccounting.endFileIO();
		}
		return false;		
	}
	
	public LineRead readVariableLengthLine(boolean bTryReadNextLF, boolean bHeaderIsInt, LineRead lineOut)	// Read a vairable length line (length is given in record header 4 bytes)
	{
		LineRead recordHeader = readBuffer(4, false);
		if(recordHeader != null)
		{
			int nLength = recordHeader.getAsLittleEndingUnsignBinaryInt();
			
			if(lineOut == null)
				lineOut = new LineRead();
			lineOut.resetAndGaranteeBufferStorage(4 + nLength, 4 + nLength);
			lineOut.append(recordHeader);
	
			LineRead recordBody = readBuffer(nLength, bTryReadNextLF);
			lineOut.append(recordBody);
			
			return lineOut;
		}
		return null;
	}
	

}
