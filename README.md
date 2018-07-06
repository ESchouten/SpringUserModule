# UserModule
Spring module for user management.

## Dependency
**Maven:**

Create a folder 'lib' in the project root, and add the UserModule.jar in it.

Add the following dependency:
```
<dependency>
    <groupId>com.erikschouten</groupId>
    <artifactId>UserModule</artifactId>
    <version>0.1.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/UserModule.jar</systemPath>
</dependency>
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
