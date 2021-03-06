/*
 * $Id: Burst.java,v 1.9 2006/08/21 13:32:23 blowagie Exp $
 * $Name:  $
 *
 * Copyright 2005 by Bruno Lowagie, based on an example by Mark Thompson
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.tools.plugins;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JInternalFrame;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.LabelAccessory;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you split a PDF in several separate PDF files (1 per page).
 */
public class Burst extends AbstractTool {

	static {
		addVersion("$Id: Burst.java,v 1.9 2006/08/21 13:32:23 blowagie Exp $");
	}

	/**
	 * Constructs a Burst object.
	 */
	public Burst() {
		FileArgument f = new FileArgument(this, "srcfile", "The file you want to split", false, new PdfFilter());
		f.setLabel(new LabelAccessory());
		arguments.add(f);
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Burst", true, false, true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== Burst OPENED ===");
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
        try {
			if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
			File src = (File)getValue("srcfile");
            File directory = src.getParentFile();
            String name = src.getName();
            name = name.substring(0, name.lastIndexOf("."));
        	// we create a reader for a certain document
			PdfReader reader = new PdfReader(src.getAbsolutePath());
			// we retrieve the total number of pages
			int n = reader.getNumberOfPages();
			int digits = 1 + (n / 10);
			System.out.println("There are " + n + " pages in the original file.");
			Document document;
			int pagenumber;
			String filename;
            for (int i = 0; i < n; i++) {
            	pagenumber = i + 1;
            	filename = String.valueOf(pagenumber);
            	while (filename.length() < digits) filename = "0" + filename;
            	filename = "_" + filename + ".pdf";
            	// step 1: creation of a document-object
            	document = new Document(reader.getPageSizeWithRotation(pagenumber));
				// step 2: we create a writer that listens to the document
            	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(directory, name + filename)));
            	// step 3: we open the document
            	document.open();
            	PdfContentByte cb = writer.getDirectContent();
				PdfImportedPage page = writer.getImportedPage(reader, pagenumber);
				int rotation = reader.getPageRotation(pagenumber);
				if (rotation == 90 || rotation == 270) {
					cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(pagenumber).height());
				}
				else {
					cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
				}
				// step 5: we close the document
				document.close();
			}
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#valueHasChanged(com.lowagie.tools.arguments.ToolArgument)
	 */
	public void valueHasChanged(ToolArgument arg) {
		if (internalFrame == null) {
			// if the internal frame is null, the tool was called from the commandline
			return;
		}
		// represent the changes of the argument in the internal frame
	}


    /**
     * Divide PDF file into pages.
     * @param args
     */
	public static void main(String[] args) {
    	Burst tool = new Burst();
    	if (args.length < 1) {
    		System.err.println(tool.getUsage());
    	}
    	tool.setArguments(args);
        tool.execute();
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#getDestPathPDF()
	 */
	protected File getDestPathPDF() throws InstantiationException {
		throw new InstantiationException("There is more than one destfile.");
	}
}
