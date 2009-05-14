/**   
 * License Agreement for Jaeksoft OpenSearchServer
 *
 * Copyright (C) 2008-2009 Emmanuel Keller / Jaeksoft
 * 
 * http://www.jaeksoft.com
 * 
 * This file is part of Jaeksoft OpenSearchServer.
 *
 * Jaeksoft OpenSearchServer is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * Jaeksoft OpenSearchServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jaeksoft OpenSearchServer. 
 *  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.jaeksoft.searchlib.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import com.jaeksoft.searchlib.index.FieldContent;
import com.jaeksoft.searchlib.index.IndexDocument;

public abstract class Parser {

	private long sizeLimit;

	private IndexDocument document;

	private ParserFieldEnum[] fieldList;

	protected Parser(ParserFieldEnum[] fieldList) {
		this.fieldList = fieldList;
		sizeLimit = 0;
		document = new IndexDocument();
	}

	public void setSizeLimit(long l) {
		sizeLimit = l;
	}

	public void setDocument(IndexDocument sourceDocument) {
		if (sourceDocument != null)
			document = new IndexDocument(sourceDocument);
	}

	public ParserFieldEnum[] getFieldList() {
		return fieldList;
	}

	public void addField(ParserFieldEnum field, String value) {
		if (value == null)
			return;
		if (value.length() == 0)
			return;
		document.add(field.name(), value);
	}

	protected void addField(ParserFieldEnum field, Object object) {
		if (object == null)
			return;
		addField(field, object.toString());
	}

	public FieldContent getFieldContent(ParserFieldEnum field) {
		return document.getField(field.name());
	}

	public IndexDocument getDocument() {
		return document;
	}

	protected abstract void parseContent(LimitInputStream inputStream)
			throws IOException;

	protected abstract void parseContent(LimitReader reader) throws IOException;

	public void parseContent(InputStream inputStream) throws IOException {
		parseContent(new LimitInputStream(inputStream, sizeLimit));
	}

	public void parseContent(Reader reader) throws IOException {
		parseContent(new LimitReader(reader, sizeLimit));
	}

	public void parseContent(byte[] byteData) throws IOException {
		ByteArrayInputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(byteData);
			parseContent(inputStream);
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	public void parseContent(String stringData) throws IOException {
		StringReader stringReader = null;
		try {
			new StringReader(stringData);
			parseContent(stringReader);
		} finally {
			if (stringReader != null)
				stringReader.close();
		}
	}

}
