[![Build Status](https://travis-ci.org/yveshwang/jpa2jdl.svg?branch=master)](https://travis-ci.org/yveshwang/jpa2jdl)

Title: 		jpa2jdl  
Author: 	@yveshwang  
Date: 		20.06.2017  
Upstream:	https://github.com/Arnaud-Nauwynck/reverse-jpa-2-jhipster-jdl  

# jpa2jdl
Parse existing JPA Entities classes (from .jar/.class) and dump jhipster JDL file.

To run: `gradle execute -Dargs='--packageName=io.github.jhipster.jpa2jdl.example.entities'`
# testing
For a slightly more comprehensive set of tests, we have taken the samples from https://github.com/jhipster/jdl-samples and will cross check the generated JPA entities back to the original JDL. See the unit tests for more details.

# assumptions
Below are some of the assumptions for this tool. In general, this is a juggling act between the naming and conventions in the bean/POJO levels and what actually is going on in the persistence layers. So just keep that in mind. Things can break if the moons and the stars are not aligned.

## required field, `@NotNull` vs `@Column(nullable = false)`
If the JPA annoates either `@NotNull` or `@Column(nullable = false)`, it will then assume the `required` field in JDL

## min and max length
Min length is detected as such in JPA `@Size(min = 2)` for example. Whilst max length is detected by either `@Size(max = 10)` or `@Column(length = 10)`

## id should be skipped
Field annoated with `@Id` is skipped.

## field name vs `@Column(name = 'something')`
The bean name wins over the column name. For example, a field called `dueDate` will be written as such in JDL. Note that when re-generating in jhipster, the column name could be set to `due_date`. This will then affect the db scripts and also the entity managers.

## `@Lob` and Blobs
Images or any other binary information in `Blob (byte[])` is supported partially. This includes `AnyBlob`, `Blog`, `ImageBlob` or `TextBlob`. The reason why it is partial is because 1 blob field in JDL is split into 2 JPA fields; the content field, and the content type descriptor field. 

For example `blob Blob required minbytes(2)` translates to:
```
	public class SomeClass {
		// ...
		@NotNull
	    	@Size(min = 2)
	    	@Lob
		@Column(name = "jhi_blob", nullable = false)
		private byte[] blob;

		@Column(name = "jhi_blob_content_type", nullable = false)
		private String blobContentType;
```
Therefore in order to reverse generate the JDL, additional field/artifact **will** be created. This is not a side-affect free operation at all. To make the matter a little more interesting, `Blob` is a JDL supported type and is prefixed with `jhi_` when generated JPA from JDL. 

To summarise, the jpa2jdl support for `Blob` are as follows:

	* `Blob` are identified by `@Lob` annotations
	* If the annotation is before a `String` type declaration, then `TextBlob` is generated, else everything else is a `AnyBlob`

All in all, `Blob` suppport is not a very easily reversable operation and should be avoided where possible.

## joining is only done by the `id` field
For now, customised joining is not supported.

##  Relationships
For all the relationships, they are summarised below and in more details here https://jhipster.github.io/v2-documentation/jhipster-uml/. 

Assumptions are:

* `ManyToMany`: ensure one entity owns the relationship by specifying `@JoinTable` annotation on the JPA entity, and the ownee (inverse of ownership) is outlined by using the `mappedBy = ` attribute. The relationship is always bidirectional, thus if unidirectional is detected, it will generate a warning and relationship is therefore ignored.
* `OneToOne`: the relationship is fairly straight forward. If it is a bidirectional relationship, the ownee can be identified through the `mappedBy` attribute.
* `OneToMany` is always bidirectional as outlined by jhipster. If we detect any unidirectional relationships, a warning is displayed and the relationship is ignored.
* `ManyToOne` is always unidirectional as outlined by jhipster. If we detect any bidirectional relationships, a warning is displayed and the relationship is ignored.

### 1-1 bidirectional
A car has one driver, a driver has one car.
```
relationship OneToOne {
  Car{driver} to Driver{car}
}
```

### 1-1 unidirectional
A citizen has one passport, a passport has no access to citizen information.
```
relationship OneToOne {
  Citizen{passport} to Passport
}
```

### 1-many bidirectional
A owner can have 0, 1 or more cars. A car has 1 known owner.
```
relationship OneToMany {
  Owner{car} to Car{owner}
}
```

### 1-many unidirectional (not supported)
JHipster does not support this. And if it did, it would look like many-1 relationship below. 
```
relationship OneToMany {
  Owner{car} to Car
}
```

### many-1
A little like the 1-many but reversed.
```
relationship OneToMany {
  Owner{car} to Car
}
```

### many-many
A car could have several drivers and driver have access to several cars.
```
relationship ManyToMany {
  Car{driver} to Driver{car}
}
```
