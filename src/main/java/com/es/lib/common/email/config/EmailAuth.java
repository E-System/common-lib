/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common.email.config;

import java.io.Serializable;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public class EmailAuth implements Serializable, Cloneable {

	private String login;
	private String password;

	public EmailAuth() {
	}

	public EmailAuth(final String login, final String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		EmailAuth object = (EmailAuth) super.clone();
		object.login = login;
		object.password = password;
		return object;
	}

	@Override
	public String toString() {
		return "EmailAuth [" +
		       "login='" + login + "'" +
		       ", password='" + password + "'" +
		       ']';
	}
}
