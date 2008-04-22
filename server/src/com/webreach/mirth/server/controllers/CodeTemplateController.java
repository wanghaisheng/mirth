/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.server.controllers;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.webreach.mirth.model.CodeTemplate;
import com.webreach.mirth.server.util.SqlConfig;

public class CodeTemplateController {
	private Logger logger = Logger.getLogger(this.getClass());
	private SqlMapClient sqlMap = SqlConfig.getSqlMapInstance();
	private static CodeTemplateController instance = null;

	private CodeTemplateController() {
		
	}
	
	public static CodeTemplateController getInstance() {
		synchronized (CodeTemplateController.class) {
			if (instance == null) {
				instance = new CodeTemplateController();
			}
			
			return instance;
		}
	}
	
	public List<CodeTemplate> getCodeTemplate(CodeTemplate codeTemplate) throws ControllerException {
		logger.debug("getting codeTemplate: " + codeTemplate);

		try {
			List<CodeTemplate> codeTemplates = sqlMap.queryForList("getCodeTemplate", codeTemplate);

			return codeTemplates;
		} catch (SQLException e) {
			throw new ControllerException(e);
		}
	}

	public void updateCodeTemplates(List<CodeTemplate> codeTemplates) throws ControllerException {
		// remove all codeTemplates
		removeCodeTemplate(null);

		for (Iterator iter = codeTemplates.iterator(); iter.hasNext();) {
			CodeTemplate codeTemplate = (CodeTemplate) iter.next();
			insertCodeTemplate(codeTemplate);
		}
	}

	private void insertCodeTemplate(CodeTemplate codeTemplate) throws ControllerException {
		try {
			CodeTemplate codeTemplateFilter = new CodeTemplate();
			codeTemplateFilter.setId(codeTemplate.getId());

			try {
				sqlMap.startTransaction();

				// insert the codeTemplate and its properties
				logger.debug("adding codeTemplate: " + codeTemplate);
				sqlMap.insert("insertCodeTemplate", codeTemplate);
				
				sqlMap.commitTransaction();
			} finally {
				sqlMap.endTransaction();
			}
		} catch (SQLException e) {
			throw new ControllerException(e);
		}
	}

	public void removeCodeTemplate(CodeTemplate codeTemplate) throws ControllerException {
		logger.debug("removing codeTemplate: " + codeTemplate);

		try {
			sqlMap.delete("deleteCodeTemplate", codeTemplate);
		} catch (SQLException e) {
			throw new ControllerException(e);
		}
	}
}