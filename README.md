# Spring User Module
Spring module for secure user management.

## Dependency
[![](https://jitpack.io/v/ESchouten/springusermodule.svg)](https://jitpack.io/#ESchouten/springusermodule)

**Maven:**

Add the following dependency:
```
<repositories>
    <repository>
    	<id>jitpack.io</id>
    	<url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.ESchouten</groupId>
    <artifactId>SpringUserModule</artifactId>
    <version>${springusermodule.version}</version>
</dependency>
```
**Gradle:**
```
repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	implementation 'com.github.ESchouten:SpringUserModule:$springusermodule.version'
}
```
## Implementation
Configure the endpoint access rights in the Spring Security Config.

**Example:**
```
    override fun configure(httpSecurity: HttpSecurity) {
            httpSecurity.headers().cacheControl()
    
            httpSecurity
                    .authorizeRequests()
                    .mvcMatchers("/user/**").hasRole("USERS")
                    .mvcMatchers("/user").hasRole("USERS")
                
                **Etc**
```
Add the libraries to the componentscans:
```
@SpringBootApplication(scanBasePackages = ["com.erikschouten.usermodule", ...])
@EntityScan(basePackages = ["com.erikschouten.usermodule", ...])
@EnableJpaRepositories(basePackages = ["com.erikschouten.usermodule.repository", ...])

```
## Built with
* Spring Security - https://spring.io/
* Spring Data - https://spring.io/# SpringUserModule
