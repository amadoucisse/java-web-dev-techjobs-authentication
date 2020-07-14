# Welcome to TechJobs Authentication

> Note: Is it me or are there things here that are also called "java-web-dev-techjobs-persistent" even though we are working with "java-web-dev-techjobs-authentication"?
>   * Nope, turns out this project must have been forked form "persistent". Someone forgot to update the `settings.gradle` file and change 
>   ```
>   rootProject.name = 'java-web-dev-techjobs-persistent'
>   ```
>   to
>   ```
>   rootProject.name = 'java-web-dev-techjobs-authentication'
>   ```
>   Update that. Thing should be resolved, hopefully.
>   In that case, it might be worth working on "persistent" and "authentication" at the same time. At least for their respective first parts that require setup.

If you are reading this, congratulations! You are almost at the end of the Java Unit...or you still have TechJobs Persistent to work on.

Either way, there are four parts to this Studio.

1. Fork this code! Then set up gradle and MySQL. They should look similar to mine here.
    * You'll still need to go to the settings and set which version of Gradle to use if you get that error message.
    * [ ] Set your `build.gradle` file to get the packages we need to run. 
        * We need to use a slightly older version of Spring Boot. (version `2.2.7.RELEASE`).
        * WE can use our current Spring dependency management (`1.0.9.RELEASE`)
        * Don't forget to upgrade the Java Version (`sourceCompatiblity = '14'`)
        * Several packages probably need to be added. 
            * It looks like all the dependencies are accounted for.
              ```
              dependencies {
              	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
              	implementation 'org.springframework.boot:spring-boot-starter-web'
              	implementation 'org.springframework.security:spring-security-crypto'
              	developmentOnly 'org.springframework.boot:spring-boot-devtools'
              	testImplementation('org.springframework.boot:spring-boot-starter-test') {
              		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
              	}
              	compile 'org.springframework.boot:spring-boot-starter-data-jpa'
              	compile 'mysql:mysql-connector-java'
              }
              ```
            * It looks like everything is there, but it might be better to change it to this.
              ```
              dependencies {
              	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
              	implementation 'org.springframework.boot:spring-boot-starter-web'
              	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
              	implementation 'mysql:mysql-connector-java'
              	implementation 'org.springframework.security:spring-security-crypto'
              	developmentOnly 'org.springframework.boot:spring-boot-devtools'
              	testImplementation('org.springframework.boot:spring-boot-starter-test') {
              		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
              	}
              }
              ```
    * [ ] Set your `gradle/wrapper/gradle.properties` file.
        * We'll need ot use a slightly older version of our Gradle Wrapper (version `6-3 all`).
    * [ ] Set your `src/java/resources/application.properties` file.
        * It should be ready. If not, copypasta.
          ```
          # Database connection settings
          spring.datasource.url=jdbc:mysql://localhost:3306/techjobs_auth
          spring.datasource.username=techjobs_auth
          spring.datasource.password=auth
          ```
          But wait! The JPA Hibernate dialect might be set for an older dialect.
          ```
          spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect 
          ```
          If that is the case, change it to this.
          ```
          spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL8Dialect 
          ```
        * You might still need to get into the MySQL Workbench and create the user and schema (database) (15.4.)
            * You WILL need to create a new user.
                1. In MySQL Workbench click on a Connection on the home page.
                2. In The toolbar click the "Create new schema" button (fourth button from the left that has a cylinder icon).
                3. A new tab should open to create a new schema. We will call this one `techjobs_auth`. Fill in the name field only, you won't need to change anything else, then click Apply then Finish.
                4. You'll see a prompt asking you to confirm running this line. Click Apply.
                   ```
                   CREATE SCHEMA `techjobs_auth` ;
                   ```
                5. On the left hand side, in the `Navigator` pane select the `Administration` tab. Under the `Management` section, click on `Users and Privileges`. A new tab should show up to the right of it.
                6. In the `Adminstration - Users and Priviledges` tab, click on the `Add Account` button.
                7. In the `Details` pane there are two tabs that will need our attention: `Login` and `Schema Privileges`.
                    * In the `Login` tab set the fields as followed:
                        * Login Name: `techjobs_auth`
                        * Authentication Type: `Standard`
                        * Limit to Hosts Matching: `localhost`
                        * Password: `auth`
                        * Confirm Password: `auth`
                    * In the `Schema Privileges` tab, there should be a table and some buttons. Click on the `Add Entry...` button.
                        * A new window will appear with radio buttons. Select the `Selected Schema` radio button, then in the dropdown menu look for the `techjobs_auth` database. Click `OK` when you've done these things.
                        * If you don't see the `Select "All"`, resize the `Output` pain so you can see it, then click on the `Select "All"` button to apply all privileges to user `techjobs_auth` to be granted for the database `techjobs_auth`.
                        * Click `Apply` to complete this part. Your database should be ready be used.
    * [ ] Back in Intellij IDEA, Hit that blue elephant icon.
2. The User Model. Pretty much the same as `code_events` Authentication from 19.3.
3. The Login and Registration Forms. Same as 19.4.
4. Filter Requests. Same as 19.5.
5. SUBMIT THIS ASSIGNMENT!

## Setup Gradle
More than likely using an older version. (Think Chapter 18 from ORM `code_events`)
