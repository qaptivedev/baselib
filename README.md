# BaseLib
Base for the apps
# Usage 
## Add dependencies

Currently the GitHub Packages requires us to Authenticate to download an Android Library (Public or Private) hosted on the GitHub Package. This might change for future releases.

## Step 1 : Generate a Personal Access Token for GitHub
    Inside you GitHub account:
    Settings -> Developer Settings -> Personal Access Tokens -> Generate new token
    Make sure you select the following scopes (“ read:packages”) and Generate a token
    After Generating make sure to copy your new personal access token. You cannot see it again! The only option is to generate a new key.

Crate file named github.properties(feel free to change file name)
and add the following
```
gpr.usr=github_user_id
gpr.key=github_Personal_access_tokens
```
In Project build.gradle
```
def githubProperties = new Properties()
githubProperties.load(new FileInputStream(rootProject.file('github.properties')))

allprojects {
    repositories {
        google()
        jcenter()
       maven {
            name = "BaseLib"
            /** Configure path of your package repository on Github
             ** Replace GITHUB_USERID with your/organisation Github userID
             ** and REPOSITORY with the repository name on GitHub
             */
            url = uri("https://maven.pkg.github.com/qaptivedev/baselib")
            credentials {
                /** Create github.properties in root project folder file with
                 ** gpr.usr=GITHUB_USER_ID & gpr.key=PERSONAL_ACCESS_TOKEN
                 ** Set env variable GPR_USER & GPR_API_KEY if not adding a properties file**/

                username = githubProperties['gpr.usr'] ?: System.getenv("GPR_USER")
                password = githubProperties['gpr.key'] ?: System.getenv("GPR_API_KEY")
            }
        }
}
```
In App build.gradle add
```
dependencies {
    ......
    ........
    implementation 'com.qaptive.base:baselib:3.0.0'
    }
```
