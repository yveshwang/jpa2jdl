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

## `@Lob` or TextBlob
Images or any other binary information in `Blob (byte[])` is not supported for now. This includes `AnyBlob`, `Blog`, `ImageBlob` or `TextBlob`.

## joining is only done by the `id` field
For now, customised joining is not support.
