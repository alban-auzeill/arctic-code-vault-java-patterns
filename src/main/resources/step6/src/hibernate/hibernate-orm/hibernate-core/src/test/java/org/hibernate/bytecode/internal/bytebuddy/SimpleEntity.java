/* (rank 63) copied from https://github.com/hibernate/hibernate-orm/blob/5eedda9a467fef44d924f64203023b2345b8415f/hibernate-core/src/test/java/org/hibernate/bytecode/internal/bytebuddy/SimpleEntity.java
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.bytecode.internal.bytebuddy;

import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "SimpleEntity")
public class SimpleEntity {

	private static final Pattern PATTERN = Pattern.compile( "whatever" );

	@Id
	@GeneratedValue
	private Long id;

	@Basic(fetch = FetchType.LAZY)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
