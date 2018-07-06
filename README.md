# UserModule
Spring module for user management.

## Dependency
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
    <version>0.1.1</version>
</dependency>
```
**Gradle:**
```
repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	implementation 'com.github.ESchouten:SpringUserModule:0.1.1'
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
